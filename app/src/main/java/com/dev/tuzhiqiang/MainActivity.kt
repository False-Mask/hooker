package com.dev.tuzhiqiang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.tuzhiqiang.aop.Tester
import com.dev.tuzhiqiang.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnStatic.setOnClickListener {
      Tester.testStaticHook()
    }

    binding.btnMember.setOnClickListener {
      Tester().testMemberHook()
    }
  }
}