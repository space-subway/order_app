package com.online.booking.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.online.booking.data.api.ApiHelper
import com.online.booking.data.api.RetrofitBuilder
import com.online.booking.data.repository.ItemRepository
import com.online.booking.utils.Resource
import kotlinx.coroutines.Dispatchers

class ItemViewModel : ViewModel() {

    private val repository: ItemRepository = ItemRepository( ApiHelper( RetrofitBuilder.apiService) )

    fun getItems() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getItems()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
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