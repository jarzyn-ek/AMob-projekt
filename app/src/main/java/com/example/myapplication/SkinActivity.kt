package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.skin_types.*


class SkinActivity : AppCompatActivity() {

    var skinType = ""

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.skin_types)
        radioButton1.setOnClickListener {
            imageViewSkinType.setImageResource(R.drawable.s1)
            this.skinType = "st1"
        }
        radioButton2.setOnClickListener {
            imageViewSkinType.setImageResource(R.drawable.s2)
            this.skinType = "st2"
        }
        radioButton3.setOnClickListener {
            imageViewSkinType.setImageResource(R.drawable.s3)
            this.skinType = "st3"
        }
        radioButton4.setOnClickListener {
            imageViewSkinType.setImageResource(R.drawable.s4)
            this.skinType = "st4"
        }
        radioButton5.setOnClickListener {
            imageViewSkinType.setImageResource(R.drawable.s5)
            this.skinType = "st5"
        }
        radioButton6.setOnClickListener {
            imageViewSkinType.setImageResource(R.drawable.s6)
            this.skinType = "st6"
        }

        buttonStartBaking.setOnClickListener {
            val intent = Intent(this, LocalizationActivity::class.java)
            intent.putExtra("skinType", this.skinType)
            startActivity(intent)
        }
    }
}