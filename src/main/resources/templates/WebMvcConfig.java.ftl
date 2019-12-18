package ${package.config};

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.charset.Charset;

/**
* 全局配置
*
* @author ${author} on ${date}
*/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        /*是否允许请求带有验证信息*/
        corsConfiguration.setAllowCredentials(true);
        /*允许访问的客户端域名*/
        corsConfiguration.addAllowedOrigin("*");
        /*允许服务端访问的客户端请求头*/
        corsConfiguration.addAllowedHeader("*");
        /*允许访问的方法名,GET POST等*/
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    /**
    * 使用fastJson代替Jackjson解析JSON数据
    *
    * @return
    */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        /*
        * 转换为JSON字符串，默认:
        * WriteNullListAsEmpty    List字段如果为null,输出为[],而非null
        * WriteNullStringAsEmpty  字符类型字段如果为null,输出为”“,而非null
        * WriteMapNullValue       是否输出值为null的字段,默认为false
        */
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        fastConverter.setDefaultCharset(Charset.forName("UTF-8"));
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

    /**
    * 资源映射
    * @param registry
    */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }

}