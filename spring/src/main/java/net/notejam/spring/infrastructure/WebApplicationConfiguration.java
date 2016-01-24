package net.notejam.spring.infrastructure;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.PropertySource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import net.notejam.spring.WebApplication;
import net.notejam.spring.infrastructure.converter.StringToPeriodConverter;

/**
 * Configures the Spring framework.
 *
 * @author markus@malkusch.de
 * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
 */
@EntityScan(basePackageClasses = { WebApplication.class, Jsr310JpaConverters.class })
@Configuration
class WebApplicationConfiguration {

    /**
     * Configures concurrency.
     *
     * @author markus@malkusch.de
     * @see <a href="bitcoin:1335STSwu9hST4vcMRppEPgENMHD2r1REK">Donations</a>
     */
    @EnableAsync
    @Configuration
    @EnableScheduling
    static class AsyncConfiguration {

	/**
	 * The queue capacity.
	 */
	@Value("${async.queueCapacity}")
	private int queueCapacity;

	/**
	 * The mail sending thread.
	 *
	 * @return The mail executor.
	 */
	@Bean
	Executor mailExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    executor.setCorePoolSize(1);
	    executor.setMaxPoolSize(1);
	    executor.setQueueCapacity(queueCapacity);
	    executor.setThreadPriority(Thread.MIN_PRIORITY);
	    executor.initialize();
	    return executor;
	}

    }

    /**
     * Provides the locale resolver.
     *
     * @return The locale resolver.
     */
    @Bean
    LocaleResolver localeResolver() {
	return new AcceptHeaderLocaleResolver();
    }

    /**
     * The conversion service.
     *
     * The conversion service helps to convert strings from e.g. a
     * {@link PropertySource} into other types.
     *
     * @return The conversion service.
     */
    @Bean
    ConversionService conversionService() {
	DefaultConversionService service = new DefaultConversionService();
	service.addConverter(new StringToPeriodConverter());
	return service;
    }

}
