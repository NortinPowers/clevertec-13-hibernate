package by.clevertec.house.config;

import java.util.Objects;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    /**
     * Переопределенный метод для создания источника свойств из YAML-файла.
     * Создает объект {@link PropertySource}, используя имя и ресурс YAML-файла.
     *
     * @param name     Имя источника свойств.
     * @param resource Ресурс YAML-файла.
     * @return Объект {@link PropertySource}, представляющий свойства из YAML-файла.
     * @throws NullPointerException Если объект, возвращаемый фабрикой свойств YAML, равен {@code null}.
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new ClassPathResource("application.yml"));
        return new PropertiesPropertySource("custom", Objects.requireNonNull(factory.getObject()));
    }
}
