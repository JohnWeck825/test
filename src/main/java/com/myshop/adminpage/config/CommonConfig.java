//package com.myshop.adminpage.config;
//
//import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
//import org.thymeleaf.templatemode.TemplateMode;
//import org.thymeleaf.templateresolver.*;
//import org.thymeleaf.*;
//
//
//@Configuration
//public class CommonConfig {
//
////    @Bean
////    public SpringTemplateEngine templateEngine() {
////        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
////        templateEngine.setEnableSpringELCompiler(true);
////        templateEngine.addDialect(new LayoutDialect());
////        return templateEngine;
////    }
////
////    @Bean
////    public ITemplateResolver templateResolver() {
////        FileTemplateResolver resolver = new FileTemplateResolver();
////        resolver.setPrefix("classpath:/templates/"); // Xác định vị trí thư mục templates
////        resolver.setSuffix(".html"); // Định dạng file template
////        resolver.setTemplateMode(TemplateMode.HTML); // Chế độ xử lý template
////        return resolver;
////    }
////
////    @Bean
////    public SpringResourceTemplateResolver resourceTemplateResolver() {
////        return new SpringResourceTemplateResolver();
////    }
////
////    @Bean
////    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
////        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
////        templateEngine.setTemplateResolver(templateResolver); // Gán TemplateResolver cho TemplateEngine
////        templateEngine.setEnableSpringELCompiler(true);
////        templateEngine.addDialect(new LayoutDialect());
////        return templateEngine;
////    }
//}
