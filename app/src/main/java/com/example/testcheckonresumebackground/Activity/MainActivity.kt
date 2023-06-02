package com.example.testcheckonresumebackground.Activity

import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testcheckonresumebackground.R
import com.example.testcheckonresumebackground.databinding.ActivityMainBinding
import com.example.testcheckonresumebackground.databinding.FloatingLayoutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var floatingbinding: FloatingLayoutBinding
    private lateinit var floatingView: View


    private var windowManager: WindowManager? = null
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        floatingbinding = FloatingLayoutBinding.inflate(layoutInflater)
        floatingView = floatingbinding.root

        initClickListener()

    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "onResume")


    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LifeCycle", "onRestart")

    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle", "onStop")

    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "onPause")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle", "onDestroy")
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }

    private fun initClickListener() {
        binding.tvStart.setOnClickListener {
            //    android:background="@android:color/transparent"
            binding.root.visibility = View.GONE

            val LAYOUT_FLAG: Int
            LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

            floatingView.setOnTouchListener(OnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return@OnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val offsetX: Int = (event.rawX - initialTouchX).toInt()
                        val offsetY: Int = (event.rawY - initialTouchY).toInt()
                        params.x = initialX + offsetX
                        params.y = initialY + offsetY
                        windowManager!!.updateViewLayout(floatingView, params)
                        return@OnTouchListener true
                    }
                }
                false
            })

            floatingbinding.btnClose.setOnClickListener {
                floatingbinding.root.visibility = View.GONE
                binding.root.visibility = View.VISIBLE
            }

            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            windowManager!!.addView(floatingView, params)
            //            val reservedUpdateIntent = Intent(this, DownloadUpdateService::class.java)
//            startService(reservedUpdateIntent)
//            finish()
        }
        binding.tvReserve.setOnClickListener {
//            ReserveAlarmManager().reserve(this, 1000 * 5)
//            finish()
        }
    }

}

