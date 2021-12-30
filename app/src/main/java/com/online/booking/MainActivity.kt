package com.online.booking

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

            val menuItem = binding.toolbarMainActivity.menu.getItem( 0 )
            menuItem.isVisible = false

            val scope = CoroutineScope( Job() + Dispatchers.Main )
            scope.launch {
                viewModel.getItems().observe( this@MainActivity, { resource ->
                    resource?.let {
                        when( resource.status ){
                            Status.SUCCESS_REMOTE -> {

                                binding.progressIndicator.visibility = View.GONE
                                binding.progressIndicator.isIndeterminate = false
                                binding.progressIndicator.progress = 0
                                binding.progressIndicator.visibility = View.VISIBLE

                                val itemsSize = resource?.data!!.size
                                var loadedItemSize = 0

                                for( item in resource?.data!!){
                                    scope.launch {
                                        viewModel.getItem( item.id ).observe( this@MainActivity, { resource ->
                                            resource?.let {
                                                when( resource.status ){
                                                    Status.SUCCESS_REMOTE -> {
                                                        loadedItemSize++

                                                        val progress = itemsSize / 100 * loadedItemSize

                                                        binding.progressIndicator.progress = progress
                                                    }
                                                    Status.ERROR -> {
                                                        binding.progressIndicator.visibility = View.GONE
                                                        menuItem.isVisible = true
                                                        showPopUpMessage( resource.message )

                                                        resource.message?.let { msg -> this.cancel(msg) }
                                                    }
                                                }
                                            }
                                        } )
                                    }
                                    binding.progressIndicator.visibility = View.GONE
                                    menuItem.isVisible = true
                                }
                                //update ui
                                refresh()

                                binding.networkStatusView.visibility = View.GONE
                                binding.navHostFragmentItemDetail.visibility = View.VISIBLE
                            }
                            Status.LOADING -> {
                                menuItem.isVisible = false
                                binding.progressIndicator.visibility = View.GONE
                                binding.progressIndicator.isIndeterminate = true
                                binding.progressIndicator.visibility = View.VISIBLE
                            }
                            Status.ERROR -> {
                                binding.progressIndicator.visibility = View.GONE
                                menuItem.isVisible = true
                                showPopUpMessage( it.message )

                                resource.message?.let { msg -> this.cancel(msg) }
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

    fun refreshBtnClick( view: View ){
        refresh()
    }

    private fun refresh(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

        if( navHostFragment.childFragmentManager.fragments[0] is Refreshable){
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

    fun onNetworkError(message: String?) {
        binding.connectionMessage.text = message
        binding.networkStatusView.visibility = View.VISIBLE
        binding.navHostFragmentItemDetail.visibility = View.GONE
    }

    private fun showPopUpMessage(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}