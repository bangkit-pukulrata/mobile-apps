package com.dicoding.pukulenamcapstone.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.pukulenamcapstone.data.remote.response.NewsResponse
import com.dicoding.pukulenamcapstone.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NewsRepository (private val services: ApiService) {
    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            services: ApiService
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(services)
            }.also { instance = it }
    }

    suspend fun getLatestNews(category: String): NewsResponse {
        val response = services.getLatestNews(category)
        val updatedPosts = response.data?.posts?.map { post ->
            post?.copy(category = category)
        } ?: emptyList()
        val updatedResponse = response.copy(data = response.data?.copy(posts = updatedPosts))
        Log.d("repo", updatedResponse.toString())
        return updatedResponse
    }
}