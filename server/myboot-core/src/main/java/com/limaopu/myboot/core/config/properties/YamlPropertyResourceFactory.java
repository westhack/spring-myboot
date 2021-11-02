package com.limaopu.myboot.core.config.properties;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *  YAML配置文件读取工厂类
 * </pre>
 * <p>
 * @author mac
 */
public class YamlPropertyResourceFactory implements PropertySourceFactory {

    /**
     * Create a {@link PropertySource} that wraps the given resource.
     *
     * @param name     the name of the property source
     * @param resource the resource (potentially encoded) to wrap
     * @return the new {@link PropertySource} (never {@code null})
     * @throws IOException if resource resolution failed
     */
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        String resourceName = Optional.ofNullable(name).orElse(resource.getResource().getFilename());
        if (resourceName.endsWith(".yml") || resourceName.endsWith(".yaml")) {
            List<PropertySource<?>> yamlSources = new YamlPropertySourceLoader().load(resourceName, resource.getResource());
            return yamlSources.get(0);
        } else {
            return new DefaultPropertySourceFactory().createPropertySource(name, resource);
        }
    }
}
