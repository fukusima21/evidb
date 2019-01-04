package org.netf.evidb.dump.batch.model;

import java.util.List;

import org.netf.evidb.dump.batch.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(value = "classpath:sqlgen.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "settings")
public class Settings {

	private List<ItemDto> items;
}
