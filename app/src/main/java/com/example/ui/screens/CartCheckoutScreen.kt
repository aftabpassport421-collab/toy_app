package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QatarStoreDirectory
import com.example.data.repository.ToyStoreRepository
import com.example.ui.viewmodel.StoreNavScreen
import com.example.ui.viewmodel.ToyStoreViewModel

@Composable
fun CartCheckoutScreen(
    viewModel: ToyStoreViewModel,
    cartItems: List<ToyStoreRepository.CartItemWithToy>,
    isParentUnlocked: Boolean,
    isKidMode: Boolean
) {
    val isArabic by viewModel.isArabic.collectAsState()
    val fulfillmentType by viewModel.fulfillmentType.collectAsState()
    val selectedStore by viewModel.selectedStore.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()

    var deliveryAddress by remember { mutableStateOf("West Bay Lagoon, Villa 14, Doha, Qatar") }
    var couponCode by remember { mutableStateOf("") }

    val rawSubtotal = cartItems.sumOf { it.toy.price * it.entity.quantity }
    val freeShippingThreshold = 150.0 // QAR
    val shippingCost = if (fulfillmentType == "CLICK_AND_COLLECT" || rawSubtotal >= freeShippingThreshold || rawSubtotal == 0.0) 0.0 else 20.0 // QAR
    val discount = if (couponCode.contains("KIDDY10", ignoreCase = true) || couponCode.contains("WELCOME", ignoreCase = true)) 25.0 else 0.0
    val totalAmount = (rawSubtotal + shippingCost - discount).coerceAtLeast(0.0)

    val qatarAreas = listOf(
        "West Bay & Diplomatic Area",
        "The Pearl Qatar & Lusail",
        "Al Waab & Aspire Zone",
        "Al Rayyan & Education City",
        "Al Sadd & Fereej Bin Mahmoud",
        "Al Wakrah & Barwa Village"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("cart_checkout_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isArabic) "حقيبة ألعاب كيدي زون 🎒" else "Kiddy Zone Toy Bag 🎒",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = if (isArabic) "${cartItems.sumOf { it.entity.quantity }} منتج جاهز للشراء والدفع الآمن"
                        else "${cartItems.sumOf { it.entity.quantity }} item(s) ready for checkout",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isParentUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isArabic) "تصريح الوالدين مؤكد" else "Parent Authorized",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }
        }

        if (cartItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🧸", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isArabic) "حقيبتك فارغة حالياً" else "Your Toy Bag is Empty",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isArabic) "استكشف ألعاب كيدي زون الرائعة وأضف هدايا لأطفالك!" else "Explore toys from LEGO, Barbie, Disney & more!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.navigateTo(StoreNavScreen.CATALOG) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(if (isArabic) "تسوق الآن" else "Explore Catalog")
                        }
                    }
                }
            }
        } else {
            // Cart Items List
            items(cartItems, key = { it.entity.toyId }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cart_item_${item.toy.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Emoji Visual
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(item.toy.colorAccentHex).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.toy.category.iconEmoji, fontSize = 28.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.toy.brand,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isArabic) item.toy.titleAr else item.toy.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.toy.formattedPrice,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Remove Button
                            IconButton(
                                onClick = { viewModel.removeCartItem(item.toy.id) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("cart_remove_${item.toy.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove item",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quantity & Subtotal Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.toy.id, item.entity.quantity - 1) },
                                        modifier = Modifier.testTag("cart_minus_${item.toy.id}")
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease quantity")
                                    }
                                }

                                Text(
                                    text = "${item.entity.quantity}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )

                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.toy.id, item.entity.quantity + 1) },
                                        modifier = Modifier.testTag("cart_plus_${item.toy.id}")
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase quantity")
                                    }
                                }
                            }

                            Text(
                                text = "${String.format("%.2f", item.toy.price * item.entity.quantity)} QAR",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (item.entity.giftWrap) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFF3E0)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CardGiftcard,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isArabic) "🎁 تغليف مجاني فاخر: \"${item.entity.giftNote.ifBlank { "هدية خاصة من كيدي زون" }}\""
                                        else "🎁 Free Gift Wrapped: \"${item.entity.giftNote.ifBlank { "Special Kiddy Zone Gift" }}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF5D4037),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Qatar Fulfillment Options (Delivery vs Store Pickup)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isArabic) "طريقة الاستلام والتوصيل في قطر 🇶🇦" else "Qatar Fulfillment Method 🇶🇦",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Switch: Delivery vs Store Pickup
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Option 1: Doorstep Delivery
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (fulfillmentType == "DELIVERY") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setFulfillmentType("DELIVERY") }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocalShipping,
                                            contentDescription = null,
                                            tint = if (fulfillmentType == "DELIVERY") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isArabic) "توصيل للباب" else "Doorstep",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isArabic) "توصيل سريع بنفس اليوم" else "Same-Day Express Doha",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Option 2: Click & Collect
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (fulfillmentType == "CLICK_AND_COLLECT") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setFulfillmentType("CLICK_AND_COLLECT") }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Storefront,
                                            contentDescription = null,
                                            tint = if (fulfillmentType == "CLICK_AND_COLLECT") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isArabic) "استلام من الفرع" else "Click & Collect",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isArabic) "مجاني في ٥ مجمعات" else "Free at 5 Qatar Malls",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (fulfillmentType == "CLICK_AND_COLLECT") {
                            Text(
                                text = if (isArabic) "اختر فرع كيدي زون للاستلام:" else "Choose Kiddy Zone Branch:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(QatarStoreDirectory.stores) { store ->
                                    val isSelected = selectedStore.id == store.id
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.clickable { viewModel.selectQatarStore(store) }
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                            Text(
                                                text = if (isArabic) store.nameAr else store.name,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = if (isArabic) store.nameAr else store.mall,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Doorstep Delivery Area & Address
                            Text(
                                text = if (isArabic) "المنطقة في قطر:" else "Delivery Area in Qatar:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(qatarAreas) { area ->
                                    val isSelected = deliveryAddress.contains(area)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.clickable {
                                            viewModel.setDeliveryArea(area)
                                            deliveryAddress = "$area, Villa 12, Doha, Qatar"
                                        }
                                    ) {
                                        Text(
                                            text = area,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = deliveryAddress,
                                onValueChange = { deliveryAddress = it },
                                label = { Text(if (isArabic) "عنوان التوصيل في قطر" else "Delivery Address in Qatar") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("delivery_address_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Coupon Code input
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = couponCode,
                            onValueChange = { couponCode = it.uppercase() },
                            placeholder = { Text(if (isArabic) "رمز الخصم (مثل KIDDY10)" else "Coupon Code (e.g. KIDDY10)") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("coupon_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { /* reactive apply */ },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(if (isArabic) "تطبيق" else "Apply")
                        }
                    }
                }
            }

            // Order Price Breakdown Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isArabic) "ملخص الفاتورة" else "Order Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (isArabic) "المجموع الفرعي" else "Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${String.format("%.2f", rawSubtotal)} QAR", fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (isArabic) "تغليف الهدايا الفاخر" else "Signature Gift Wrapping", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(if (isArabic) "مجاني 🎁" else "FREE 🎁", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (fulfillmentType == "CLICK_AND_COLLECT") {
                                    if (isArabic) "الاستلام من الفرع" else "Store Click & Collect"
                                } else {
                                    if (isArabic) "توصيل الدوحة السريع" else "Express Doha Delivery"
                                },
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                if (shippingCost == 0.0) {
                                    if (isArabic) "مجاني" else "FREE"
                                } else {
                                    "${String.format("%.2f", shippingCost)} QAR"
                                },
                                color = if (shippingCost == 0.0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (discount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(if (isArabic) "خصم كيدي زون (KIDDY10)" else "Kiddy Zone Discount", color = MaterialTheme.colorScheme.primary)
                                Text("-${String.format("%.2f", discount)} QAR", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabic) "الإجمالي المستحق" else "Total Due",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${String.format("%.2f", totalAmount)} QAR",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // SECURE PARENTAL PAYMENT OPTIONS (Qatar Gateways)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parental_payment_options_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isParentUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Parental Security",
                                    tint = if (isParentUnlocked) Color(0xFF00C853) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isArabic) "بوابة دفع أولياء الأمور الآمنة 🛡️" else "Secure Parental Payment 🛡️",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isParentUnlocked) {
                                            if (isArabic) "تم التحقق من ولي الأمر" else "Parental Verification Active"
                                        } else {
                                            if (isArabic) "مطلوب تصريح الوالدين لإتمام الشراء" else "Parent verification required to purchase"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (!isParentUnlocked) {
                                Button(
                                    onClick = { viewModel.triggerParentalGate("Parental Payment Authorization") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.height(48.dp).testTag("unlock_parent_pay_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isArabic) "تصريح" else "Authorize")
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { viewModel.lockParentMode() },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text(if (isArabic) "قفل" else "Lock")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!isParentUnlocked) {
                            // Locked notice for kids
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (isArabic) "🛡️ نظام حماية الطفل من الشراء العشوائي" else "🛡️ Child-Safe Checkout Protection",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isArabic) "لحماية الأطفال، يجب على أحد الوالدين إدخال رمز PIN أو حل مسألة حسابية قبل خصم أي مبالغ أو استخدام البطاقات البنكية."
                                        else "To protect children, an adult must enter the Parent PIN or solve a math challenge before bank cards can be charged.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        } else {
                            // Unlocked Payment Options in Qatar
                            Text(
                                text = if (isArabic) "طرق الدفع المعتمدة في قطر:" else "Select Qatar Payment Method:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // 1. Qatar Debit Card (NAPS)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setPaymentMethod("NAPS_DEBIT") }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == "NAPS_DEBIT",
                                    onClick = { viewModel.setPaymentMethod("NAPS_DEBIT") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = Color(0xFF8A1538)) // Qatar Maroon
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isArabic) "بطاقة الخصم المباشر (NAPS / QNB / CBQ)" else "Qatar National Debit Card (NAPS / QNB / CBQ)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isArabic) "دفع آمن معتمد من مصرف قطر المركزي" else "Instant, secure debit verified by Qatar Central Bank",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // 2. Credit Card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setPaymentMethod("CREDIT_CARD") }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == "CREDIT_CARD",
                                    onClick = { viewModel.setPaymentMethod("CREDIT_CARD") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isArabic) "بطاقة ائتمان (Visa / MasterCard)" else "Credit Card (Visa / MasterCard)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "3D Secure 2.0 OTP Authentication",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // 3. Apple Pay / Google Pay
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setPaymentMethod("DIGITAL_WALLET") }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == "DIGITAL_WALLET",
                                    onClick = { viewModel.setPaymentMethod("DIGITAL_WALLET") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "📱", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isArabic) "محفظة رقمية (Apple Pay / Google Pay)" else "Digital Wallet (Apple Pay / Google Pay)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Biometric FaceID / TouchID authorization",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // 4. Cash on Delivery / Pay at Store
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setPaymentMethod("CASH_ON_DELIVERY") }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == "CASH_ON_DELIVERY",
                                    onClick = { viewModel.setPaymentMethod("CASH_ON_DELIVERY") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.Payments, contentDescription = null, tint = Color(0xFF00C853))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isArabic) "الدفع عند الاستلام / في المتجر" else "Cash on Delivery / Pay at Store Counter",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isArabic) "ادفع نقداً أو بالبطاقة عند الاستلام في قطر" else "Pay in cash or POS card upon delivery or pickup",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Checkout Action Button
            item {
                Button(
                    onClick = {
                        if (!isParentUnlocked) {
                            viewModel.triggerParentalGate("Parental Payment Authorization")
                        } else {
                            viewModel.placeOrder(deliveryAddress, couponCode)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("checkout_submit_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isParentUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = if (isParentUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isParentUnlocked) {
                            if (isArabic) "تأكيد الطلب (${String.format("%.2f", totalAmount)} QAR)" else "Place Secure Order (${String.format("%.2f", totalAmount)} QAR)"
                        } else {
                            if (isArabic) "تصريح الوالدين والدفع (${String.format("%.2f", totalAmount)} QAR)" else "Authorize & Checkout (${String.format("%.2f", totalAmount)} QAR)"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
