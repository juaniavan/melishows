package ar.com.juani.melishows.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	// @formatter:off
	public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Melishows API",
            "Show API for the following services&#58;\n" +
            "      - filtering shows through /shows \n" +
            "      - filtering showing or events through /showings \n" +
            "      - get some showing or event detail through /showings/{id} \n" +
            "      - reserve seats for a showing /reserve \n" +
            "      - reset database (Testing) /reset \n" ,
            "V1.0",
            null,
            new Contact("Juani", "", "ignacio.avantario@gmail.com"),
            null,
            null,
            new ArrayList<>());
	
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
          .useDefaultResponseMessages(false)
          .apiInfo(DEFAULT_API_INFO)
          .select()
          .apis(RequestHandlerSelectors.basePackage("ar.com.juani"))
          .paths(PathSelectors.any())
          .build();                                           
    }
	
	// @formatter:on
}
