package com.featuremodule.data.floof

interface FoxRepository {
    suspend fun getRandomFox(): Result<FoxResponse>
}
