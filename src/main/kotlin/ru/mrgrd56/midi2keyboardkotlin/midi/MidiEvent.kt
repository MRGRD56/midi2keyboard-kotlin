package ru.mrgrd56.midi2keyboardkotlin.midi

import org.apache.commons.lang3.builder.ToStringBuilder

class MidiEvent<T : MidiEventType> private constructor(
    val type: T,
    val key: Byte,
    val value: Byte,
    val rawData: ByteArray
) {

    override fun toString(): String {
        return ToStringBuilder(this)
            .append(type)
            .append("key", key)
            .append("value", value)
            .toString()
    }

    companion object {
        fun <T : MidiEventType> parse(rawData: ByteArray, eventTypeProvider: MidiEventTypeProvider<T>): MidiEvent<T> {
            val eventType = eventTypeProvider.getByCode(rawData[0])
            val key = rawData[1]
            val value = rawData[2]
            return MidiEvent(eventType, key, value, rawData)
        }
    }
}
