package com.ameyu.chatty.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler

@Configuration
@EnableWebSocket
open class WsConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatHandler(),"/chat")
    }

    @Bean
    open fun chatHandler():WebSocketHandler = ChatWebSocketHandler()
}

class ChatWebSocketHandler : TextWebSocketHandler() {
    companion object{
        const val ROOM = "public"
        var onlineCount:Int = 0
    }
    @Autowired
    var redisTemplate: StringRedisTemplate? = null
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        redisTemplate!!.opsForList()
                .range(ROOM, -10, -1)
                ?.forEach{
                    session?.sendMessage(TextMessage(it))
                }
        println("Opened new session in instance $this")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) { //组装返回的Echo信息
        redisTemplate!!.opsForList().rightPush(ROOM,message.payload)
        println(message.payload)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        session.close(CloseStatus.SERVER_ERROR)
        println("Info: WebSocket connection closed.")
    }
}

