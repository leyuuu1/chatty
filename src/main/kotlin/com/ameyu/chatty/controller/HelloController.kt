package com.ameyu.chatty.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("hello")
open class HelloController{
    @RequestMapping("/")
    open fun home():String{
        return "OK body"
    }
}