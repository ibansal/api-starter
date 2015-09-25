package com.companyname.projectname.config;

import com.companyname.projectname.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

//import AuthenticationInterceptor;

@EnableWebMvc
@Configuration
@Import(value = {DataConfig.class, SchedulerConfig.class})
@ComponentScan(basePackages = "com.companyname.projectname")
@PropertySource(value = {"classpath:server.properties"})
class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Resource
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor);
    }

    /**
     * Without this @Value will not work
     * {@link "http://www.baeldung.com/2012/02/06/properties-with-spring/"}
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}