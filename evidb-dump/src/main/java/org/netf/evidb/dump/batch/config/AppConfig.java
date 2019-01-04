package org.netf.evidb.dump.batch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public ApplicationContextFactory sampleJobs() {
		return new GenericApplicationContextFactory(ProcessJobConfiguration.class);
	}

	@Bean
	public DefaultBatchConfigurer defaultBatchConfigurer() {

		return new DefaultBatchConfigurer() {

			MapJobRepositoryFactoryBean jobRepositoryFactory;

			@Override
			protected JobRepository createJobRepository() throws Exception {
				jobRepositoryFactory = new MapJobRepositoryFactoryBean(getTransactionManager());
				jobRepositoryFactory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
				jobRepositoryFactory.afterPropertiesSet();
				return jobRepositoryFactory.getObject();
			}

			@Override
			protected JobExplorer createJobExplorer() throws Exception {
				MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory);
				jobExplorerFactory.afterPropertiesSet();
				return jobExplorerFactory.getObject();
			}

		};

	}

}
