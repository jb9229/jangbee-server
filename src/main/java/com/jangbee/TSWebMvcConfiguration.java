package com.jangbee;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jeong on 2016-03-16.
 */
@Configuration
public class TSWebMvcConfiguration extends WebMvcConfigurerAdapter {
    public static final String FILESYSTEM_PATH              =   "/filesystem/";
    public static final String FILESYSTEM_IMG_PATH          =   FILESYSTEM_PATH+"img/";
    public static final String FILESYSTEM_THUMBNAILS_PATH   =   FILESYSTEM_IMG_PATH+"thumbnails/";


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        registry.addResourceHandler(FILESYSTEM_PATH+"**")
////                .addResourceLocations(Application.FILESERVER)
////                .setCachePeriod(3600)
////                .resourceChain(true)
////                .addResolver(new PathResourceResolver());
//
//        super.addResourceHandlers(registry);
//    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/assets/**").addResourceLocations(staticResourceLocation); }
}
