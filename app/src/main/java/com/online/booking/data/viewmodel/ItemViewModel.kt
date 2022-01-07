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
        if(items.isNotEmpty()) emit(Resource.localSuccess(data = items))
        try {
            val received = repository.getItems()
            //Compare two lists
            if( received.size != items.size
                && !received.zip(items).all { (x, y) -> x == y }
            ){
                itemDao.deleteAll()
                received.forEach { item ->  itemDao.insert( item ) }
                emit(Resource.remoteSuccess(data = received))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = items, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getItem( id: String? ) = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val item = itemDao.findById( id!! )
        item?.let {
            emit(Resource.localSuccess(data = item))
        }
        try {
            val received = repository.getItem( id )
            if(received != item){
                itemDao.update( received )
                emit(Resource.remoteSuccess(data = received))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = item, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun viewCountInc( id: String? ) = liveData(Dispatchers.IO) {
        try{
            val item = repository.viewCountInc( id!! )
            item?.let {
                emit(Resource.remoteSuccess(data=item))
            }
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

}