package ru.mrgrd56.midi2keyboardkotlin.midi.minilab3

import ru.mrgrd56.midi2keyboard.midi.MidiEventTypeProvider

class Minilab3MidiEventTypeProvider : MidiEventTypeProvider<Minilab3MidiEventType?> {
    fun getByCode(code: Byte): Minilab3MidiEventType {
        return Minilab3MidiEventType.valueOf(code)
    }
}
