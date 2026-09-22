package com.example.data.repository

import com.example.data.local.CartItemEntity
import com.example.data.local.ChatMessageEntity
import com.example.data.local.OrderEntity
import com.example.data.local.RewardsProfileEntity
import com.example.data.local.ToyDao
import com.example.data.local.WishlistEntity
import com.example.data.model.AgeGroup
import com.example.data.model.Toy
import com.example.data.model.ToyCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ToyStoreRepository(private val toyDao: ToyDao) {

    // Toys Catalog
    val allToys: List<Toy> = ToyCatalog.toys

    fun getToyById(id: String): Toy? = allToys.find { it.id == id }

    fun filterToys(
        query: String,
        ageGroup: AgeGroup,
        category: ToyCategory,
        brand: String = "All Brands",
        maxPrice: Double?,
        minRating: Float?,
        bpaFreeOnly: Boolean,
        stemCertifiedOnly: Boolean,
        batteriesNotRequiredOnly: Boolean,
        sortBy: String // "POPULAR", "PRICE_LOW", "PRICE_HIGH", "RATING"
    ): List<Toy> {
        return allToys.filter { toy ->
            val matchesQuery = query.isBlank() ||
                    toy.title.contains(query, ignoreCase = true) ||
                    toy.titleAr.contains(query, ignoreCase = true) ||
                    toy.brand.contains(query, ignoreCase = true) ||
                    toy.description.contains(query, ignoreCase = true) ||
                    toy.category.displayName.contains(query, ignoreCase = true)

            val matchesAge = ageGroup == AgeGroup.ALL || toy.ageGroup == ageGroup
            val matchesCategory = category == ToyCategory.ALL || toy.category == category
            val matchesBrand = brand == "All Brands" || brand.isBlank() || toy.brand.equals(brand, ignoreCase = true)
            val matchesPrice = maxPrice == null || toy.price <= maxPrice
            val matchesRating = minRating == null || toy.rating >= minRating
            val matchesBpa = !bpaFreeOnly || toy.safetyCertifications.any { it.contains("BPA Free", ignoreCase = true) || it.contains("Non-Toxic", ignoreCase = true) || it.contains("GSO", ignoreCase = true) }
            val matchesStem = !stemCertifiedOnly || toy.awards.any { it.contains("STEM", ignoreCase = true) } || toy.category == ToyCategory.STEM
            val matchesNoBatteries = !batteriesNotRequiredOnly || toy.batteriesRequired.contains("No Batteries", ignoreCase = true)

            matchesQuery && matchesAge && matchesCategory && matchesBrand && matchesPrice && matchesRating && matchesBpa && matchesStem && matchesNoBatteries
        }.let { list ->
            when (sortBy) {
                "PRICE_LOW" -> list.sortedBy { it.price }
                "PRICE_HIGH" -> list.sortedByDescending { it.price }
                "RATING" -> list.sortedByDescending { it.rating }
                else -> list.sortedByDescending { it.reviewCount }
            }
        }
    }

    // Wishlist
    val wishlistToyIds: Flow<Set<String>> = toyDao.getWishlistItems().map { list ->
        list.map { it.toyId }.toSet()
    }

    val wishlistToys: Flow<List<Toy>> = toyDao.getWishlistItems().map { list ->
        list.mapNotNull { item -> getToyById(item.toyId) }
    }

    suspend fun toggleWishlist(toyId: String) {
        val current = wishlistToyIds.firstOrNull() ?: emptySet()
        if (current.contains(toyId)) {
            toyDao.removeFromWishlist(toyId)
        } else {
            toyDao.addToWishlist(WishlistEntity(toyId = toyId))
        }
    }

    suspend fun removeFromWishlist(toyId: String) {
        toyDao.removeFromWishlist(toyId)
    }

    // Cart
    data class CartItemWithToy(
        val entity: CartItemEntity,
        val toy: Toy
    )

    val cartItems: Flow<List<CartItemWithToy>> = toyDao.getCartItems().map { list ->
        list.mapNotNull { entity ->
            val toy = getToyById(entity.toyId)
            toy?.let { CartItemWithToy(entity, it) }
        }
    }

    suspend fun addToCart(toyId: String, quantity: Int = 1, giftWrap: Boolean = false, note: String = "") {
        val existing = toyDao.getCartItems().firstOrNull()?.find { it.toyId == toyId }
        val newQty = (existing?.quantity ?: 0) + quantity
        toyDao.upsertCartItem(
            CartItemEntity(
                toyId = toyId,
                quantity = newQty,
                giftWrap = giftWrap || (existing?.giftWrap ?: false),
                giftNote = if (note.isNotBlank()) note else (existing?.giftNote ?: "")
            )
        )
    }

    suspend fun updateCartQuantity(toyId: String, newQty: Int) {
        if (newQty <= 0) {
            toyDao.removeCartItem(toyId)
        } else {
            val existing = toyDao.getCartItems().firstOrNull()?.find { it.toyId == toyId }
            toyDao.upsertCartItem(
                CartItemEntity(
                    toyId = toyId,
                    quantity = newQty,
                    giftWrap = existing?.giftWrap ?: false,
                    giftNote = existing?.giftNote ?: ""
                )
            )
        }
    }

    suspend fun updateGiftOptions(toyId: String, giftWrap: Boolean, note: String) {
        val existing = toyDao.getCartItems().firstOrNull()?.find { it.toyId == toyId } ?: return
        toyDao.upsertCartItem(existing.copy(giftWrap = giftWrap, giftNote = note))
    }

    suspend fun removeCartItem(toyId: String) {
        toyDao.removeCartItem(toyId)
    }

    suspend fun clearCart() {
        toyDao.clearCart()
    }

    // Orders & Tracking
    val allOrders: Flow<List<OrderEntity>> = toyDao.getAllOrders()

    fun getOrder(orderId: String): Flow<OrderEntity?> = toyDao.getOrderById(orderId)

    suspend fun placeOrder(
        items: List<CartItemWithToy>,
        totalAmount: Double,
        deliveryAddress: String,
        discountApplied: Double = 0.0
    ): OrderEntity {
        val orderId = "TW-${(100000..999999).random()}"
        val trackingNumber = "WONDER-EXP-${(1000..9999).random()}"
        val summary = items.joinToString(", ") { "${it.entity.quantity}x ${it.toy.title}" }
        val finalAmount = (totalAmount - discountApplied).coerceAtLeast(0.0)

        val newOrder = OrderEntity(
            orderId = orderId,
            dateTimestamp = System.currentTimeMillis(),
            status = "CONFIRMED",
            stepIndex = 0,
            itemsSummary = summary,
            itemCount = items.sumOf { it.entity.quantity },
            totalAmount = finalAmount,
            trackingNumber = trackingNumber,
            estimatedDelivery = "2-3 Days via WonderExpress",
            deliveryAddress = deliveryAddress
        )

        toyDao.insertOrder(newOrder)
        toyDao.clearCart()

        // Award rewards points: 10 WonderCoins per dollar spent
        val pointsEarned = (finalAmount * 10).toInt()
        addWonderCoins(pointsEarned, "Purchase: $orderId")

        return newOrder
    }

    suspend fun advanceOrderTrackingStep(orderId: String) {
        val currentOrders = toyDao.getAllOrders().firstOrNull() ?: return
        val target = currentOrders.find { it.orderId == orderId } ?: return
        val nextStep = (target.stepIndex + 1).coerceAtMost(4)
        val nextStatus = when (nextStep) {
            0 -> "CONFIRMED"
            1 -> "QUALITY_CHECK"
            2 -> "PACKED_WITH_SURPRISE"
            3 -> "OUT_FOR_DELIVERY"
            else -> "DELIVERED"
        }
        toyDao.updateOrder(target.copy(stepIndex = nextStep, status = nextStatus))
    }

    // Seed initial demo orders if empty
    suspend fun ensureDemoOrdersExist() {
        val existing = toyDao.getAllOrders().firstOrNull() ?: emptyList()
        if (existing.isEmpty()) {
            val demoOrder1 = OrderEntity(
                orderId = "KZ-DOHA-9941",
                dateTimestamp = System.currentTimeMillis() - 86400000L * 1,
                status = "OUT_FOR_DELIVERY",
                stepIndex = 3,
                itemsSummary = "1x LEGO City Space Explorer, 1x Barbie Dreamhouse Playset",
                itemCount = 2,
                totalAmount = 358.0,
                trackingNumber = "KZ-QATAR-EXPRESS-882",
                estimatedDelivery = "Today by 4:30 PM (Doha Same-Day Delivery)",
                deliveryAddress = "Al Dafna, West Bay Lagoon Villa 42, Doha, Qatar"
            )
            val demoOrder2 = OrderEntity(
                orderId = "KZ-PICKUP-4182",
                dateTimestamp = System.currentTimeMillis() - 86400000L * 4,
                status = "DELIVERED",
                stepIndex = 4,
                itemsSummary = "1x Monopoly Qatar Landmark Edition, 1x Hot Wheels T-Rex Transporter",
                itemCount = 2,
                totalAmount = 324.0,
                trackingNumber = "KZ-STORE-MOQ-110",
                estimatedDelivery = "Collected at Kiddy Zone - Mall of Qatar Store",
                deliveryAddress = "Store Pickup: Mall of Qatar, Ground Floor near Oasis"
            )
            toyDao.insertOrder(demoOrder1)
            toyDao.insertOrder(demoOrder2)
        }
    }

    // Rewards Profile
    val rewardsProfile: Flow<RewardsProfileEntity> = toyDao.getRewardsProfile().map { profile ->
        profile ?: RewardsProfileEntity().also {
            toyDao.saveRewardsProfile(it)
        }
    }

    suspend fun addWonderCoins(amount: Int, reason: String = "") {
        val current = rewardsProfile.firstOrNull() ?: RewardsProfileEntity()
        val newCoins = current.wonderCoins + amount
        val newTier = when {
            newCoins >= 1000 -> "Gold Toy Master 👑"
            newCoins >= 400 -> "Silver Builder 🌟"
            else -> "Bronze Explorer 🚀"
        }
        toyDao.saveRewardsProfile(current.copy(wonderCoins = newCoins, tier = newTier))
    }

    suspend fun spinRewardsWheel(): Int {
        val options = listOf(50, 100, 150, 200, 300, 500)
        val won = options.random()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val current = rewardsProfile.firstOrNull() ?: RewardsProfileEntity()
        val updatedCoins = current.wonderCoins + won
        toyDao.saveRewardsProfile(
            current.copy(
                wonderCoins = updatedCoins,
                lastSpinDate = today
            )
        )
        return won
    }

    suspend fun redeemVoucher(costInCoins: Int, voucherTitle: String, voucherCode: String): Boolean {
        val current = rewardsProfile.firstOrNull() ?: RewardsProfileEntity()
        if (current.wonderCoins < costInCoins) return false
        val newCoins = current.wonderCoins - costInCoins
        val updatedCoupons = if (current.activeCoupons.isBlank()) "$voucherTitle:$voucherCode"
        else "${current.activeCoupons};$voucherTitle:$voucherCode"
        toyDao.saveRewardsProfile(
            current.copy(wonderCoins = newCoins, activeCoupons = updatedCoupons)
        )
        return true
    }

    // Support Chat
    val chatMessages: Flow<List<ChatMessageEntity>> = toyDao.getChatMessages()

    suspend fun initializeChatWelcomeIfEmpty() {
        val current = toyDao.getChatMessages().firstOrNull() ?: emptyList()
        if (current.isEmpty()) {
            toyDao.insertChatMessage(
                ChatMessageEntity(
                    message = "Marhaba & Welcome to Kiddy Zone Qatar! 🇶🇦🧸 I'm your Kiddy Buddy Assistant. Looking for birthday gifts, store pickup in Doha (Mall of Qatar, Doha Festival City, Place Vendôme, Hyatt Plaza), or same-day delivery across Qatar? How may I help you today?",
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun sendUserChatMessage(userQuery: String) {
        val userMsg = ChatMessageEntity(
            message = userQuery,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )
        toyDao.insertChatMessage(userMsg)

        // Generate intelligent contextual response
        val (botReply, recommendedToyId) = generateSupportResponse(userQuery)
        val botMsg = ChatMessageEntity(
            message = botReply,
            isUser = false,
            timestamp = System.currentTimeMillis() + 500L,
            recommendedToyId = recommendedToyId
        )
        toyDao.insertChatMessage(botMsg)
    }

    private fun generateSupportResponse(query: String): Pair<String, String?> {
        val lower = query.lowercase(Locale.ROOT)
        return when {
            lower.contains("toddler") || lower.contains("baby") || lower.contains("0") || lower.contains("1") || lower.contains("2") -> {
                Pair(
                    "For little ones (ages 0-2), we recommend the Fisher-Price Linkimals Smooth Moves Sloth! It's GSO-certified, infant-safe, and teaches early melodies and colors.",
                    "kz-fisherprice-10"
                )
            }
            lower.contains("lego") || lower.contains("building") || lower.contains("blocks") -> {
                Pair(
                    "Kiddy Zone is an authorized LEGO retailer in Qatar! Check out our best-selling LEGO City Space Explorer Rover & Research Station (199 QAR).",
                    "kz-lego-1"
                )
            }
            lower.contains("barbie") || lower.contains("doll") || lower.contains("girl") -> {
                Pair(
                    "Our Barbie Color Reveal Pastel Dreamhouse and Disney Frozen Elsa Magical Ice Palace are huge favorites in Doha right now! Both include complimentary gift wrapping.",
                    "kz-barbie-2"
                )
            }
            lower.contains("hot wheels") || lower.contains("car") || lower.contains("race") -> {
                Pair(
                    "Hot Wheels Ultimate T-Rex Transporter is a top hit! It transforms into a 3-foot dual racetrack and holds over 20 cars.",
                    "kz-hotwheels-3"
                )
            }
            lower.contains("stem") || lower.contains("robot") || lower.contains("code") -> {
                Pair(
                    "Check out the Kiddy Tech Programmable Smart AI Coding Robot! It features English & Arabic voice responses, ultrasonic sensors, and visual Scratch-style coding.",
                    "kz-stem-9"
                )
            }
            lower.contains("store") || lower.contains("branch") || lower.contains("mall") || lower.contains("pickup") -> {
                Pair(
                    "We have branches across Qatar: Mall of Qatar (Ground Floor near Oasis), Doha Festival City (1st Floor Kids Zone), Place Vendôme (Lusail Level 1), Hyatt Plaza (Gate 3), and Landmark Mall. You can select Click & Collect at checkout for 1-hour in-store pickup!",
                    null
                )
            }
            lower.contains("delivery") || lower.contains("shipping") || lower.contains("qatar") || lower.contains("doha") -> {
                Pair(
                    "We offer Same-Day Express Delivery across Doha and all Qatar municipalities (West Bay, Lusail, Al Rayyan, Al Wakrah, Al Khor). Orders over 150 QAR enjoy Free Delivery!",
                    null
                )
            }
            lower.contains("gift") || lower.contains("wrap") || lower.contains("birthday") || lower.contains("eid") -> {
                Pair(
                    "Kiddy Zone offers Signature Free Gift Wrapping! You can select Classic Kiddy Red Ribbon, Eid Mubarak, or Birthday celebration themes, along with a personalized handwritten greeting card at checkout.",
                    null
                )
            }
            lower.contains("points") || lower.contains("club") || lower.contains("reward") -> {
                Pair(
                    "Every 1 QAR spent earns you 1 Kiddy Club Point! Spin our Daily Rewards Wheel in the 'Rewards' tab to win up to 500 bonus points and redeem 10 QAR to 30 QAR discount vouchers.",
                    null
                )
            }
            else -> {
                Pair(
                    "Welcome to Kiddy Zone Qatar! We carry 100% original, licensed brands like LEGO, Barbie, Disney, Marvel, and Nerf with same-day Qatar delivery. Can I help find something for a specific age or brand?",
                    "kz-monopoly-11"
                )
            }
        }
    }
}
