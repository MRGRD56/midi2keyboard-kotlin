package ru.mrgrd56.midi2keyboardkotlin.midi.midi2keyboard

import ru.mrgrd56.midi2keyboardkotlin.midi.MappedMultiMidiEventListener
import ru.mrgrd56.midi2keyboardkotlin.midi.MidiEvent
import ru.mrgrd56.midi2keyboardkotlin.midi.MidiEventListener
import ru.mrgrd56.midi2keyboardkotlin.midi.MidiEventType
import java.awt.Robot
import java.util.concurrent.ConcurrentHashMap

object Midi2KeyboardMidiEventListener {
    private var robot: Robot = Robot()

    inline fun <T : MidiEventType> build(building: Builder<T>.() -> Unit): MidiEventListener<T> {
        val builder = Builder<T>()
        building(builder)
        return builder.build()
    }

    class Builder<T : MidiEventType> {
        private val multiListenerBuilder: MappedMultiMidiEventListener.Builder<T> =
            MappedMultiMidiEventListener.Builder()

        private lateinit var keyDownEvent: T
        private lateinit var keyUpEvent: T
        private lateinit var knobEvent: T

        private val knobStates = ConcurrentHashMap<Int, Int>()

        fun setupKeys(keyDownEvent: T, keyUpEvent: T) {
            this.keyDownEvent = keyDownEvent
            this.keyUpEvent = keyUpEvent
        }

        fun setupKnobs(knowEvent: T) {
            this.knobEvent = knowEvent
        }

        fun bind(eventType: T, midiKey: Int, handler: (event: MidiEvent<T>, robot: Robot) -> Unit) {
            multiListenerBuilder.onEvent(eventType, midiKey) { handler(it, robot) };
        }

        fun bindKnob(midiKey: Int, handler: (event: MidiEvent<T>, delta: Int, robot: Robot) -> Unit) {
            multiListenerBuilder.onEvent(knobEvent, midiKey) { event ->
                val previousState = knobStates[midiKey]
                val currentState = event.value.toInt()
                knobStates[midiKey] = currentState

                if (previousState == null) {
                    return@onEvent
                }

                val delta = if (previousState == currentState) {
                    when (currentState) {
                        0 -> -1
                        127 -> 1
                        else -> throw IllegalArgumentException("invalid state $currentState")
                    }
                } else {
                    currentState - previousState
                }

                handler(event, delta, robot)
            }
        }

        fun bindKey(midiKey: Int, osKey: Int): Builder<T> {
            multiListenerBuilder.onEvent(keyDownEvent, midiKey) { robot.keyPress(osKey) }
            multiListenerBuilder.onEvent(keyUpEvent, midiKey) { robot.keyRelease(osKey) }
            return this
        }

        fun bindKeyToMouseButton(midiKey: Int, osMouseButton: Int): Builder<T> {
            multiListenerBuilder.onEvent(keyDownEvent, midiKey) { robot.mousePress(osMouseButton) }
            multiListenerBuilder.onEvent(keyUpEvent, midiKey) { robot.mouseRelease(osMouseButton) }
            return this
        }

        fun build(): MidiEventListener<T> {
            return multiListenerBuilder.build()
        }
    }
}
