package com.example.testcheckonresumebackground.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testcheckonresumebackground.R
import com.example.testcheckonresumebackground.ReserveAlarmManager
import com.example.testcheckonresumebackground.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()

    }

    private fun initClickListener(){
        binding.tvStart.setOnClickListener {

        }
    }
}