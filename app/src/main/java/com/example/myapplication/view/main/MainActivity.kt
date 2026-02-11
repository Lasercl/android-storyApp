package com.example.myapplication.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.view.ViewModelFactory
import com.example.myapplication.view.liststory.ListStoryActivity
import com.example.myapplication.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.Companion.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                var name=user.email
                var message="Hai, $name \uD83D\uDC4B"
                binding.nameTextView.text=message

            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.infoCard.setOnClickListener {
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)
        }
//        var message = intent.getStringExtra("PERSON_EMAIL")
//        message="Hai, $message \uD83D\uDC4B"
//        binding.nameTextView.text=message
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

}