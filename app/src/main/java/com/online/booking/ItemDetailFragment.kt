package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.online.booking.databinding.FragmentItemDetailBinding
import com.online.booking.databinding.FragmentItemListBinding
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

    private var _binding: FragmentItemDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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
        binding.itemPrice.text = "$" + item.price
        binding.itemTitle.text = item.title
        binding.shortDescription.text = item.descriptionShort
        binding.description.text = item.description
        binding.itemRating.text = item.rating?.toString()
        if( item.viewCount != null ) binding.itemViewCount.text = item.viewCount.toString() + " Views"
    }
}