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
import com.online.booking.data.model.Item

class ItemListFragment: Fragment() {
    private lateinit var itemAdapter : ItemsAdapter

    companion object {
        const val ARG_ITEMS = "items"

        fun getInstance( items: ArrayList<Item>? ): Fragment{
            var bundle = Bundle()
            bundle.putParcelableArrayList( ARG_ITEMS, items)
            var fragment = ItemListFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var items = requireArguments().getParcelableArrayList<Item>(ARG_ITEMS)

        items?.let{
            itemAdapter = ItemsAdapter(items, View.OnClickListener { itemView ->

                // Click Listener to trigger navigation based

                var item = itemView.tag as Item
                val bundle = Bundle()
                bundle.putParcelable(
                    ItemDetailFragment.ARG_ITEM,
                    item
                )
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            })
            itemAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        var recyclerView = binding.recyclerItemsView
        recyclerView.adapter = itemAdapter

        return binding.root
    }

}