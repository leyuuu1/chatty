package com.ameyu.chatty.controller

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Controller
import javax.websocket.OnClose
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint


//Public chat room
//@ServerEndpoint("/chat")
//@Controller
open class  WebSocketEndpoint{
    companion object{
        const val ROOM = "public"
        var onlineCount:Int = 0
    }
    var session:Session? = null
    var redisTemplate: StringRedisTemplate? = null
    @OnOpen
    open fun onOpen(session:Session){
        this.session = session
        sendMessage("success")
        //return history
        redisTemplate!!.opsForList()
                .range(ROOM, -10, -1)
                ?.forEach(::sendMessage)
    }

    @OnClose
    fun onClose() {
    }

    @OnMessage
    fun receiveMessage(message: String?) {
        redisTemplate!!.opsForList().leftPush(ROOM,message)
        println("${session!!.id}:$message")
    }


    open fun sendMessage(msg:String){
        this.session!!.basicRemote.sendText(msg)
    }
}

//@ServerEndpoint("/chat/{room}")
//class ChatRoomEndpoint {
//    var session:Session? = null
//    @OnOpen
//    fun onOpen(session:Session,@PathParam("room") room: String?){
//        this.session = session
//        sendMessage("Now you're enter $room chat room")
//    }
//    @OnMessage
//    fun receiveMessage(message: String?, @PathParam("room") room: String?) { //. . .
//    }
//
//    fun sendMessage(msg:String){
//        this.session!!.basicRemote.sendText(msg)
//    }
//}