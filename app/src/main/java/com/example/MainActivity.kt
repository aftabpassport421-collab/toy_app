package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ParentalGateDialog
import com.example.ui.components.SearchAndFilterSheet
import com.example.ui.screens.CartCheckoutScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.ChatSupportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.ToyWonderTheme
import com.example.ui.viewmodel.StoreNavScreen
import com.example.ui.viewmodel.ToyStoreViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ToyStoreViewModel = viewModel()
            ToyWonderApp(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToyWonderApp(viewModel: ToyStoreViewModel) {
    val themeMode by viewModel.themeMode.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedToy by viewModel.selectedToy.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val filteredToys by viewModel.filteredToys.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val wishlistToys by viewModel.wishlistToys.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val selectedOrderId by viewModel.selectedOrderId.collectAsState()
    val rewardsProfile by viewModel.rewardsProfile.collectAsState()
    val lastSpinResult by viewModel.lastSpinResult.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isKidMode by viewModel.isKidMode.collectAsState()
    val isParentUnlocked by viewModel.isParentUnlocked.collectAsState()
    val isArabic by viewModel.isArabic.collectAsState()
    val showParentalGate by viewModel.showParentalGate.collectAsState()
    val parentalGateReason by viewModel.parentalGateReason.collectAsState()
    val hapticFeedbackEnabled by viewModel.hapticFeedbackEnabled.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // Handle system back navigation
    BackHandler(enabled = currentScreen != StoreNavScreen.HOME) {
        viewModel.navigateBack()
    }

    ToyWonderTheme(themeMode = themeMode) {
        // Parental Gate Security Dialog
        if (showParentalGate) {
            ParentalGateDialog(
                purposeTitle = parentalGateReason,
                onDismiss = { viewModel.dismissParentalGate() },
                onSuccess = { viewModel.onParentalGateSuccess() },
                verifyPin = { pin -> viewModel.verifyParentPin(pin) }
            )
        }

        // Search & Robust Filter Modal Sheet
        if (showFilterSheet) {
            SearchAndFilterSheet(
                filterState = filterState,
                sheetState = filterSheetState,
                onDismiss = {
                    coroutineScope.launch { filterSheetState.hide() }
                    showFilterSheet = false
                },
                onCategorySelect = { viewModel.setCategoryFilter(it) },
                onBrandSelect = { viewModel.setBrandFilter(it) },
                onPriceSelect = { viewModel.setMaxPriceFilter(it) },
                onRatingSelect = { viewModel.setMinRatingFilter(it) },
                onToggleBpa = { viewModel.toggleBpaFreeFilter() },
                onToggleStem = { viewModel.toggleStemCertifiedFilter() },
                onToggleNoBatteries = { viewModel.toggleBatteriesNotRequiredFilter() },
                onSortSelect = { viewModel.setSortBy(it) },
                onReset = { viewModel.resetFilters() }
            )
        }

        CompositionLocalProvider(LocalLayoutDirection provides (if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr)) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    // Hide main top bar on detail screen since detail has its own custom bar
                    if (currentScreen != StoreNavScreen.DETAIL && currentScreen != StoreNavScreen.TOY_DETAIL) {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = when (currentScreen) {
                                            StoreNavScreen.HOME -> if (isArabic) "وندر توي قطر 🧸" else "Wonder Toy Qatar 🧸"
                                            StoreNavScreen.CATALOG -> if (isArabic) "كتالوج الألعاب 🎯" else "Toy Catalog 🎯"
                                            StoreNavScreen.WISHLIST -> if (isArabic) "قائمة الأمنيات 💖" else "Wishlist 💖"
                                            StoreNavScreen.CART -> if (isArabic) "حقيبة التسوق 🎒" else "Toy Bag 🎒"
                                            StoreNavScreen.ORDERS, StoreNavScreen.ORDER_TRACKING, StoreNavScreen.ORDER_DETAIL -> if (isArabic) "تتبع الطلبات 🚚" else "Order Tracking 🚚"
                                            StoreNavScreen.REWARDS -> if (isArabic) "نقاط وندر كلوب ⭐" else "Wonder Points Club ⭐"
                                            StoreNavScreen.CHAT_SUPPORT -> if (isArabic) "مساعد وندر 🤖" else "Wonder Buddy Support 🤖"
                                            StoreNavScreen.ACCESSIBILITY_SETTINGS, StoreNavScreen.SETTINGS -> if (isArabic) "الإعدادات والأمان ⚙️" else "Settings & Safety ⚙️"
                                            StoreNavScreen.TOY_DETAIL, StoreNavScreen.DETAIL -> ""
                                        },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        navigationIcon = {
                            if (currentScreen != StoreNavScreen.HOME && currentScreen != StoreNavScreen.CATALOG) {
                                IconButton(
                                    onClick = { viewModel.navigateBack() },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        },
                        actions = {
                            // Quick Mode indicator toggle
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isKidMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier
                                    .clickable { viewModel.toggleKidMode() }
                                    .padding(end = 4.dp)
                                    .testTag("top_app_bar_mode_badge")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isKidMode) Icons.Default.Security else Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = if (isKidMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = if (isKidMode) "Kid" else "Parent",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Order Tracking Icon button
                            val isOrdersScreen = currentScreen == StoreNavScreen.ORDER_TRACKING || currentScreen == StoreNavScreen.ORDERS || currentScreen == StoreNavScreen.ORDER_DETAIL
                            IconButton(
                                onClick = { viewModel.navigateTo(StoreNavScreen.ORDER_TRACKING) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("top_bar_orders_btn")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (orders.isNotEmpty()) {
                                            Badge { Text("${orders.size}") }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalShipping,
                                        contentDescription = "Orders and Tracking",
                                        tint = if (isOrdersScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // Settings Icon button
                            val isSettingsScreen = currentScreen == StoreNavScreen.SETTINGS || currentScreen == StoreNavScreen.ACCESSIBILITY_SETTINGS
                            IconButton(
                                onClick = { viewModel.navigateTo(StoreNavScreen.SETTINGS) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("top_bar_settings_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings & Accessibility",
                                    tint = if (isSettingsScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            },
            floatingActionButton = {
                // Show floating chat button when not on Chat screen or Checkout
                if (currentScreen != StoreNavScreen.CHAT_SUPPORT && currentScreen != StoreNavScreen.CART) {
                    FloatingActionButton(
                        onClick = { viewModel.navigateTo(StoreNavScreen.CHAT_SUPPORT) },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("floating_chat_support_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Text("🤖", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isArabic) "مساعد وندر" else "Ask Wonder", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            bottomBar = {
                // Persistent bottom navigation with 5 key destinations
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    // 1. Home
                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.HOME,
                        onClick = { viewModel.navigateTo(StoreNavScreen.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text(if (isArabic) "الرئيسية" else "Home", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    // 2. Catalog
                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.CATALOG,
                        onClick = { viewModel.navigateTo(StoreNavScreen.CATALOG) },
                        icon = { Icon(Icons.Default.Explore, contentDescription = "Catalog") },
                        label = { Text(if (isArabic) "الألعاب" else "Catalog", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_item_catalog")
                    )

                    // 3. Wishlist
                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.WISHLIST,
                        onClick = { viewModel.navigateTo(StoreNavScreen.WISHLIST) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (wishlistIds.isNotEmpty()) {
                                        Badge { Text("${wishlistIds.size}") }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = "Wishlist")
                            }
                        },
                        label = { Text(if (isArabic) "الأمنيات" else "Wishlist", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_item_wishlist")
                    )

                    // 4. Cart
                    val totalCartQty = cartItems.sumOf { it.entity.quantity }
                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.CART,
                        onClick = { viewModel.navigateTo(StoreNavScreen.CART) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (totalCartQty > 0) {
                                        Badge { Text("$totalCartQty") }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            }
                        },
                        label = { Text(if (isArabic) "السلة" else "Bag", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_item_cart")
                    )

                    // 5. Rewards
                    NavigationBarItem(
                        selected = currentScreen == StoreNavScreen.REWARDS,
                        onClick = { viewModel.navigateTo(StoreNavScreen.REWARDS) },
                        icon = {
                            Icon(Icons.Default.Stars, contentDescription = "Rewards")
                        },
                        label = { Text(if (isArabic) "المكافآت" else "Rewards", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_item_rewards")
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    StoreNavScreen.HOME -> {
                        HomeScreen(
                            viewModel = viewModel,
                            toys = filteredToys,
                            wishlistIds = wishlistIds,
                            onOpenFilterSheet = { showFilterSheet = true },
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.CATALOG -> {
                        CatalogScreen(
                            viewModel = viewModel,
                            filterState = filterState,
                            toys = filteredToys,
                            wishlistIds = wishlistIds,
                            onOpenFilterSheet = { showFilterSheet = true },
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.DETAIL, StoreNavScreen.TOY_DETAIL -> {
                        selectedToy?.let { toy ->
                            ProductDetailScreen(
                                toy = toy,
                                viewModel = viewModel,
                                isWishlisted = wishlistIds.contains(toy.id),
                                onBack = { viewModel.navigateBack() }
                            )
                        } ?: run {
                            viewModel.navigateTo(StoreNavScreen.CATALOG)
                        }
                    }

                    StoreNavScreen.WISHLIST -> {
                        WishlistScreen(
                            viewModel = viewModel,
                            wishlistToys = wishlistToys,
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.CART -> {
                        CartCheckoutScreen(
                            viewModel = viewModel,
                            cartItems = cartItems,
                            isParentUnlocked = isParentUnlocked,
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.ORDER_TRACKING, StoreNavScreen.ORDERS, StoreNavScreen.ORDER_DETAIL -> {
                        OrderTrackingScreen(
                            viewModel = viewModel,
                            orders = orders,
                            selectedOrderId = selectedOrderId,
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.REWARDS -> {
                        RewardsScreen(
                            viewModel = viewModel,
                            profile = rewardsProfile,
                            lastSpinResult = lastSpinResult,
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.CHAT_SUPPORT -> {
                        ChatSupportScreen(
                            viewModel = viewModel,
                            messages = chatMessages,
                            isKidMode = isKidMode
                        )
                    }

                    StoreNavScreen.SETTINGS, StoreNavScreen.ACCESSIBILITY_SETTINGS -> {
                        SettingsScreen(
                            viewModel = viewModel,
                            themeMode = themeMode,
                            isKidMode = isKidMode,
                            isParentUnlocked = isParentUnlocked,
                            hapticFeedbackEnabled = hapticFeedbackEnabled
                        )
                    }
                }
            }
        }
    }
}
}
