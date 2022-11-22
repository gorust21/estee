package com.gaia.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.gaia.util.MyDateUtil.*;

/*Springboot拦截器未起作用
 * 之前遇到要使用springboot拦截器却始终未生效的状况，查了网上的博客，大抵都是@Component,@Configuration注解未加，或是使用@ComponentScan增加包扫描，
 * 但是尝试后都没有生效，最后才发现是因为之前为了解决跨域问题配置类继承了WebMvcConfigurationSupport，并重写了里面的方法。之后的拦截器配置类同样继承
 * 了这个这个类并重写方法，它只会生效前一个配置类，后一个配置类不会生效，所以解决方法就是在一个配置类重写这两个方法就行了，不要分成两个配置类写。
 */

@Configuration
//@EnableSwagger2
@EnableOpenApi
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * ajax跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*");
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //这里：是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }

    /**
     * 拦截器
     *
     * @return
     */
    // @Bean
    // public AuthenticationInterceptor authenticationInterceptor() {
    //     return new AuthenticationInterceptor();
    // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry//.addInterceptor(authenticationInterceptor())
        //        .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
    }

    /**
     * Swagger
     * @return
     */
//    @Bean
//    public Docket productApi() {
//        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.basePackage("com.king.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(metaData());
//    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                // 标题
                .title("king")
                // 描述
                .description("king api")
                // 文档版本
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Swagger3接口文档")
                .description("社区交互软件接口文档")
                .contact(new Contact("Lawrence","https://xuyijie.icu/", "1119461672@qq.com"))
                .version("1.0")
                .build();
    }


    private Boolean swaggerEnabled=true;
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled)
                .select()
                // 扫描的路径使用@Api的controller
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any())
                .build();
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * 更改jackson默认配置
     */
    @Bean
    public ObjectMapper ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 对于空的对象转json的时候不抛出错误
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用遇到未知属性抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化BigDecimal时不使用科学计数法输出
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // 日期和时间格式化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(PATTERN_DATE_TIME));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(PATTERN_DATE));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(PATTERN_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(PATTERN_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(PATTERN_DATE));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(PATTERN_TIME));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }



}