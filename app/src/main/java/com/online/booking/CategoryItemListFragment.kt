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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryItemListFragment : BaseFragment() {

    private var itemsMap : MutableMap<ItemCategory, MutableList<Item>>? = HashMap()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            tab.text = itemsMap!!.keys.elementAt(position)!!.name
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun loadItems(){
        val itemService = retrofit.create(ItemService::class.java)

        val itemServiceCall = itemService.list()

        itemServiceCall.enqueue( object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if(response.code() == 200){
                    val recieved = response.body()!!.asReversed()

                    itemsMap!!.clear()

                    var it = recieved.listIterator()
                    while( it.hasNext() ){
                        var item = it.next()
                        if( item.category != null ){

                            if( itemsMap!![item.category] == null )
                                itemsMap!![item.category] = ArrayList<Item>()

                            itemsMap!![item.category]!!.add( item )
                        }
                    }

                    setupCategoriesAdapter()
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        } )
    }

    class ViewPagerAdapter(activity: AppCompatActivity, val itemsMap: MutableMap<ItemCategory, MutableList<Item>>? ): FragmentStateAdapter( activity ){
        override fun getItemCount(): Int {
            return itemsMap!!.keys.size
        }

        override fun createFragment(position: Int): Fragment {
            var category = itemsMap!!.keys.elementAt(position)
            var items = itemsMap[ category ]

            return ItemListFragment.getInstance(items as ArrayList<Item>?)
        }
    }

}