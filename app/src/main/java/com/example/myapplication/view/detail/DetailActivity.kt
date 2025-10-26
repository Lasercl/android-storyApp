package com.example.myapplication.view.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.remote.response.ListStoryItem
import java.util.Objects

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupData()
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<ListStoryItem>("Story") as ListStoryItem
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .circleCrop()
            .into(findViewById(R.id.image_detail))
        findViewById<TextView>(R.id.title_detail).text = story.name
        findViewById<TextView>(R.id.desc_detail).text = story.description
    }
}