package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.online.booking.domain.Item
import com.online.booking.web.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemDetailFragment : BaseFragment() {
    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    private var itemId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if(it.containsKey(
                    ARG_ITEM_ID
            )){
                itemId = it.getString(ARG_ITEM_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_item_detail, container, false)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        val itemService = retrofit.create(ItemService::class.java)
        val itemServiceCall = itemService.getItem(itemId!!)

        itemServiceCall.enqueue( object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if( response.code() == 200 ){
                    val recievedItem = response.body() as Item

                    updateUI(recievedItem)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun updateUI( item : Item ){
        TODO("Fill user interface")
    }
}