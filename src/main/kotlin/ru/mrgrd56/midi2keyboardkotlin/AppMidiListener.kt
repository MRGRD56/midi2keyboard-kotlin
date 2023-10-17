package ru.mrgrd56.midi2keyboardkotlin.midi

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AppMidiListener {
    @PostConstruct
    @Async
    fun start() {

    }
}