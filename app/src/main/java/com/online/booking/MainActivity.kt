package com.online.booking

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.*
import com.online.booking.databinding.ActivityMainBinding
import com.online.booking.utils.DownloadAllItemsWorker
import com.online.booking.utils.DownloadAllItemsWorker.Companion.PROGRESS
import com.online.booking.utils.Refreshable

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

            setVisibleActionItem(0, false)

            val downloadAllItemsWork = OneTimeWorkRequestBuilder<DownloadAllItemsWorker>()
                .build()

            val workManager = WorkManager.getInstance(this)

            workManager.enqueue(downloadAllItemsWork)

            workManager.getWorkInfoByIdLiveData(downloadAllItemsWork.id)
                .observe(this, { info ->
                    if( info != null ){
                        when( info.state ){
                            WorkInfo.State.RUNNING -> {
                                val progress = info.progress.getInt(PROGRESS, 0)
                                if(progress == 0) {
                                    //init progress bar
                                    binding.progressIndicator.visibility = View.GONE
                                    binding.progressIndicator.isIndeterminate = true
                                    binding.progressIndicator.progress = 0
                                    binding.progressIndicator.visibility = View.VISIBLE
                                } else {
                                    binding.progressIndicator.isIndeterminate = false
                                }
                                binding.progressIndicator.progress = progress
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                binding.progressIndicator.visibility = View.GONE
                                setVisibleActionItem(0, true)

                                refresh()
                            }
                            WorkInfo.State.FAILED -> {
                                val message = info.outputData.getString( DownloadAllItemsWorker.MESSAGE_PARAM )

                                binding.progressIndicator.visibility = View.GONE
                                setVisibleActionItem(0, true)
                                showPopUpMessage( message )
                            }
                        }
                    }
                })

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

    fun setVisibleActionItem(i: Int, isVisible: Boolean) {
        val menuItem = binding.toolbarMainActivity.menu.getItem(i)
        menuItem.isVisible = isVisible
    }
}