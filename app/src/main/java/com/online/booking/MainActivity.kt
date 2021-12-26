package com.online.booking

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.online.booking.data.viewmodel.ItemViewModel
import com.online.booking.databinding.ActivityMainBinding
import com.online.booking.utils.Refreshable
import com.online.booking.utils.Status
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var _binding: ActivityMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMainActivity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.download_all_items -> {

            val viewModel = ViewModelProviders.of( this ).get( ItemViewModel::class.java )

            val scope = CoroutineScope( Job() + Dispatchers.Main )
            val job = scope.launch {
                viewModel.getItems().observe( this@MainActivity, { resource ->
                    resource?.let {
                        when( resource.status ){
                            Status.SUCCESS -> {
                                
                                for( item in resource?.data!!){
                                    scope.launch {
                                        viewModel.getItem( item.id )
                                    }
                                }

                                //TODO calculate progress of loaded items
                            }
                            Status.LOADING -> {
                                //TODO replace download all items icon to circle progress bar
                            }
                            Status.ERROR -> {
                                //TODO show error
                            }
                        }
                    }
                } )
            }

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
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

        if( navHostFragment.childFragmentManager.fragments[0] is Refreshable){
            val refreshableFragment = navHostFragment.childFragmentManager.fragments[0] as Refreshable

            refreshableFragment.refresh()

            binding.networkStatusView.visibility = View.GONE
            binding.navHostFragmentItemDetail.visibility = View.VISIBLE
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

    fun onNetworkError(message: String?) {
        binding.connectionMessage.text = message
        binding.networkStatusView.visibility = View.VISIBLE
        binding.navHostFragmentItemDetail.visibility = View.GONE
    }
}