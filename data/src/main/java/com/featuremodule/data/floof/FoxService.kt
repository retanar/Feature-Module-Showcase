package com.featuremodule.data.floof

import retrofit2.http.GET

internal interface FoxService {
    @GET("floof")
    suspend fun getRandomFox(): FoxResponse
}
