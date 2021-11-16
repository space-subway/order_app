package com.online.booking

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.online.booking.databinding.ActivityMainBinding
import com.online.booking.web.utils.InternetConnectionListener
import com.online.booking.web.utils.Refreshable

class MainActivity : AppCompatActivity(), InternetConnectionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var _binding: ActivityMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).connectionListener = this

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMainActivity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    fun showUpToolbar(){
        binding.appbarMainActivity.setExpanded(true, true)
    }

    fun setTitle( title : String ){
        supportActionBar?.title = title
    }

    fun refresh( view : View ){

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

        if( navHostFragment.childFragmentManager.fragments[0] is Refreshable ){
            val refreshableFragment = navHostFragment.childFragmentManager.fragments[0] as Refreshable
            refreshableFragment.refresh()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onPause() {
        (application as App).connectionListener = null
        super.onPause()
    }

    override fun onInternetUnavailable() {
        runOnUiThread {
            binding.connectionMessage.text = getString(R.string.no_internet)
            binding.networkStatusView.visibility = View.VISIBLE
            binding.navHostFragmentItemDetail.visibility = View.GONE
        }
    }

    override fun onServerIsNotAvailable(){
        runOnUiThread{
            binding.connectionMessage.text = getString(R.string.no_server)
            binding.networkStatusView.visibility = View.VISIBLE
            binding.navHostFragmentItemDetail.visibility = View.GONE
        }
    }

    override fun onServerResponse(code: Int) {
        runOnUiThread{
            when( code ) {
                200 -> {
                    //hide error view and show recycler list view
                    binding.networkStatusView.visibility = View.GONE
                    binding.navHostFragmentItemDetail.visibility = View.VISIBLE
                }
                404 -> {
                    binding.connectionMessage.text = getString(R.string.page_not_found)
                    binding.networkStatusView.visibility = View.VISIBLE
                    binding.navHostFragmentItemDetail.visibility = View.GONE
                }
            }
        }
    }
}