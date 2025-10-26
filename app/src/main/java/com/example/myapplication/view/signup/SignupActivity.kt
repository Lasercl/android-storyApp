package com.example.myapplication.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.data.Result
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivitySignupBinding
import com.example.myapplication.view.ViewModelFactory
import com.example.myapplication.view.login.LoginViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupView()
        setupAction()
        playAnimation()

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
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val nameTextview = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val nameEditText = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)

        val emailTextview = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(100)
        val emailEditText= ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(100)
        val passwordTextview= ObjectAnimator.ofFloat(binding.passwordTextView,View.ALPHA, 0f, 1f).setDuration(100)
        val passwordEditText= ObjectAnimator.ofFloat(binding.passwordEditTextLayout,View.ALPHA, 0f, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(title, nameTextview, nameEditText,emailTextview,emailEditText,passwordTextview,passwordEditText,signup)
            startDelay=100
            start()
        }
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
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()


            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Email tidak boleh kosong"
                }
                password.length < 8 -> {
                    binding.passwordEditTextLayout.error = "Password minimal 8 karakter"
                }
                else -> {
                    viewModel.register(name, email, password).observe(this) { result ->
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                AlertDialog.Builder(this).apply {
                                    setTitle("Yeah!")
                                    setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    this,
                                    "Registrasi gagal: ${result.error}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}