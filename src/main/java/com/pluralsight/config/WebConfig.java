package com.pluralsight.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.*;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    HandlerInterceptor yourInjectedInterceptor;

    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        mediaTypes.put("xml", MediaType.APPLICATION_XML);

        List<View> defaultViews = new ArrayList<>();
        defaultViews.add(new MappingJackson2JsonView());
        defaultViews.add(new MarshallingView());

        ContentNegotiationManager manager = new ContentNegotiationManager(new PathExtensionContentNegotiationStrategy(mediaTypes));

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setOrder(1);
        resolver.setContentNegotiationManager(manager);
        resolver.setDefaultViews(defaultViews);
        return resolver;
    }

    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
        messages.setBasename("messages");
        return messages;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
