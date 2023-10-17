package ru.mrgrd56.midi2keyboardkotlin

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import ru.mrgrd56.midi2keyboardkotlin.midi.MidiInputReceiver
import ru.mrgrd56.midi2keyboardkotlin.midi.midi2keyboard.Midi2KeyboardMidiEventListener
import ru.mrgrd56.midi2keyboardkotlin.midi.minilab3.Minilab3MidiEventType
import ru.mrgrd56.midi2keyboardkotlin.midi.minilab3.Minilab3MidiEventTypeProvider
import java.awt.MouseInfo
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent


@Component
class AppMidiListener {
    @PostConstruct
    @Async
    fun start() {
        val inputReceiver = MidiInputReceiver("Minilab3 MIDI", Minilab3MidiEventTypeProvider)
        inputReceiver.listen(
            Midi2KeyboardMidiEventListener.build {
                setupKeys(Minilab3MidiEventType.KEY_DOWN, Minilab3MidiEventType.KEY_UP)
                bindKey(54, KeyEvent.VK_ESCAPE)
                bindKey(51, KeyEvent.VK_CONTROL)
                bindKey(52, KeyEvent.VK_SHIFT)
                bindKey(53, KeyEvent.VK_A)
                bindKey(55, KeyEvent.VK_S)
                bindKey(56, KeyEvent.VK_W)
                bindKey(57, KeyEvent.VK_D)
                bindKey(58, KeyEvent.VK_E)
                bindKey(59, KeyEvent.VK_SPACE)

                setupKeys(Minilab3MidiEventType.PAD_DOWN, Minilab3MidiEventType.PAD_UP)
                bindKeyToMouseButton(36, MouseEvent.BUTTON3_DOWN_MASK)
                bindKey(37, KeyEvent.VK_1)
                bindKey(38, KeyEvent.VK_2)
                bindKey(39, KeyEvent.VK_3)
                bindKey(40, KeyEvent.VK_4)
                bindKey(41, KeyEvent.VK_5)
                bindKey(42, KeyEvent.VK_6)
                bindKey(43, KeyEvent.VK_7)

                bind(Minilab3MidiEventType.KNOB, 118) { event, robot ->
                    when (event.value.toInt()) {
                        127 -> robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK)
                        0 -> robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK)
                    }
                }

                setupKnobs(Minilab3MidiEventType.KNOB)
                bindKnob(110) { event, delta, robot ->
                    val cursorPosition: Point = MouseInfo.getPointerInfo().location

                    robot.mouseMove(cursorPosition.x, cursorPosition.y + (delta * 15))
                }
                bindKnob(111) { event, delta, robot ->
                    robot.mouseWheel(delta)
                }

                bind(Minilab3MidiEventType.KNOB, 28) { event, robot ->
                    val cursorPosition: Point = MouseInfo.getPointerInfo().location

                    println("cursorPosition before move: $cursorPosition")

                    val mouseDelta: Int = when (event.value.toInt()) {
                        61 -> -40
                        62 -> -15
                        65 -> 15
                        66 -> 40
                        else -> throw IllegalArgumentException("invalid value ${event.value}")
                    }

                    robot.mouseMove(cursorPosition.x + mouseDelta, cursorPosition.y)
                }
            }
        )

        inputReceiver.listen { println(it) }
    }
}