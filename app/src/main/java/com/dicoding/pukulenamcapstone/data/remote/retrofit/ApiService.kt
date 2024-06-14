package com.dicoding.pukulenamcapstone.data.remote.retrofit

import com.dicoding.pukulenamcapstone.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("{category}")
    suspend  fun getLatestNews(@Path("category") category: String): NewsResponse

}