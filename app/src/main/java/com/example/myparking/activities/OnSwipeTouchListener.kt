package com.example.myparking.activities

import android.content.Context
import android.text.method.Touch.onTouchEvent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

import kotlin.math.abs


abstract class OnSwipeTouchListener(val context: Context) : View.OnTouchListener {


    val gestureDetector: GestureDetector


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    abstract fun onSwipeRight()
    abstract fun onSwipeLeft()



    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }


        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.rawY - e1.rawY
                val diffX = e2.rawX - e1.rawX
                if (abs(diffX) - abs(diffY) > SWIPE_THRESHOLD) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else {
//                    if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
//                            onSwipeBottom()
//                        } else {
//                            onSwipeTop()
//                        }
//                        result = true
//                    }
                }
            } catch (e: Exception) {

            }

            return result
        }

    }



}