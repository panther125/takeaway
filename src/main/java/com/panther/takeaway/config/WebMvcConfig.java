package com.panther.takeaway.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.panther.takeaway.common.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
@EnableKnife4j
public class WebMvcConfig extends WebMvcConfigurationSupport {

    // 静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/front/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * 扩展消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        MappingJackson2HttpMessageConverter jack = new MappingJackson2HttpMessageConverter();
        jack.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,jack);
    }

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(new ApiInfoBuilder()
                        .title("瑞吉外卖")
                        .description("接口文档")
                        // .termsOfServiceUrl("http://www.xx.com/")
                        //.contact(new Contact("knife", "https://knife.blog.csdn.net/", "xx@qq.com"))
                        .version("1.0")
                        .build())
                // 分组名称
                .groupName("all")
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.panther.takeaway.controller"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }
}
