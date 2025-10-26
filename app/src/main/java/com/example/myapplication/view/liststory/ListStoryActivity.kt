package com.example.myapplication.view.liststory

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.ListStoryAdapter
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.databinding.ActivityListStoryBinding
import com.example.myapplication.di.Injection
import com.example.myapplication.view.ViewModelFactory
import com.example.myapplication.view.detail.DetailActivity
import java.util.Objects

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding

    // ViewModel pakai delegate biar lebih clean
    private val listStoryViewModel by viewModels<ListStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setHomeAsUpIndicator(R.mipmap.logo_foreground);

        // Setup RecyclerView
        val adapter = ListStoryAdapter()
        binding.rvHeroes.layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.adapter = adapter

        // Observers
        listStoryViewModel.listItem.observe(this) { items ->
            setEventData(items, adapter)
        }

        listStoryViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setEventData(items: List<ListStoryItem>?, adapter: ListStoryAdapter) {
        adapter.submitList(items)
//        adapter.setOnItemClickListener { selected ->
////            val intent = Intent(this, DetailActivity::class.java)
////            intent.putExtra("detail_item", selectedEvent)
////            startActivity(intent)
//
//        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}