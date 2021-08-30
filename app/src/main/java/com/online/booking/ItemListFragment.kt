package com.online.booking

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import com.online.booking.databinding.FragmentItemListBinding
import com.online.booking.domain.Item
import com.online.booking.domain.ItemCategory
import com.online.booking.web.ItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemListFragment: BaseFragment() {
    private lateinit var itemAdapter : ItemsAdapter

    companion object {
        public final val ARG_ITEMS = "items"

        fun getInstance( items: ArrayList<Item>? ): BaseFragment{
            var bundle = Bundle()
            bundle.putParcelableArrayList( ARG_ITEMS, items)
            var fragment = ItemListFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

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

        var items = requireArguments().getParcelableArrayList<Item>(ARG_ITEMS)

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

        itemAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}