package ru.mrgrd56.midi2keyboardkotlin.midi

import javax.sound.midi.*

class MidiInputReceiver<T : MidiEventType?>(
    deviceName: String,
    private val eventTypeProvider: MidiEventTypeProvider<T>
) {
    private val device: MidiDevice? = null

    init {
        try {
            val midiInfos = MidiSystem.getMidiDeviceInfo()
            for (info in midiInfos) {
                if (info.name != deviceName) {
                    continue
                }
                val device = MidiSystem.getMidiDevice(info)
                if (device.maxTransmitters != 0) {
                    this.device = device
                    return
                }
            }
            throw RuntimeException(MidiUnavailableException("Midi device not found"))
        } catch (e: MidiUnavailableException) {
            throw RuntimeException(e)
        }
    }

    fun listen(eventListener: MidiEventListener<T>): Receiver {
        return try {
            if (!device!!.isOpen) {
                device.open()
            }
            val receiver: Receiver = this.InputReceiver(eventListener)
            device.transmitter.receiver = receiver
            receiver
        } catch (e: MidiUnavailableException) {
            throw RuntimeException(e)
        }
    }

    private inner class InputReceiver(private val eventListener: MidiEventListener<T>) : Receiver {
        override fun send(message: MidiMessage, timeStamp: Long) {
            val data = message.getMessage()
            if (data[0] != 0xF0.toByte()) {
                val event = MidiEvent.parse(data, eventTypeProvider)
                eventListener.onEvent(event)
            }
        }

        override fun close() {
            println("Closed")
        }
    }
}
