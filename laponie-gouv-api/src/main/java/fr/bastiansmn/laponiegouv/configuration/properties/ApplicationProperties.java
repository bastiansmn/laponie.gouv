package fr.bastiansmn.laponiegouv.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(value = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    // Frontend URL
    private String url;

}
