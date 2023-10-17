package ru.mrgrd56.midi2keyboardkotlin.midi

fun interface MidiEventListener<T : MidiEventType?> {
    fun onEvent(event: MidiEvent<T>?)
}
