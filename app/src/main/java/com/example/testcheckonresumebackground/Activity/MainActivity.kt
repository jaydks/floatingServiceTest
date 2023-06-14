package com.example.testcheckonresumebackground.Activity

import android.app.ActivityManager
import android.content.*
import android.content.Context.ACTIVITY_SERVICE
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.testcheckonresumebackground.Constants
import com.example.testcheckonresumebackground.PreferenceUtil
import com.example.testcheckonresumebackground.R
import com.example.testcheckonresumebackground.Service.DownloadUpdateService
import com.example.testcheckonresumebackground.databinding.ActivityMainBinding
import com.example.testcheckonresumebackground.databinding.FloatingLayoutBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var floatingbinding: FloatingLayoutBinding
    private lateinit var floatingView: View


    private var windowManager: WindowManager? = null
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    var percent = ""

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

            //binding.root.visibility = View.GONE
            //binding.root.setBackgroundColor(Color.TRANSPARENT)

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

            floatingView.setOnTouchListener(View.OnTouchListener { v, event ->
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
                    MotionEvent.ACTION_UP -> {
                        val touchX = event.rawX.toInt()
                        val touchY = event.rawY.toInt()
                        val viewRect = Rect()
                        floatingView.getGlobalVisibleRect(viewRect)
                        if (!viewRect.contains(touchX, touchY)) {
                            // 투명한 영역을 터치했을 때의 동작 처리
                            val targetView = findViewAtPosition(touchX, touchY)
                            targetView?.let {
                                // 다른 뷰에 터치 이벤트 전달
                                val offsetX: Int = touchX - it.left
                                val offsetY: Int = touchY - it.top
                                val motionEvent = MotionEvent.obtain(
                                    event.downTime,
                                    event.eventTime,
                                    event.action,
                                    offsetX.toFloat(),
                                    offsetY.toFloat(),
                                    event.metaState
                                )
                                it.dispatchTouchEvent(motionEvent)
                            }

                        }
                        return@OnTouchListener true

                    }
                }
                false
            })

            floatingbinding.btnClose.setOnClickListener {
                windowManager!!.removeView(floatingView)

                //floatingbinding.root.visibility = View.GONE
                binding.root.visibility = View.VISIBLE
            }

            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            floatingView.setBackgroundColor(Color.TRANSPARENT)
            binding.root.visibility = View.INVISIBLE
            windowManager!!.addView(floatingView, params)


//            btnStart()
        }
        binding.tvReserve.setOnClickListener {
//            ReserveAlarmManager().reserve(this, 1000 * 5)
//            finish()

        }
    }

    private fun btnStart() {
        binding.tvStart.visibility = GONE
        binding.tvReserve.visibility = GONE
        binding.tvPercent.visibility = VISIBLE

        for (i in 0..100) {
            val startUpdateIntent = Intent(this, DownloadUpdateService::class.java)
            startUpdateIntent.putExtra(Constants.ACTION_SEND_PERCENT, Integer.toString(i))
            startService(startUpdateIntent)

        }
        val progressReceiver = ProgressReceiver()

        progressReceiver.setProgressUpdateListener(object : ProgressUpdateListener {

            override fun onProgressUpdate(percent: String) {
                Log.d("퍼센트", percent)

                runOnUiThread {

                    binding.tvPercent.text = percent
                }
            }

        })
        registerReceiver(progressReceiver, IntentFilter(Constants.ACTION_UPDATE_PROGRESS))


//      Log.d("퍼센트", ProgressReceiver().percent)
//      ACTION_UPDATE_PROGRESS
    }

    //progressPercent
    private fun findViewAtPosition(x: Int, y: Int): View? {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        return rootView.findViewWithTag<View>("tag_for_intercept_touch_events")
    }

    private fun findViewAtPosition(rootView: View, x: Int, y: Int): View? {
        if (rootView is ViewGroup) {
            for (i in 0 until rootView.childCount) {
                val childView = rootView.getChildAt(i)
                val location = IntArray(2)
                childView.getLocationOnScreen(location)
                val left = location[0]
                val top = location[1]
                val right = left + childView.width
                val bottom = top + childView.height

                if (x >= left && x <= right && y >= top && y <= bottom) {
                    return childView
                }

                if (childView is ViewGroup) {
                    val foundView = findViewAtPosition(childView, x, y)
                    if (foundView != null) {
                        return foundView
                    }
                }
            }
        }

        return null
    }

}

class ProgressReceiver : BroadcastReceiver() {
    private var progressUpdateListener: ProgressUpdateListener? = null

    fun setProgressUpdateListener(listener: ProgressUpdateListener) {
        progressUpdateListener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Receiver", "ProgressReceiver onReceive:" + intent.action)
        if (intent.action == Constants.ACTION_UPDATE_PROGRESS) {
            Log.d("Receiver", "### ProgressReceiver")
            val percent = intent.getStringExtra("progressPercent").toString()
            progressUpdateListener?.onProgressUpdate(percent)
            Log.d("percent", percent)
        }
    }
}

interface ProgressUpdateListener {
    fun onProgressUpdate(percent: String)
}

