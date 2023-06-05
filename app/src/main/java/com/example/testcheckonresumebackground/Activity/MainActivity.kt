package com.example.testcheckonresumebackground.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testcheckonresumebackground.Constants
import com.example.testcheckonresumebackground.R
import com.example.testcheckonresumebackground.Service.DownloadUpdateService
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

    lateinit var tvPercent: TextView

    var percent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.tvPercent = findViewById(R.id.tv_percent)

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

//            //binding.root.visibility = View.GONE
//            binding.root.setBackgroundColor(Color.TRANSPARENT)
//
//            val LAYOUT_FLAG: Int
//            LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            } else {
//                WindowManager.LayoutParams.TYPE_PHONE
//            }
//
//            val params = WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                LAYOUT_FLAG,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT
//            )
//
//            floatingView.setOnTouchListener(OnTouchListener { v, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        initialX = params.x
//                        initialY = params.y
//                        initialTouchX = event.rawX
//                        initialTouchY = event.rawY
//                        return@OnTouchListener true
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        val offsetX: Int = (event.rawX - initialTouchX).toInt()
//                        val offsetY: Int = (event.rawY - initialTouchY).toInt()
//                        params.x = initialX + offsetX
//                        params.y = initialY + offsetY
//                        windowManager!!.updateViewLayout(floatingView, params)
//                        return@OnTouchListener true
//                    }
//                }
//                false
//            })
//
//            floatingbinding.btnClose.setOnClickListener {
//                windowManager!!.removeView(floatingView)
//
//                //floatingbinding.root.visibility = View.GONE
//                binding.root.visibility = View.VISIBLE
//            }
//
//            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//            floatingView.setBackgroundColor(Color.TRANSPARENT)
//            binding.root.visibility = View.INVISIBLE
//            windowManager!!.addView(floatingView, params)




            btnStart()

        }
        binding.tvReserve.setOnClickListener {
//            ReserveAlarmManager().reserve(this, 1000 * 5)
//            finish()

        }
    }

    private fun btnStart(){
        binding.tvStart.visibility = GONE
        binding.tvReserve.visibility = GONE
        binding.tvPercent.visibility = VISIBLE

        for (i in 0..100){
            val startUpdateIntent = Intent(this, DownloadUpdateService::class.java)
            startUpdateIntent.putExtra(Constants.ACTION_SEND_PERCENT, Integer.toString(i))
            startService(startUpdateIntent)

        }

        ProgressReceiver()
       // Log.d("퍼센트", ProgressReceiver().percent)

//        ACTION_UPDATE_PROGRESS
    }

    //progressPercent



}

class ProgressReceiver : BroadcastReceiver() {
    public var percent = ""

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Receiver", "ProgressReceiver onReceive:" + intent.action)
        if (intent.action == Constants.ACTION_UPDATE_PROGRESS) {
            Log.d("Receiver", "### ProgressReceiver")
           percent = intent.getStringExtra("progressPercent").toString()
            Log.d("percent", percent)


            MainActivity().findViewById<TextView>(R.id.tv_percent).text = percent

        }
    }
}

