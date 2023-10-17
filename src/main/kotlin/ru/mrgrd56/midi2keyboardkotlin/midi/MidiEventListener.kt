package ru.mrgrd56.midi2keyboardkotlin.midi

import java.util.*

fun interface MidiEventListener<T : MidiEventType> : EventListener {
    fun onEvent(event: MidiEvent<T>)
}
