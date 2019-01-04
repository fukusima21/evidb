package org.netf.evidb.dump.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.netf.evidb.dump.batch")
@EnableBatchProcessing(modular = true)
public class DumpDB {

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(DumpDB.class);
		app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
		System.exit(SpringApplication.exit(app.run(args)));
	}

}
