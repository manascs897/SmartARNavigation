package com.example.smartar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("detect")
    suspend fun detectObstacle(
        @Body request: ImageRequest
    ): ObstacleResponse

    companion object {
        private const val BASE_URL = "http://192.168.1.100:5000/" 
        // Replace with your PC's local IP

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
