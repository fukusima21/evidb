package org.netf.evidb.dump.batch.factory;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

		if (resource != null && resource.getResource().exists()) {

			List<PropertySource<?>> propertySourceList = new YamlPropertySourceLoader()
					.load(resource.getResource().getFilename(), resource.getResource());

			if (!propertySourceList.isEmpty()) {
				return propertySourceList.iterator().next();
			}

		}

		return super.createPropertySource(name, resource);

	}
}
