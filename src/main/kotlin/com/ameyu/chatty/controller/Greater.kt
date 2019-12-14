package com.ameyu.chatty.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
open class Greater{
    @Autowired
    var redisTemplate: StringRedisTemplate? = null
    @RequestMapping("/count")
    open fun countIn():String{
        val operations = redisTemplate!!.opsForValue()
        val num = operations.increment("count")
        //Return visitor number
        return "You are $num arrive here!"
    }

}