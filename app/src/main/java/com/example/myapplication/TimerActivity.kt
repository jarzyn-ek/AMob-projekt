package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.localization_view.*
import kotlinx.android.synthetic.main.skin_types.*

class TimerActivity : AppCompatActivity() {

    lateinit var time : String

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer)
        this.time = getIntent().getStringExtra("skinType");
        textView.text = time
    }
}