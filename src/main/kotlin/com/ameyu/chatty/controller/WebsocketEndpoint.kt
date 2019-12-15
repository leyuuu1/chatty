package com.ameyu.chatty.controller

import org.springframework.stereotype.Controller
import javax.websocket.OnClose
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint


@Controller
@ServerEndpoint("/chat")
class  WebsocketEndpoint{
    companion object{
        var onlineCount:Int = 0
    }
    var session:Session? = null

    @OnOpen
    fun onOpen(session:Session){
        this.session = session
        onlineCount++
        print("Someone online")
        sendMessage("有新窗口开始监听")
    }

    @OnClose
    fun onClose() {
        onlineCount--
        print("Someone offline")
    }

    fun sendMessage(msg:String){
        this.session!!.getBasicRemote().sendText(msg)
    }
}