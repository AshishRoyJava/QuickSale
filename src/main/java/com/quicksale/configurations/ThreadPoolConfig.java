package com.quicksale.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class for thread pool. The core pool size and max pool size can
 * be configured depending on the server.
 * 
 * @author ashishr
 *
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

	@Bean
	public TaskExecutor threadPoolTaskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(4);
		executor.setThreadNamePrefix("QuickSaleOperationThread");
		executor.initialize();

		return executor;
	}
}
