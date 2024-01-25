package by.clevertec.house.config;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayMigration {

    @Value("${flyway.enabled}")
    private Boolean enabled;

    private final DataSource dataSource;

    /**
     * Метод, аннотированный {@code @PostConstruct}, предназначенный для выполнения миграции базы данных
     * при инициализации бина, если активирована соответствующая опция.
     *
     * <p>Пример использования:
     * <pre>
     * {@code @}PostConstruct
     * public void migrateDatabase() {
     *     if (enabled) {
     *         Flyway flyway = Flyway.configure()
     *                 .dataSource(dataSource)
     *                 .locations("classpath:db/migration")
     *                 .load();
     *         flyway.baseline();
     *         flyway.migrate();
     *     }
     * }
     * </pre>
     *
     * <p>Если опция {@code enabled} установлена в {@code true}, метод использует библиотеку Flyway для
     * базовой и последующей миграции базы данных в соответствии с настройками.
     */
    @PostConstruct
    public void migrateDatabase() {
        if (enabled) {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .load();
            flyway.baseline();
            flyway.migrate();
        }
    }
}
