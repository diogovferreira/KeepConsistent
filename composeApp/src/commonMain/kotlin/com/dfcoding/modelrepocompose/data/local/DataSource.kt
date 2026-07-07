package com.dfcoding.optcg.data.local

import com.dfcoding.optcg.data.model.Card
import com.dfcoding.optcg.database.TCG
import com.dfcoding.optcg.database.TcgDatabase
import kotlinx.datetime.Clock

class CardDataSource(private val database: TcgDatabase) {

    val queries = database.tcgQueries

    fun getAllCards() = queries.getAllCards().executeAsList().map { it.toCardModel() }

    fun getCardByImageId(imageId: String) = queries.getCardByImageId(imageId).executeAsOneOrNull()?.toCardModel()

    fun getCardsByName(name: String) = queries.getCardsByName(name).executeAsList().map { it.toCardModel() }

    fun getCardsBySet(set: String) = queries.getCardsBySet(set).executeAsList().map { it.toCardModel() }

    fun getCollectionCards() = queries.getCollectionCards().executeAsList().map { it.toCardModel() }

    fun getWishlistCards() = queries.getWishlistCards().executeAsList().map { it.toCardModel() }

    fun insertCard(card: Card) {
        println("DEBUG insertCard: id=${card.id} name=${card.name}")

        queries.insertCard(
            image_url = card.imageUrl,
            card_image_id = card.id,
            name = card.name,
            card_set = card.set,
            color = card.color,
            type = card.type,
            rarity = card.rarity,
            date = Clock.System.now().toEpochMilliseconds(),
            in_collection = 0L,
            in_wishlist = 0L,
            power = card.power,
            market_price = card.marketPrice
        )
    }

    fun addRemoveFromCollection(imageId: String, inCollection: Boolean) {
        println("DEBUG addRemoveFromCollection: imageId=$imageId inCollection=$inCollection")
        queries.addRemoveFromCollection(
            in_collection = if (inCollection) 1L else 0L,
            image_url = imageId
        )
    }

    fun addRemoveFromWishlist(imageId: String, inWishlist: Boolean) {
        queries.addRemoveFromWishlist(
            in_wishlist = if (inWishlist) 1L else 0L,
            image_url = imageId
        )
    }

    fun isCardInWishlist(imageId: String) = (queries.isCardInWishlist(imageId).executeAsOneOrNull() ?: 0L) == 1L

    fun isCardInCollection(imageId: String) = (queries.isCardInCollection(imageId).executeAsOneOrNull() ?: 0L) == 1L



}

private fun TCG.toCardModel() =
    Card(
        id = card_image_id,
        name = name,
        imageUrl = image_url,
        set = card_set.toString(),
        color = color,
        type = type,
        rarity = rarity,
        inCollection = in_collection == 1L,
        inWhishlist = in_wishlist == 1L,
        power = power,
        marketPrice = market_price
    )
