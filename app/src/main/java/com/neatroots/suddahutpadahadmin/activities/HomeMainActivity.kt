package com.neatroots.suddahutpadahadmin.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.neatroots.suddahutpadahadmin.databinding.ActivityHomeMainBinding

class HomeMainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.WHITE)
        )

        binding.apply {

//            button.setOnClickListener {
//                Firebase.auth.signOut()
//                startActivity(Intent(this@HomeMainActivity, WelcomeActivity::class.java))
//                finish()
//            }

        }


    }



}