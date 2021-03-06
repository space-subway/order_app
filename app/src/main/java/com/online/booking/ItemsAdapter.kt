package com.online.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.online.booking.data.model.Item
import java.math.BigDecimal

class ItemsAdapter(private val items: List<Item>,
                   private val onClickListener: View.OnClickListener
                   ) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var tittleTextView              : TextView = view.findViewById(R.id.title)
        var priceTextView               : TextView = view.findViewById(R.id.price)
        var shortDescriptionTextView    : TextView = view.findViewById(R.id.short_description)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[ position ]

        holder.tittleTextView.text              = item.title
        holder.priceTextView.text               = "$" + item.price.toDouble()
        holder.shortDescriptionTextView.text    = item.descriptionShort

        holder.itemView.tag = item
        holder.itemView.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}