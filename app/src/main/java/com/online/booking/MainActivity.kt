package com.online.booking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.online.booking.domain.Item
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {
    private val items = ArrayList<Item>()
    private lateinit var itemAdapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main )

        title = "Main Activity Title"

        val recyclerView : RecyclerView = findViewById( R.id.recyclerView )

        itemAdapter = ItemsAdapter( items )

        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager  = layoutManager
        recyclerView.adapter        = itemAdapter

        prepareItems()
    }

    private fun prepareItems(){
        items.add( Item("Item 1", "description", BigDecimal(50.0)) )
        items.add( Item("Item 2", "description", BigDecimal(50.0)) )
        items.add( Item("Item 3", "description", BigDecimal(50.0)) )
        items.add( Item("Item 4", "description", BigDecimal(50.0)) )
        items.add( Item("Item 5", "description", BigDecimal(50.0)) )
        items.add( Item("Item 6", "description", BigDecimal(50.0)) )
        items.add( Item("Item 7", "description", BigDecimal(50.0)) )
        items.add( Item("Item 8", "description", BigDecimal(50.0)) )
        items.add( Item("Item 9", "description", BigDecimal(50.0)) )

        itemAdapter.notifyDataSetChanged()
    }
}