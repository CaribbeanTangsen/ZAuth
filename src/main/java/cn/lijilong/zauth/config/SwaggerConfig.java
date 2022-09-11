package cn.lijilong.zauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder()
                    .title("ZAuth")
                    .contact(new Contact("lijilong", "#", "13792139613@qq.com"))
                    .version("v0.1.1")
                    .build())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/error").negate())
//                .paths(PathSelectors.regex("/api/.*"))
                .paths(PathSelectors.any())
                .build();
    }
}
