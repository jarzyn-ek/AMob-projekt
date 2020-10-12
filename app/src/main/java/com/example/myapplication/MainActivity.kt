package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val font_style = Typeface.createFromAsset(assets, "vacaciones.ttf")
        button2.setTypeface(font_style)
        button2.setOnClickListener{
        val intent = Intent(this, SkinActivity::class.java)
        startActivity(intent)
        }
    }
}