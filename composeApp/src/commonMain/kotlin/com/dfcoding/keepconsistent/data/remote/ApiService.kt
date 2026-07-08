package com.dfcoding.optcg.data.remote

import io.ktor.client.HttpClient

private const val BASE_URL = "https://www.optcgapi.com/api"

class OptcgApiService(private val client: HttpClient) {

/*    suspend fun searchCards(
        name: String? = null,
    ): List<CardDto> = client.get("$BASE_URL/sets/filtered/") {
        name?.let  { parameter("card_name", it) }
    }.body()

    suspend fun getCardById(cardId: String): CardDto =
        client.get("$BASE_URL/sets/card/$cardId/").body()*/
}