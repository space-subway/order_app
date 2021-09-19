package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.online.booking.databinding.FragmentItemDetailBinding
import com.online.booking.domain.Item
import com.online.booking.web.ItemService
import com.online.booking.web.utils.Refreshable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemDetailFragment : Fragment(), Refreshable {
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).showUpToolbar()

        loadItem()
    }

    private fun updateUI( item : Item ){
        binding.itemPrice.text = "$" + item.price
        binding.itemTitle.text = item.title
        binding.shortDescription.text = item.descriptionShort
        if ( item.description == null || "" == item.description) {
            binding.descriptionTitle.visibility = View.GONE
        } else {
            binding.description.text = item.description
        }

        var overrageRating = item.overrageRating()
        binding.itemRating.text = overrageRating.toString()
        //calculate stars
        var starCount = overrageRating.toInt()
        when( starCount ){
            1 -> binding.itemRatingStars.text = "●"
            2 -> binding.itemRatingStars.text = "● ●"
            3 -> binding.itemRatingStars.text = "● ● ●"
            4 -> binding.itemRatingStars.text = "● ● ● ●"
            5 -> binding.itemRatingStars.text = "● ● ● ● ●"
            else -> {
                binding.itemRating.visibility = View.GONE
                binding.itemRatingStars.visibility = View.GONE
            }
        }

        if( item.viewCount != null ) binding.itemViewCount.text = item.viewCount.toString() + " Views"
    }

    private fun loadItem() {
        val itemService = (activity?.application as App).retrofit.create(ItemService::class.java)
        val itemServiceCall = itemService.getItem(itemId!!)

        itemServiceCall.enqueue( object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if( response.code() == 200 ){
                    val receivedItem = response.body() as Item

                    updateUI(receivedItem)
                }

                (this@ItemDetailFragment.activity as MainActivity).onServerResponse( response.code() )
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                (this@ItemDetailFragment.activity as MainActivity).onServerIsNotAvailable()
            }
        })
    }

    override fun refresh() {
        loadItem()
    }
}