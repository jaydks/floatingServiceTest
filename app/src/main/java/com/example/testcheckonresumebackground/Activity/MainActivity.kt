package com.example.testcheckonresumebackground.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testcheckonresumebackground.ReserveAlarmManager
import com.example.testcheckonresumebackground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun initClickListener(){
        binding.tvStart.setOnClickListener {
            ReserveAlarmManager().reserve(this, 1000 * 5)
            finish()
        }
    }

}