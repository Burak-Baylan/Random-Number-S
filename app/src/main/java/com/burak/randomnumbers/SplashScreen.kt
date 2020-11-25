package com.burak.randomnumbers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val timerThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    go()
                } finally {
                    go()
                }
            }
        }
        timerThread.start()
    }

    private fun go(){
        val intent = Intent(this@SplashScreen, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}