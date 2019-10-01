package uk.yapily.facts.swagger;

import com.google.common.base.Predicates;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class MySwaggerConfiguration {
    private static final String API_DESCRIPTION_FILENAME = "api-doc/description.md";

    @Bean
    public Docket demoApi() throws IOException {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

        docket.apiInfo(getApiInfo());
        return docket;
    }

    private String getApiDescriptionFromFile() throws IOException {
        try (InputStream descriptionIs = this.getClass()
                .getClassLoader()
                .getResourceAsStream(API_DESCRIPTION_FILENAME)) {
            if (descriptionIs == null) {
                return null;
            }

            return IOUtils.toString(descriptionIs, Charset.forName("UTF-8").toString());
        }
    }

    private ApiInfo getApiInfo() throws IOException {
        return new ApiInfo(
                "Facts API",
                getApiDescriptionFromFile(),
                "1.0",
                null,
                new Contact("Boboc Costin Gabriel", null, "boboc.g21@gmail.com"),
                null, null, Collections.emptyList());
    }


}
