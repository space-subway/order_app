package com.online.booking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.online.booking.domain.Item
import com.online.booking.web.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    public final val BASE_URL = "http://10.0.2.2:8080"

    private var items: MutableList<Item>? = ArrayList<Item>()
    private lateinit var itemAdapter : ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main )

        title = "Main Activity Title"

        val recyclerView : RecyclerView = findViewById( R.id.recyclerView )

        itemAdapter = ItemsAdapter( items!! )

        val layoutManager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager  = layoutManager
        recyclerView.adapter        = itemAdapter

        loadItems()
    }

    private fun loadItems(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val itemService = retrofit.create(ItemService::class.java)

        val itemServiceCall = itemService.list()

        itemServiceCall.enqueue( object : Callback<List<Item>>{
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if(response.code() == 200){
                    val recieved = response.body()!!.asReversed()

                    items!!.clear()
                    items!!.addAll(recieved)
                }

                itemAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {

            }

        } )
    }
}