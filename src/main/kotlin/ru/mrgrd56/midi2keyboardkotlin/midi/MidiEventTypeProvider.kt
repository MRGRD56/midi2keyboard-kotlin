package ru.mrgrd56.midi2keyboardkotlin.midi

interface MidiEventTypeProvider<T : MidiEventType?> {
    fun getByCode(code: Byte): T
}
