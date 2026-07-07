package com.dfcoding.optcg.data.mappers

import com.dfcoding.optcg.data.model.Card
import com.dfcoding.optcg.data.model.CardDetail
import com.dfcoding.optcg.data.model.dto.CardDto

fun CardDto.toCard(): Card = Card(
    id = cardId,
    name = name,
    imageUrl = imageUrl.orEmpty(),
    set = set.orEmpty(),
    color = color.orEmpty(),
    type = type.orEmpty(),
    rarity = rarity.toString(),
    marketPrice = marketPrice ?: 0.0,
    power = power.toString()
)

fun CardDto.toCardDetail(): CardDetail = CardDetail(
    id       = cardId,
    name     = name,
    imageUrl = imageUrl.orEmpty(),
    set      = set.orEmpty(),
    color    = color.orEmpty(),
    type     = type.orEmpty(),
    power    = power,
    cost     = cost,
    rarity   = rarity,
)