package com.elgupo;

import com.elgupo.elguposerver.authentication.routes.AuthenticationController;
import com.elgupo.elguposerver.authentication.services.AuthenticationService;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.elgupo.elguposerver.database.repositories")
@EntityScan(basePackages = "com.elgupo.elguposerver.database.models")
@ComponentScan(basePackages = {
    "com.elgupo.elguposerver.authentication.services",
    "com.elgupo.elguposerver.authentication.routes",
    "com.elgupo.elguposerver.database.repositories",
        "com.elgupo.elguposerver.database.routes",
        "com.elgupo.elguposerver.database.services"
})
public class TestConfig {

} 