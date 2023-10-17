package ru.mrgrd56.midi2keyboardkotlin.midi.minilab3

import dev.b37.mgutils.CollectionUtils
import java.util.*

enum class Minilab3MidiEventType(code: Int) : MidiEventType {
    KEY_DOWN(0x90),
    KEY_UP(0x80),
    PAD_DOWN(0x99),
    PAD_UP(0x89),
    KNOB(0xB0),
    PITCH(0xE0);

    val code: Byte

    init {
        this.code = code.toByte()
    }

    companion object {
        private val membersMap: Map<Byte, Minilab3MidiEventType> =
            CollectionUtils.mapByKey(Arrays.asList(*values())) { obj: Minilab3MidiEventType -> obj.code }

        fun valueOf(code: Byte): Minilab3MidiEventType {
            return membersMap[code]
                ?: throw IllegalArgumentException("Midi event type not found")
        }
    }
}