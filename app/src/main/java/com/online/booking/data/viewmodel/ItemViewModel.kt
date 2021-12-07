package com.online.booking.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.online.booking.data.api.ApiHelper
import com.online.booking.data.api.RetrofitBuilder
import com.online.booking.data.db.AppDatabase
import com.online.booking.data.db.ItemDao
import com.online.booking.data.repository.ItemRepository
import com.online.booking.utils.Resource
import kotlinx.coroutines.Dispatchers

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository = ItemRepository( ApiHelper( RetrofitBuilder.apiService) )
    private val itemDao: ItemDao = AppDatabase.getDatabase(application).itemsDao()

    fun getItems() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val items = itemDao.allItems
        if(items.isNotEmpty()) emit(Resource.success(data = items))
        try {
            val received = repository.getItems()
            itemDao.deleteAll()
            received.forEach { item ->  itemDao.insert( item ) }
            emit(Resource.success(data = received))
        } catch (exception: Exception) {
            if(items.isEmpty()){
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } else {
                emit(Resource.error(data = items, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getItem( id: String? ) = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getItem( id )))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}