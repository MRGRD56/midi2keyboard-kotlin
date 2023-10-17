package ru.mrgrd56.midi2keyboardkotlin.midi

import java.util.concurrent.ConcurrentLinkedDeque
import javax.sound.midi.*

class MidiInputReceiver<T : MidiEventType>(
    deviceName: String,
    private val eventTypeProvider: MidiEventTypeProvider<T>
) {
    private lateinit var device: MidiDevice

    private val handlers = ConcurrentLinkedDeque<MidiEventListener<T>>()

    init {
        run {
            val midiInfos = MidiSystem.getMidiDeviceInfo()
            for (info in midiInfos) {
                if (info.name != deviceName) {
                    continue
                }

                val device = MidiSystem.getMidiDevice(info)

                if (device.maxTransmitters != 0) {
                    this.device = device
                    device.transmitter.receiver = this.InputReceiver(handlers)
                    return@run
                }
            }

            throw MidiUnavailableException("Midi device not found")
        }
    }

    fun listen(handler: MidiEventListener<T>) {
        try {
            if (!device.isOpen) {
                device.open()
            }

            handlers += handler
        } catch (e: MidiUnavailableException) {
            throw RuntimeException(e)
        }
    }

    private inner class InputReceiver(private val handlers: Collection<MidiEventListener<T>>) : Receiver {
        private var isClosed: Boolean = false

        override fun send(message: MidiMessage, timeStamp: Long) {
            if (isClosed) return

            val data = message.getMessage()
            if (data[0] != 0xF0.toByte()) {
                val event = MidiEvent.parse(data, eventTypeProvider)
                handlers.forEach { it.onEvent(event) }
            }
        }

        override fun close() {
            isClosed = true
        }
    }
}
