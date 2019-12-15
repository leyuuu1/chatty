package com.ameyu.chatty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChattyApplication

fun main(args: Array<String>) {
    runApplication<ChattyApplication>(*args)
}
