package com.example.greenpass.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import com.example.greenpass.MainActivity
import com.example.greenpass.R
import com.example.greenpass.utils.Rotate3dAnimation
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

//        greenpass_img.animation = AnimationUtils.loadAnimation(this, R.anim.from_top_splash)
//        greenpass_label.animation = AnimationUtils.loadAnimation(this, R.anim.from_bottom_splash)

        gate_rotation(heaven_gate_left,true,35f)
        gate_rotation(heaven_gate_right,false,35f)
        GlobalScope.launch {
            delay(1500L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun gate_rotation(view: View, isLeft: Boolean,angleDelta: Float){
        val rotation = if (isLeft)
            Rotate3dAnimation(0f,angleDelta,0f,view.height/2.0f,0f,false)
        else
            Rotate3dAnimation(180f,180f - angleDelta,0f,view.height/2.0f,0f,false)
        rotation.apply {
            duration = 500
            fillAfter = true
            interpolator = AccelerateInterpolator()
        }
        val translateAnimation = TranslateAnimation(0f,view.width.toFloat(),0f,0f)
        translateAnimation.fillAfter = true
        view.startAnimation(translateAnimation)
        view.startAnimation(rotation)
    }
}