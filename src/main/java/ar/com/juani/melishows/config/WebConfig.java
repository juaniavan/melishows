package ar.com.juani.melishows.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ar.com.juani.melishows.converter.StringToShowOrderByConverter;
import ar.com.juani.melishows.converter.StringToSortDirectionConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToShowOrderByConverter());
        registry.addConverter(new StringToSortDirectionConverter());
    }
}