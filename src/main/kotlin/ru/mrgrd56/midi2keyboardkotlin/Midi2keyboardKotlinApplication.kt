package ru.mrgrd56.midi2keyboardkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class Midi2keyboardKotlinApplication {
    companion object {
        init {
            System.setProperty("java.awt.headless", "false")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Midi2keyboardKotlinApplication>(*args)
}