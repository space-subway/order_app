package com.online.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.online.booking.data.api.ApiHelper
import com.online.booking.data.api.RetrofitBuilder
import com.online.booking.data.model.Item
import com.online.booking.data.model.ItemCategory
import com.online.booking.data.repository.ItemRepository
import com.online.booking.databinding.FragmentCategoryItemListBinding
import com.online.booking.utils.Refreshable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        GlobalScope.launch ( Dispatchers.Main ) {
            loadItems()
        }
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

    private suspend fun loadItems(){
        binding.progressBar.visibility = View.VISIBLE

        val itemRepository = ItemRepository( ApiHelper(RetrofitBuilder.apiService) )

        val items = withContext(Dispatchers.IO) {
            itemRepository.getItems()
        }

        //update ui
        binding.progressBar.visibility = View.GONE
        itemsMap.clear()

        var it = items.listIterator()
        while( it.hasNext() ){
            var item = it.next()
            if( item.category != null ){

                if( itemsMap[item.category] == null )
                    itemsMap[item.category!!] = ArrayList()

                itemsMap[item.category]!!.add( item )
            }
        }

        setupCategoriesAdapter()

        //setup observer
        /*itemRepository.getItems().enqueue( object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if(response.code() == 200){
                    val received = response.body()!!.asReversed()

                    itemsMap.clear()

                    var it = received.listIterator()
                    while( it.hasNext() ){
                        var item = it.next()
                        if( item.category != null ){

                            if( itemsMap[item.category] == null )
                                itemsMap[item.category!!] = ArrayList()

                            itemsMap[item.category]!!.add( item )
                        }
                    }

                    setupCategoriesAdapter()
                }

                binding.progressBar.visibility = View.GONE

                (this@CategoryItemListFragment.activity as InternetConnectionListener).onServerResponse( response.code() )
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                (this@CategoryItemListFragment.activity as InternetConnectionListener).onServerIsNotAvailable()
            }

        } )*/
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