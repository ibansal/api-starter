package com.companyname.projectname.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by ishan.bansal on 6/30/15.
 */
@Configuration
@PropertySource(value = {"classpath:payment.properties"})
public class ServerConfig {
    @Autowired
    Environment env;

}
