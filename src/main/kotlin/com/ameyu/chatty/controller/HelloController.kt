package com.ameyu.chatty.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
//TODO change to websocket controller
@RestController
open class HelloController{
    @RequestMapping("")
    open fun home():String{
        return "index"
    }
}