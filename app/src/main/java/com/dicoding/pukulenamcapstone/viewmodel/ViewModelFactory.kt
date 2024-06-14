package com.dicoding.pukulenamcapstone.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.pukulenamcapstone.di.Injection
import com.dicoding.pukulenamcapstone.repo.NewsRepository

class ViewModelFactory private constructor(private val repo: NewsRepository): ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LatestNewsViewModel::class.java) -> {
                LatestNewsViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("ViewModel Unknown : " + modelClass.name)
        }
    }
}