package ru.mrgrd56.midi2keyboardkotlin.midi.midi2keyboard

import java.awt.AWTException
import java.awt.Robot
import java.util.*

class Midi2KeyboardMidiEventListener : EventListener {
    private var robot: Robot? = null

    init {
        robot = try {
            Robot()
        } catch (e: AWTException) {
            throw RuntimeException(e)
        }
    }
}
