package org.netf.evidb.dump.batch.model;

import org.netf.evidb.dump.batch.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(name = "dump", value = { "classpath:dump.yml", "classpath:config/dump.yml", "file:./dump.yml",
		"file:.config/dump.yml" }, factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "dump")
public class Dump {

	private String outputDir;
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private String dialect;
}
