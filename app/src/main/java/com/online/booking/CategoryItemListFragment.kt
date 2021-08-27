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

class CategoryItemListFragment : BaseFragment() {

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

        var categoryTab = binding.tabLayout

        var titles = arrayListOf("category 1", "category 2")
        //categoryTab.setTitlesAtTabs( titles )

        var viewPager   = binding.tabViewpager
        var adapter : ViewPagerAdapter = ViewPagerAdapter(activity as MainActivity, titles.size)
        viewPager.adapter = adapter

        TabLayoutMediator(categoryTab, viewPager) { tab, position ->
            tab.text = "category"
            viewPager.setCurrentItem(tab.position, true)
        }.attach()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    class ViewPagerAdapter(activity: AppCompatActivity, val itemsCount: Int ): FragmentStateAdapter( activity ){
        override fun getItemCount(): Int {
            return itemsCount
        }

        override fun createFragment(position: Int): Fragment {
            return ItemListFragment.getInstance(position)
        }
    }

}