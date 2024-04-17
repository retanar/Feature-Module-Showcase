package com.featuremodule.data.floof

import javax.inject.Inject

internal class FoxRepositoryImpl @Inject constructor(
    private val service: FoxService,
) : FoxRepository {
    override suspend fun getRandomFox() = runCatching {
        service.getRandomFox()
    }
}
