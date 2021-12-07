package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
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

        GlobalScope.launch ( Dispatchers.Main ) {
            loadItems()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupCategoriesAdapter( itemsMap: SortedMap<ItemCategory, MutableList<Item>> ){
        var categoryTab = binding.tabLayout

        var viewPager   = binding.tabViewpager
        var adapter = ViewPagerAdapter(activity as MainActivity, itemsMap)
        viewPager.adapter = adapter

        TabLayoutMediator(categoryTab, viewPager) { tab, position ->
            tab.text = itemsMap.keys.elementAt(position).name
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun loadItems(){

        val viewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        viewModel.getItems().observe(this, { resource ->
            resource?.let { resource ->

                val itemsMap : MutableMap<ItemCategory, MutableList<Item>> = HashMap()

                when (resource.status) {
                    Status.SUCCESS -> {
                        //update ui
                        binding.progressBar.visibility = View.GONE

                        resource.data?.let {

                            var iterator = it.listIterator()
                            while( iterator.hasNext() ){
                                var item = iterator.next()
                                if( item.category != null ){

                                    if( itemsMap[item.category] == null )
                                        itemsMap[item.category!!] = ArrayList()

                                    itemsMap[item.category]!!.add( item )
                                }
                            }

                            setupCategoriesAdapter( itemsMap.toSortedMap { t, t2 -> t.name.compareTo(t2.name) } )

                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE

                        if( resource.data == null ) (activity as MainActivity).onNetworkError( resource.message )

                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

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