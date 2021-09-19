package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.online.booking.databinding.FragmentCategoryItemListBinding
import com.online.booking.domain.Item
import com.online.booking.domain.ItemCategory
import com.online.booking.web.ItemService
import com.online.booking.web.utils.Refreshable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryItemListFragment : Fragment(), Refreshable {

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

        loadItems()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupCategoriesAdapter(){
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
        val itemService = (activity?.application as App).retrofit.create(ItemService::class.java)

        val itemServiceCall = itemService.list()

        itemServiceCall.enqueue( object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if(response.code() == 200){
                    val received = response.body()!!.asReversed()

                    itemsMap.clear()

                    var it = received.listIterator()
                    while( it.hasNext() ){
                        var item = it.next()
                        if( item.category != null ){

                            if( itemsMap[item.category] == null )
                                itemsMap[item.category] = ArrayList()

                            itemsMap[item.category]!!.add( item )
                        }
                    }

                    setupCategoriesAdapter()
                }

                (this@CategoryItemListFragment.activity as MainActivity).onServerResponse( response.code() )
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                (this@CategoryItemListFragment.activity as MainActivity).onServerIsNotAvailable()
            }

        } )
    }

    override fun refresh() {
        loadItems()
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