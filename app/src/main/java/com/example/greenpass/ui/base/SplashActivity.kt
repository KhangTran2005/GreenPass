package com.example.greenpass.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.*
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

        gateRotation(heaven_gate_left,true,35f)
        gateRotation(heaven_gate_right,false,35f)
        GlobalScope.launch {
            delay(500)
            starRotation(star,800)
        }
        GlobalScope.launch {
            delay(1500L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun starRotation(view:View,length: Long){
        val rotate = RotateAnimation(0f,180f,0.5f,0.5f)
        val alpha1 = AlphaAnimation(0f,1.0f)
        val alpha2 = AlphaAnimation(1.0f,0f)
        rotate.apply {
            duration=length
            fillAfter=true
        }
        alpha1.apply {
            duration=length/2
            fillAfter=true
            interpolator=AccelerateInterpolator()
        }
        alpha2.apply {
            duration=length/2
            fillAfter=true
            interpolator=AccelerateInterpolator()
            startOffset=length/2
        }
        view.startAnimation(rotate)
        view.startAnimation(alpha1)
        view.startAnimation(alpha2)
    }

    private fun gateRotation(view: View, isLeft: Boolean, angleDelta: Float){
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