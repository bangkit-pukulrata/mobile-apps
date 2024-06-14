package com.dicoding.pukulenamcapstone.di

import android.content.Context
import com.dicoding.pukulenamcapstone.data.remote.retrofit.ApiConfig
import com.dicoding.pukulenamcapstone.repo.NewsRepository

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val serviceApi = ApiConfig.getApiService()

        return NewsRepository.getInstance(serviceApi)
    }
}