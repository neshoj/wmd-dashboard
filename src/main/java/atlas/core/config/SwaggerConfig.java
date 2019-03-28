package atlas.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @category    Swagger
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig  {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nimbus.api.controllers"))
//                .apis( RequestHandlerSelectors.any() )
                .paths( PathSelectors.any() )
//                .apis(RequestHandlerSelectors.basePackage("com.nimbus.api.controllers"))
//                .paths(PathSelectors.ant("/api/*"))
                .build()
                .apiInfo( apiInfo() )
        ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Nimbus",
                "POS REST API.",
                "1.0.0",
                "Terms of service",
                new Contact("Anthony Mwawughanga", "http://nimbus-sandbox.binary.co.ke/", "amwawughanga@binary.co.ke"),
                "Proprietary License", "", Collections.emptyList());
    }

//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

}
