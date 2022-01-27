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
import com.google.gson.Gson
import com.online.booking.data.model.Item
import com.online.booking.data.model.ItemCategory
import com.online.booking.data.viewmodel.ItemViewModel
import com.online.booking.databinding.FragmentCategoryItemListBinding
import com.online.booking.utils.Refreshable
import com.online.booking.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CategoryItemListFragment : Fragment(), Refreshable {
    companion object {
        const val ARG_ITEMS_LIST = "items_list"
    }

    private lateinit var viewPager: ViewPager2
    private var items : List<Item> = ArrayList()

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

        viewPager   = binding.tabViewpager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setVisibleActionItem(0, true)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState == null || items.isEmpty()){
            MainScope().launch ( Dispatchers.Main ) {
                loadItems()
            }
        } else if(savedInstanceState.containsKey(ARG_ITEMS_LIST)){
            items = savedInstanceState.getParcelableArrayList(ARG_ITEMS_LIST)!!
            setupCategoriesAdapter(items)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(ARG_ITEMS_LIST, items as ArrayList<Item>)
        super.onSaveInstanceState(outState)
    }

    private fun setupCategoriesAdapter( list: List<Item> ){

        val itemsMap: Map<ItemCategory, MutableList<Item>> = convertResponse( list )

        val categoryTab = binding.tabLayout

        val adapter = ViewPagerAdapter(activity as MainActivity, itemsMap.toSortedMap { t, t2 -> t.name.compareTo(t2.name) })
        viewPager.adapter = adapter

        TabLayoutMediator(categoryTab, viewPager) { tab, position ->
            tab.text = itemsMap.keys.elementAt(position).name
        }.attach()
    }

    private fun loadItems(){

        val viewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        viewModel.getItems().observe(viewLifecycleOwner, { resource ->
            resource?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS_REMOTE, Status.SUCCESS_LOCAL -> {
                        //update ui
                        binding.progressBar.visibility = View.GONE

                        resource.data?.let {
                            items = it
                            setupCategoriesAdapter( items!! )
                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE

                        if(items.isNullOrEmpty()) (activity as MainActivity).onNetworkError( resource.message )

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

        val iterator = itemList.sortedByDescending { it.rating }.listIterator()
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
        MainScope().launch ( Dispatchers.Main ) {
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