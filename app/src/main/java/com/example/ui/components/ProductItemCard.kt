package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Toy

/**
 * Modern, accessible product card composable tailored for a 2-column or adaptive product grid.
 * Displays an image placeholder container, brand, product title, ratings, and price formatting.
 */
@Composable
fun ProductItemCard(
    name: String,
    price: String,
    modifier: Modifier = Modifier,
    originalPrice: String? = null,
    brand: String? = null,
    rating: Float? = null,
    reviewCount: Int? = null,
    badgeText: String? = null,
    placeholderEmoji: String = "🧸",
    accentColor: Color = MaterialTheme.colorScheme.primary,
    isWishlisted: Boolean = false,
    inStock: Boolean = true,
    onClick: () -> Unit = {},
    onAddToCart: (() -> Unit)? = null,
    onWishlistToggle: (() -> Unit)? = null
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                onClick = onClick,
                role = Role.Button
            )
            .semantics {
                contentDescription = "$name, price $price"
            }
            .testTag("product_card_${name.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // --- 1. Image Placeholder Container (1:1 Aspect Ratio) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.12f),
                                accentColor.copy(alpha = 0.04f)
                            )
                        )
                    )
                    .testTag("product_image_placeholder"),
                contentAlignment = Alignment.Center
            ) {
                // Image Placeholder Visual Elements
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Central playful emoji or icon
                    Text(
                        text = placeholderEmoji,
                        fontSize = 44.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = brand?.uppercase() ?: "WONDER TOY",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Top Badge (e.g., Best Seller, Same-Day Delivery, or Sale)
                if (badgeText != null) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 12.dp, topEnd = 0.dp, bottomStart = 0.dp),
                        color = accentColor,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .testTag("product_badge")
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Wishlist Toggle Icon Button (Top End, min 48x48dp touch target)
                if (onWishlistToggle != null) {
                    val heartTint by animateColorAsState(
                        targetValue = if (isWishlisted) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        animationSpec = tween(durationMillis = 200),
                        label = "heartTint"
                    )

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(36.dp)
                    ) {
                        IconButton(
                            onClick = onWishlistToggle,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("product_wishlist_button")
                                .semantics {
                                    role = Role.Button
                                    contentDescription = if (isWishlisted) "Remove from wishlist" else "Add to wishlist"
                                }
                        ) {
                            Icon(
                                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                tint = heartTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // --- 2. Product Details Section ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Brand Name
                if (!brand.isNullOrBlank()) {
                    Text(
                        text = brand.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Product Name (2 Lines Max)
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    modifier = Modifier.testTag("product_name")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating & Reviews row
                if (rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", rating),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (reviewCount != null && reviewCount > 0) {
                            Text(
                                text = " ($reviewCount)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // --- 3. Price & Add To Cart Action Row ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Price display (Current + Original discounted)
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = price,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("product_price")
                        )

                        if (!originalPrice.isNullOrBlank()) {
                            Text(
                                text = originalPrice,
                                style = MaterialTheme.typography.labelSmall,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Quick Add to Cart Button (Accessible touch target: 48x48dp outer target)
                    if (onAddToCart != null && inStock) {
                        FilledIconButton(
                            onClick = onAddToCart,
                            shape = RoundedCornerShape(10.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("product_add_to_cart_button")
                                .semantics {
                                    role = Role.Button
                                    contentDescription = "Add $name to cart"
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Convenient Toy model overload for ProductItemCard.
 * Directly integrates with the existing Toy data model.
 */
@Composable
fun ProductItemCard(
    toy: Toy,
    modifier: Modifier = Modifier,
    isWishlisted: Boolean = false,
    onClick: () -> Unit = {},
    onAddToCart: (() -> Unit)? = null,
    onWishlistToggle: (() -> Unit)? = null,
    isArabic: Boolean = false
) {
    val displayName = if (isArabic && toy.titleAr.isNotBlank()) toy.titleAr else toy.title
    val displayPrice = toy.localizedPrice(isArabic)
    val displayOriginalPrice = toy.localizedOriginalPrice(isArabic)
    val badge = when {
        toy.isBestSellerInQatar -> if (isArabic) "الأكثر مبيعاً" else "Best Seller"
        toy.isSameDayDeliveryQatar -> if (isArabic) "توصيل اليوم" else "Same-Day"
        else -> null
    }

    ProductItemCard(
        name = displayName,
        price = displayPrice,
        originalPrice = displayOriginalPrice,
        brand = toy.brand,
        rating = toy.rating,
        reviewCount = toy.reviewCount,
        badgeText = badge,
        placeholderEmoji = toy.ageGroup.iconEmoji,
        accentColor = Color(toy.colorAccentHex),
        isWishlisted = isWishlisted,
        inStock = toy.inStock,
        onClick = onClick,
        onAddToCart = onAddToCart,
        onWishlistToggle = onWishlistToggle,
        modifier = modifier
    )
}

/**
 * Reusable 2-column Product Grid layout component using ProductItemCard.
 */
@Composable
fun ProductGrid(
    toys: List<Toy>,
    modifier: Modifier = Modifier,
    wishlistIds: Set<String> = emptySet(),
    onToyClick: (Toy) -> Unit = {},
    onAddToCart: (Toy) -> Unit = {},
    onWishlistToggle: (Toy) -> Unit = {},
    isArabic: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = toys,
            key = { it.id }
        ) { toy ->
            ProductItemCard(
                toy = toy,
                isWishlisted = wishlistIds.contains(toy.id),
                onClick = { onToyClick(toy) },
                onAddToCart = { onAddToCart(toy) },
                onWishlistToggle = { onWishlistToggle(toy) },
                isArabic = isArabic
            )
        }
    }
}
