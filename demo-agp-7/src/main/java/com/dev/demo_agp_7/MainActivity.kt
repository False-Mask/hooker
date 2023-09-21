package com.dev.demo_agp_7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.demo_agp_7.aop.Tester
import com.dev.demo_agp_7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStatic.setOnClickListener {
            Tester.testStaticHook()
        }

        binding.btnMember.setOnClickListener {
            Tester().testMemberHook()
        }
    }
}