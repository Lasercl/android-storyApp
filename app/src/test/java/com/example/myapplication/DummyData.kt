package com.example.myapplication

import com.example.myapplication.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = "story-$i",
                name = "User $i",
                description = "Ini adalah deskripsi untuk story ke-$i.",
                photoUrl = "https://picsum.photos/200?random=$i",
                createdAt = "2025-10-30T0${i % 10}:00:00Z",
                lat = -6.2 + (i * 0.01),
                lon = 106.8 + (i * 0.01)
            )
            items.add(story)
        }
        return items
    }
}