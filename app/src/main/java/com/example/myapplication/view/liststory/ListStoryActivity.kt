package com.example.myapplication.view.liststory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.ListStoryAdapter
import com.example.myapplication.adapter.LoadingStateAdapter
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.databinding.ActivityListStoryBinding
import com.example.myapplication.di.Injection
import com.example.myapplication.view.ViewModelFactory
import com.example.myapplication.view.detail.DetailActivity
import com.example.myapplication.view.feature.add_story.AddStoryActivity
import com.example.myapplication.view.map.MapsActivity
import java.util.Objects

class ListStoryActivity : AppCompatActivity() {
    val adapter = ListStoryAdapter()

    private lateinit var binding: ActivityListStoryBinding

    private val listStoryViewModel by viewModels<ListStoryViewModel> {
        ViewModelFactory.getInstance(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar())?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setHomeAsUpIndicator(R.mipmap.logo_foreground);

        binding.rvHeroes.layoutManager = LinearLayoutManager(this)
        binding.fabAdd.setOnClickListener { v->
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
        getStory()
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
        }



    }
    private fun getStory(){

        binding.rvHeroes.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        listStoryViewModel.listItem.observe(this,{items ->
            adapter.submitData(lifecycle,items)
            adapter.addLoadStateListener { loadState ->
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Klik tombol “back” di kiri
                onBackPressed()
                true
            }
            R.id.action_map-> {
                // Klik tombol peta
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
//            R.id.action_more -> {
//                // Klik tombol menu (misalnya tampilkan popup)
//                Toast.makeText(this, "Menu diklik!", Toast.LENGTH_SHORT).show()
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()

    }

//    private fun setEventData(items: List<ListStoryItem>?, adapter: ListStoryAdapter) {
//        adapter.submitData(lifecycle,items!!)
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}