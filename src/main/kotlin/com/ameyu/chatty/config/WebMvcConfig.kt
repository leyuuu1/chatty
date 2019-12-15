package com.ameyu.chatty.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter

@Configuration
open class WebMvcConfig{
    @Bean
    open fun serverEndpointExporter() = ServerEndpointExporter()
}