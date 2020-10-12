package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.animation.*


class AnimationActivity : AppCompatActivity() {

    var clicks = 0


   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.animation)

        imageView.setOnClickListener{
            if (clicks == 0) {
                imageView.animate().apply {
                    duration = 1000
                    alpha(.5f)
                    scaleXBy(.5f)
                    scaleYBy(.5f)
                    rotationYBy(360f)
                    translationYBy(200f)
                }.withEndAction {
                    imageView.animate().apply {
                        duration = 1000
                        alpha(1f)
                        scaleXBy(-.5f)
                        scaleYBy(-.5f)
                        rotationXBy(360f)
                        translationYBy(-200f)
                    }.start()
                }
                clicks += 1
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            }

   }

}