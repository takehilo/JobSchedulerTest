package com.example.jobschedulertest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(private val times: List<String>): RecyclerView.Adapter<TextViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder = TextViewHolder(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val time = times[position]
        holder.configure(time)
    }

    override fun getItemCount(): Int {
        return times.size
    }
}

class TextViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)) {

    fun configure(text: String) {
        itemView.textView.text = text
    }
}