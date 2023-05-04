package by.pavel.imageannotationbe.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.storage")
public class LocalStorageConfigurationProperties {

    private String uploadDefaultPath;
    private String uploadPreviewPath;

}
