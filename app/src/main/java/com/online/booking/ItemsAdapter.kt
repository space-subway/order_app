package com.online.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.online.booking.domain.Item

class ItemsAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>(){
    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var tittleTextView : TextView = view.findViewById(R.id.title)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[ position ]
        holder.tittleTextView.text = item.tittle
    }

    override fun getItemCount(): Int {
        return items.size
    }
}