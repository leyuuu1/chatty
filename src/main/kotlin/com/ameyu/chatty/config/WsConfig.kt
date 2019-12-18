package com.ameyu.chatty.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Configuration
@EnableWebSocket
open class WsConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatHandler(),"/chat")
        registry.addHandler(customHandler(),"/room/**").addInterceptors(actionInterceptor())
    }

    @Bean
    open fun chatHandler():WebSocketHandler = ChatWebSocketHandler()

    @Bean
    open fun customHandler():WebSocketHandler = CustomRoomHandler()

    @Bean
    open fun actionInterceptor(): HandshakeInterceptor {
        return object:HandshakeInterceptor{
            override fun afterHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, exception: Exception?) {
                //Nothing
            }

            override fun beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: MutableMap<String, Any>): Boolean {
                val path = request.uri.path
                val ch = path.substring(path.lastIndexOf('/') + 1)
                attributes["ch"] = ch
                return true
            }

        }
    }
}

open class ChatWebSocketHandler : TextWebSocketHandler() {
    companion object{
        const val ROOM = "public"
        var pool:ArrayList<WebSocketSession> = arrayListOf()
    }
    @Autowired
    var redisTemplate: StringRedisTemplate? = null
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        pool.add(session)
        redisTemplate!!.opsForList()
                .range(ROOM, -10, -1)
                ?.forEach{
                    session.sendMessage(TextMessage(it))
                }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        pool.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        redisTemplate!!.opsForList().rightPush(ROOM,message.payload)
        transmit(session,message.payload)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        session.close(CloseStatus.SERVER_ERROR)
        println("Info: WebSocket connection closed.")
    }

    fun transmit(submitter:WebSocketSession,msg:String){
        pool.filter { it!=submitter }.forEach{
            it.sendMessage(TextMessage(msg))
        }
    }
}


open class CustomRoomHandler : TextWebSocketHandler() {
    companion object{
        var pool = mutableMapOf<String,ArrayList<WebSocketSession>>()
    }
    @Autowired
    var redisTemplate: StringRedisTemplate? = null
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val room = session.attributes["ch"] as String
        if(room !in pool.keys){
            pool[room] = arrayListOf()
        }
        pool[room]?.add(session)
        redisTemplate!!.opsForList()
                .range(room, -10, -1)
                ?.forEach{
                    session.sendMessage(TextMessage(it))
                }

    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val room = session.attributes["ch"] as String
        pool[room]?.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val room = session.attributes["ch"] as String
        redisTemplate!!.opsForList().rightPush(room,message.payload)
        transmit(session,room,message.payload)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        session.close(CloseStatus.SERVER_ERROR)
        println("Info: WebSocket connection closed.")
    }

    fun transmit(submitter:WebSocketSession,room:String,msg:String){
        pool[room]?.filter { it!=submitter }?.forEach{
            it.sendMessage(TextMessage(msg))
        }
    }
}
