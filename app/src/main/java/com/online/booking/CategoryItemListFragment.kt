package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.online.booking.data.model.Item
import com.online.booking.data.model.ItemCategory
import com.online.booking.data.viewmodel.ItemViewModel
import com.online.booking.databinding.FragmentCategoryItemListBinding
import com.online.booking.utils.Refreshable
import com.online.booking.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CategoryItemListFragment : Fragment(), Refreshable {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private var itemsMap : MutableMap<ItemCategory, MutableList<Item>> = HashMap()

    private var _binding: FragmentCategoryItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setVisibleActionItem(0, true)

        GlobalScope.launch ( Dispatchers.Main ) {
            loadItems()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupCategoriesAdapter( itemsMap: SortedMap<ItemCategory, MutableList<Item>> ){
        val categoryTab = binding.tabLayout

        viewPager   = binding.tabViewpager

        adapter = ViewPagerAdapter(activity as MainActivity, itemsMap)
        viewPager.adapter = adapter

        TabLayoutMediator(categoryTab, viewPager) { tab, position ->
            tab.text = itemsMap.keys.elementAt(position).name
        }.attach()
    }

    private fun loadItems(){

        val viewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        viewModel.getItems().observe(this, { resource ->
            resource?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS_REMOTE, Status.SUCCESS_LOCAL -> {
                        //update ui
                        binding.progressBar.visibility = View.GONE

                        resource.data?.let {
                            itemsMap = convertResponse( it )
                            setupCategoriesAdapter( itemsMap.toSortedMap { t, t2 -> t.name.compareTo(t2.name) } )
                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE

                        if(itemsMap.isNullOrEmpty()) (activity as MainActivity).onNetworkError( resource.message )

                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

    private fun convertResponse( itemList: List<Item> ): MutableMap<ItemCategory, MutableList<Item>>{

        val itemsMap : MutableMap<ItemCategory, MutableList<Item>> = HashMap()

        val iterator = itemList.listIterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if( item.category != null ){

                if( itemsMap[item.category] == null )
                    itemsMap[item.category!!] = ArrayList()

                itemsMap[item.category]!!.add( item )
            }
        }

        return itemsMap
    }

    override fun refresh() {
        GlobalScope.launch ( Dispatchers.Main ) {
            loadItems()
        }
    }

    class ViewPagerAdapter(activity: AppCompatActivity, private val itemsMap: MutableMap<ItemCategory, MutableList<Item>> ): FragmentStateAdapter( activity ){
        override fun getItemCount(): Int {
            return itemsMap.keys.size
        }

        override fun createFragment(position: Int): Fragment {
            var category = itemsMap.keys.elementAt(position)
            var items = itemsMap[ category ]

            return ItemListFragment.getInstance(items as ArrayList<Item>?)
        }
    }

}