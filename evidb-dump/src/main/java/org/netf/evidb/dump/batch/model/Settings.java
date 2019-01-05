package org.netf.evidb.dump.batch.model;

import java.util.List;

import org.netf.evidb.dump.batch.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(name = "sqlgen", value = { "classpath:sqlgen.yml", "classpath:config/sqlgen.yml", "file:./sqlgen.yml",
		"file:.config/sqlgen.yml" }, factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "settings")
public class Settings {
	private List<Item> items;
}
