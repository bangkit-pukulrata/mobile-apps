package com.dicoding.pukulenamcapstone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.pukulenamcapstone.data.remote.response.NewsResponse
import com.dicoding.pukulenamcapstone.repo.NewsRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class LatestNewsViewModel(private val repo: NewsRepository) : ViewModel() {

    private val _upRes = MutableLiveData<NewsResponse>()
    val upRes: LiveData<NewsResponse> get() = _upRes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getLatestNews(category: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res = repo.getLatestNews(category)
                _upRes.value = res
                _loading.value = false
                Log.d("LatestNewsViewModel", _upRes.value.toString())
            } catch (e: Exception) {
                _loading.value = false
                Log.e("LatestNewsViewModel", "Error fetching latest news", e)
            }
        }
    }
}