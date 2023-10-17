package ru.mrgrd56.midi2keyboardkotlin.midi.minilab3

import ru.mrgrd56.midi2keyboardkotlin.midi.MidiEventTypeProvider

object Minilab3MidiEventTypeProvider : MidiEventTypeProvider<Minilab3MidiEventType> {
    override fun getByCode(code: Byte): Minilab3MidiEventType {
        return Minilab3MidiEventType.valueOf(code)
    }
}
