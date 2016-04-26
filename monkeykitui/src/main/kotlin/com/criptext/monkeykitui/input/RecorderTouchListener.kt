package com.criptext.monkeykitui.input

import android.content.Context
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.criptext.monkeykitui.input.listeners.AudioRecorder
import com.criptext.monkeykitui.input.listeners.RecordingListener

/**
 * Created by gesuwall on 4/25/16.
 */

class RecorderTouchListener : View.OnTouchListener{
    var blocked : Boolean = false
    var lastHit : Long = 0L
    var startTime : Long = 0L

    var startX : Float = 0F

    var recordingAnimations : RecorderAnimations? = null

    val maxLength = 80

    lateinit var dragger : ViewDragger

    fun vibrate(ctx: Context){
        val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(50)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.pointerCount > 1 || blocked)
            return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                startTime = System.nanoTime()
                startX = event.rawX


                dragger = ViewDragger(v, 100)
                vibrate(v.context)
                recordingAnimations?.revealRecorder()

            }
            MotionEvent.ACTION_UP -> {

                dragger.reset()
                lastHit = System.nanoTime()
                recordingAnimations?.hideRecorder(false)


            }
            MotionEvent.ACTION_MOVE -> {

                val reachedEnd = dragger.drag((startX - event.rawX).toInt())
                if (reachedEnd)
                    recordingAnimations?.hideRecorder(true)


            }
        }
        return true
    }
}