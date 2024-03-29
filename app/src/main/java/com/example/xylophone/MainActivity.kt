package com.example.xylophone

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(8).build()
    } else {
        SoundPool(8,AudioManager.STREAM_MUSIC,0)
    }

    private val sounds = listOf(
        Pair(R.id.do1,R.raw.do1),
        Pair(R.id.re,R.raw.re),
        Pair(R.id.mi,R.raw.mi),
        Pair(R.id.fa,R.raw.fa),
        Pair(R.id.sol,R.raw.sol),
        Pair(R.id.ra,R.raw.la),
        Pair(R.id.si,R.raw.si),
        Pair(R.id.do2,R.raw.do2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        //화면을 가로 모드로 고정시키기.. (Manifest에서도 설정 가능)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sounds.forEach{ tune(it)}
    }

    private fun tune(pitch: Pair<Int,Int>){
        val soundId = soundPool.load(this, pitch.second,1)
        findViewById<TextView>(pitch.first).setOnClickListener {
            soundPool.play(soundId, 1.0f,1.0f,0,0,1.0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
