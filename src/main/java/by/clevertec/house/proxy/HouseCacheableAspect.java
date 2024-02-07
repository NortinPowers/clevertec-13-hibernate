package by.clevertec.house.proxy;

import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.house.cache.Cache;
import by.clevertec.house.cache.impl.LfuCache;
import by.clevertec.house.cache.impl.LruCache;
import by.clevertec.house.domain.House;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.repository.HouseRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HouseCacheableAspect {

    private final HouseRepository houseRepository;
    private final HouseMapper mapper;

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.max-collection-size}")
    private Integer maxCollectionSize;
    private Cache<UUID, Object> cache = configureCache();

    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(by.clevertec.house.proxy.Cacheable) && execution(* by.clevertec.house.service.HouseService.getByUuid(..))")
    public Object cacheableGet(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        UUID uuid = (UUID) args[0];
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        HouseResponseDto result;
        try {
            result = (HouseResponseDto) joinPoint.proceed();
        } catch (CustomEntityNotFoundException e) {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(by.clevertec.house.proxy.Cacheable) && execution(* by.clevertec.house.service.HouseService.save(..))", returning = "uuid")
    public void cacheableCreate(UUID uuid) {
        Optional<House> optionalHouse = houseRepository.findByUuid(uuid);
        optionalHouse.ifPresent(house -> cache.put(uuid, mapper.toDto(house)));
    }

    @AfterReturning(pointcut = "@annotation(by.clevertec.house.proxy.Cacheable) && execution(* by.clevertec.house.service.HouseService.delete(..)) && args(uuid)", argNames = "uuid")
    public void cacheableDelete(UUID uuid) {
        cache.remove(uuid);
    }

    @AfterReturning(pointcut = "@annotation(by.clevertec.house.proxy.Cacheable) && execution(* by.clevertec.house.service.HouseService.update(..)) && args(uuid, houseDto)", argNames = "uuid, houseDto")
    public void cacheableUpdate(UUID uuid, HouseRequestDto houseDto) {
        House house = houseRepository.findByUuid(uuid).orElseThrow(() -> CustomEntityNotFoundException.of(House.class, uuid));
        cache.put(uuid, mapper.toDto(house));
    }

    private Cache<UUID, Object> configureCache() {
        if (cache == null) {
            synchronized (this) {
                if (maxCollectionSize == null) {
                    maxCollectionSize = 30;
                }
                if ("lfu".equals(algorithm)) {
                    cache = new LfuCache<>(maxCollectionSize);
                } else {
                    cache = new LruCache<>(maxCollectionSize);
                }
            }
        }
        return cache;
    }
}

