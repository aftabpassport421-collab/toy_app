package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey val toyId: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val toyId: String,
    val quantity: Int = 1,
    val giftWrap: Boolean = false,
    val giftNote: String = ""
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val dateTimestamp: Long,
    val status: String, // "CONFIRMED", "QUALITY_CHECK", "PACKED_WITH_SURPRISE", "OUT_FOR_DELIVERY", "DELIVERED"
    val stepIndex: Int, // 0 to 4
    val itemsSummary: String,
    val itemCount: Int,
    val totalAmount: Double,
    val trackingNumber: String,
    val estimatedDelivery: String,
    val deliveryAddress: String
)

@Entity(tableName = "rewards_profile")
data class RewardsProfileEntity(
    @PrimaryKey val id: Int = 1,
    val wonderCoins: Int = 250,
    val tier: String = "Silver Builder",
    val lastSpinDate: String = "",
    val unlockedBadges: String = "Welcome Explorer,Safety Star",
    val activeCoupons: String = "$5 Welcome Gift:WELCOME5"
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val recommendedToyId: String? = null
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val titleAr: String = "",
    val brand: String,
    val ageGroup: String, // e.g. "EXPLORER", "TODDLER", etc.
    val category: String, // e.g. "BUILDING", "DOLLS", etc.
    val price: Double,
    val originalPrice: Double? = null,
    val rating: Float = 5.0f,
    val reviewCount: Int = 0,
    val description: String = "",
    val descriptionAr: String = "",
    val batteriesRequired: String = "No Batteries Required",
    val isBestSellerInQatar: Boolean = false,
    val isSameDayDeliveryQatar: Boolean = true,
    val inStock: Boolean = true,
    val colorAccentHex: Long = 0xFFD32F2F
)
