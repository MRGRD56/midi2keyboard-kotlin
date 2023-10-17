package ru.mrgrd56.midi2keyboardkotlin.midi.minilab3

import ru.mrgrd56.midi2keyboardkotlin.midi.MidiEventType
import java.util.*

enum class Minilab3MidiEventType(code: Int) : MidiEventType {
    KEY_DOWN(0x90),
    KEY_UP(0x80),
    PAD_DOWN(0x99),
    PAD_UP(0x89),
    KNOB(0xB0),
    PITCH(0xE0);

    private val _code: Int

    init {
        this._code = code
    }

    override val code: Byte
        get() = _code.toByte()

    companion object {
        private val membersMap: Map<Byte, Minilab3MidiEventType> = values().associateBy { it.code }

        fun valueOf(code: Byte): Minilab3MidiEventType {
            return membersMap[code]
                ?: throw IllegalArgumentException("Midi event type not found")
        }
    }
}