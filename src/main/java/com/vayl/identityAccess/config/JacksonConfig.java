//package com.vayl.identityAccess.config;
//
//import tools.jackson.databind.module.SimpleModule;
//import com.vayl.identityAccess.core.domain.common.inputtableValue.InputtableValue;
//import com.vayl.identityAccess.core.controller.InputtableValueDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    public SimpleModule inputtableValueModule() {
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(InputtableValue.class, new InputtableValueDeserializer());
//        return module;
//    }
//}
//
