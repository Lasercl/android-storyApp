package com.example.myapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.remote.response.ListStoryItem
import com.example.myapplication.databinding.ItemRowHeroBinding
import com.example.myapplication.view.detail.DetailActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter

class ListStoryAdapter: PagingDataAdapter<ListStoryItem, ListStoryAdapter.MyViewHolder>(ListStoryAdapter.Companion.DIFF_CALLBACK)  {
    private var onItemClickListener: ((ListStoryItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bind(storyItem!!)
    }
    class MyViewHolder(val binding: ItemRowHeroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            binding.tvStoryTitle.text="${story.name}"
            binding.tvStoryDesc.text="${story.description}"
            Glide.with(binding.root.context)
                .load("${story.photoUrl}")
                .into(binding.imgStory)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("Story", story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStory, "imageStory"),
                        Pair(binding.tvStoryTitle, "title"),
                        Pair(binding.tvStoryDesc, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}