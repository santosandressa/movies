package com.cinema.plus.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tmdb.api")
@Data
public class TmdbProperties {
    private String baseUrl;
    private String apiKey;
    private String language;
    private String imageBaseUrl;
}