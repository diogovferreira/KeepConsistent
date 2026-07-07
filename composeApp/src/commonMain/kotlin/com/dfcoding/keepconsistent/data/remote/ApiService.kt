package com.dfcoding.optcg.data.remote

import com.dfcoding.optcg.data.model.dto.CardDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://www.optcgapi.com/api"

class OptcgApiService(private val client: HttpClient) {

    suspend fun searchCards(
        name: String? = null,
    ): List<CardDto> = client.get("$BASE_URL/sets/filtered/") {
        name?.let  { parameter("card_name", it) }
    }.body()

    suspend fun getCardById(cardId: String): CardDto =
        client.get("$BASE_URL/sets/card/$cardId/").body()
}