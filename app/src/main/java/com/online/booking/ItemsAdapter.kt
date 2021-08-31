package com.online.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.online.booking.domain.Item

class ItemsAdapter(private val items: List<Item>,
                   private val onClickListener: View.OnClickListener
                   ) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var tittleTextView              : TextView = view.findViewById(R.id.title)
        var priceTextView               : TextView = view.findViewById(R.id.price)
        var shortDescriptionTextView    : TextView = view.findViewById(R.id.short_description)
        var ratingTextView              : TextView = view.findViewById(R.id.rating)
        var ratingStarsTextView         : TextView = view.findViewById(R.id.rating_stars)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[ position ]

        holder.tittleTextView.text              = item.title
        holder.priceTextView.text               = "$" + item.price
        holder.shortDescriptionTextView.text    = item.descriptionShort
        var overrageRating = item.overrageRating()
        holder.ratingTextView.text              = overrageRating.toString()
        //calculate stars
        var starCount = overrageRating.toInt()
        when( starCount ){
            1 -> holder.ratingStarsTextView.text = "●"
            2 -> holder.ratingStarsTextView.text = "● ●"
            3 -> holder.ratingStarsTextView.text = "● ● ●"
            4 -> holder.ratingStarsTextView.text = "● ● ● ●"
            5 -> holder.ratingStarsTextView.text = "● ● ● ● ●"
            else -> {
                holder.ratingTextView.visibility = View.GONE
                holder.ratingStarsTextView.visibility = View.GONE
            }
        }

        holder.itemView.tag = item
        holder.itemView.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}