package com.example.myapplication.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.example.myapplication.data.Result
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.view.ViewModelFactory
import com.example.myapplication.view.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password=binding.passwordEditText.text.toString()
//            viewModel.saveSession(UserModel(email, "sample_token"))

            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        // pindah ke halaman utama
//                        startActivity(Intent(this, MainActivity::class.java))
//                        finish()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Login gagal: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(100)

        val emailTextview = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val emailEditText= ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val passwordTextview= ObjectAnimator.ofFloat(binding.passwordTextView,View.ALPHA, 0f, 1f).setDuration(100)
        val passwordEditText= ObjectAnimator.ofFloat(binding.passwordEditTextLayout,View.ALPHA, 0f, 1f).setDuration(100)


        AnimatorSet().apply {
            startDelay=100

            playSequentially(title,emailTextview,emailEditText,passwordTextview,passwordEditText,login)
            start()
        }
    }

}