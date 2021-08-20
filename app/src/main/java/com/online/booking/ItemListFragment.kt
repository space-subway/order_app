package com.online.booking

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.online.booking.databinding.FragmentItemListBinding
import com.online.booking.domain.Item
import com.online.booking.web.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemListFragment : BaseFragment() {

    private var items: MutableList<Item>? = ArrayList<Item>()
    private lateinit var itemAdapter : ItemsAdapter

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat =
        ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
            if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            false
        }

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView = binding.recyclerItemsView

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            var item = itemView.tag as Item
            val bundle = Bundle()
            bundle.putString(
                ItemDetailFragment.ARG_ITEM_ID,
                item.id
            )
            itemView.findNavController().navigate(R.id.show_item_detail, bundle)
        }

        itemAdapter = ItemsAdapter(items!!, onClickListener)

        recyclerView.adapter = itemAdapter

    }

    override fun onResume() {
        super.onResume()

        loadItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadItems(){
        val itemService = retrofit.create(ItemService::class.java)

        val itemServiceCall = itemService.list()

        itemServiceCall.enqueue( object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if(response.code() == 200){
                    val recieved = response.body()!!.asReversed()

                    items!!.clear()
                    items!!.addAll(recieved)
                }

                itemAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        } )
    }
}