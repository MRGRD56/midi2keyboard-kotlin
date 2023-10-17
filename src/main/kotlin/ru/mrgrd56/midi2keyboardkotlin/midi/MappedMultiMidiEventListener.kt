package ru.mrgrd56.midi2keyboardkotlin.midi

class MultiMidiEventListener<T : MidiEventType> private constructor(
    private val handlerMap: Map<HandlerKey<T>, MutableList<MidiEventListener<T>>>
) : MidiEventListener<T> {

    override fun onEvent(event: MidiEvent<T>) {
        handlerMap[HandlerKey(event.type, event.key)]?.forEach { handler: MidiEventListener<T> ->
            handler.onEvent(event)
        }
    }

    class Builder<T : MidiEventType> {
        private val handlerMap: MutableMap<HandlerKey<T>, MutableList<MidiEventListener<T>>> = HashMap()
        fun onEvent(eventType: T, key: Int, handler: MidiEventListener<T>): Builder<T> {
            handlerMap.computeIfAbsent(HandlerKey(eventType, key.toByte())) { ArrayList() }.add(handler)
            return this
        }

        fun build(): MidiEventListener<T> {
            return MultiMidiEventListener(handlerMap)
        }
    }

    private data class HandlerKey<T : MidiEventType>(val eventType: T, val key: Byte)
    companion object {
        inline fun <T : MidiEventType> build(building: Builder<T>.() -> Unit): MidiEventListener<T> {
            val builder = Builder<T>()
            building(builder)
            return builder.build()
        }
    }
}
