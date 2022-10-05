package com.app.configuration;

import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public RequestOptions requestOptions(@Value("${stripe_api_key}") String apiKey) {
        return RequestOptions.builder()
                .setApiKey(apiKey)
                .build();
    }

}
