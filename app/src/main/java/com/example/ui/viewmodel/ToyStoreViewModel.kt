package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ChatMessageEntity
import com.example.data.local.OrderEntity
import com.example.data.local.RewardsProfileEntity
import com.example.data.local.ToyDatabase
import com.example.data.model.AgeGroup
import com.example.data.model.QatarStore
import com.example.data.model.QatarStoreDirectory
import com.example.data.model.Toy
import com.example.data.model.ToyCategory
import com.example.data.repository.ToyCatalog
import com.example.data.repository.ToyStoreRepository
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class StoreNavScreen {
    HOME,
    CATALOG,
    WISHLIST,
    CART,
    ORDERS,
    ORDER_TRACKING,
    REWARDS,
    CHAT_SUPPORT,
    ACCESSIBILITY_SETTINGS,
    SETTINGS,
    TOY_DETAIL,
    DETAIL,
    ORDER_DETAIL
}

data class FilterState(
    val query: String = "",
    val ageGroup: AgeGroup = AgeGroup.ALL,
    val category: ToyCategory = ToyCategory.ALL,
    val brand: String = "All Brands",
    val maxPrice: Double? = null,
    val minRating: Float? = null,
    val bpaFreeOnly: Boolean = false,
    val stemCertifiedOnly: Boolean = false,
    val batteriesNotRequiredOnly: Boolean = false,
    val sortBy: String = "POPULAR" // "POPULAR", "PRICE_LOW", "PRICE_HIGH", "RATING"
)

class ToyStoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ToyStoreRepository

    init {
        val db = ToyDatabase.getDatabase(application)
        repository = ToyStoreRepository(db.toyDao())

        viewModelScope.launch {
            repository.ensureDemoOrdersExist()
            repository.initializeChatWelcomeIfEmpty()
        }
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(StoreNavScreen.HOME)
    val currentScreen: StateFlow<StoreNavScreen> = _currentScreen.asStateFlow()

    private val _screenBackStack = mutableListOf<StoreNavScreen>()

    private val _selectedToy = MutableStateFlow<Toy?>(null)
    val selectedToy: StateFlow<Toy?> = _selectedToy.asStateFlow()

    private val _selectedOrderId = MutableStateFlow<String?>(null)
    val selectedOrderId: StateFlow<String?> = _selectedOrderId.asStateFlow()

    fun navigateTo(screen: StoreNavScreen) {
        if (_currentScreen.value != screen) {
            _screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenBackStack.isNotEmpty()) {
            _currentScreen.value = _screenBackStack.removeAt(_screenBackStack.size - 1)
            return true
        }
        return false
    }

    fun openToyDetail(toy: Toy) {
        _selectedToy.value = toy
        navigateTo(StoreNavScreen.TOY_DETAIL)
    }

    fun openOrderDetail(orderId: String) {
        _selectedOrderId.value = orderId
        navigateTo(StoreNavScreen.ORDER_DETAIL)
    }

    // Filter State
    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun setAgeGroupFilter(ageGroup: AgeGroup) {
        _filterState.value = _filterState.value.copy(ageGroup = ageGroup)
    }

    fun setCategoryFilter(category: ToyCategory) {
        _filterState.value = _filterState.value.copy(category = category)
    }

    fun setBrandFilter(brand: String?) {
        _filterState.value = _filterState.value.copy(brand = brand ?: "All Brands")
    }

    fun setPriceFilter(maxPrice: Double?) {
        _filterState.value = _filterState.value.copy(maxPrice = maxPrice)
    }

    fun setMaxPriceFilter(maxPrice: Double?) = setPriceFilter(maxPrice)

    fun setRatingFilter(minRating: Float?) {
        _filterState.value = _filterState.value.copy(minRating = minRating)
    }

    fun setMinRatingFilter(minRating: Float?) = setRatingFilter(minRating)

    fun toggleBpaFilter() {
        _filterState.value = _filterState.value.copy(bpaFreeOnly = !_filterState.value.bpaFreeOnly)
    }

    fun toggleBpaFreeFilter() = toggleBpaFilter()

    fun toggleStemFilter() {
        _filterState.value = _filterState.value.copy(stemCertifiedOnly = !_filterState.value.stemCertifiedOnly)
    }

    fun toggleStemCertifiedFilter() = toggleStemFilter()

    fun toggleNoBatteriesFilter() {
        _filterState.value = _filterState.value.copy(batteriesNotRequiredOnly = !_filterState.value.batteriesNotRequiredOnly)
    }

    fun toggleBatteriesNotRequiredFilter() = toggleNoBatteriesFilter()

    fun setSortBy(sortBy: String) {
        _filterState.value = _filterState.value.copy(sortBy = sortBy)
    }

    fun resetFilters() {
        _filterState.value = FilterState()
    }

    // Filtered Toys
    val filteredToys: StateFlow<List<Toy>> = _filterState.combine(MutableStateFlow(repository.allToys)) { filter, _ ->
        repository.filterToys(
            query = filter.query,
            ageGroup = filter.ageGroup,
            category = filter.category,
            brand = filter.brand,
            maxPrice = filter.maxPrice,
            minRating = filter.minRating,
            bpaFreeOnly = filter.bpaFreeOnly,
            stemCertifiedOnly = filter.stemCertifiedOnly,
            batteriesNotRequiredOnly = filter.batteriesNotRequiredOnly,
            sortBy = filter.sortBy
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.allToys)

    // Wishlist
    val wishlistToyIds: StateFlow<Set<String>> = repository.wishlistToyIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val wishlistIds: StateFlow<Set<String>> = wishlistToyIds

    val wishlistToys: StateFlow<List<Toy>> = repository.wishlistToys
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleWishlist(toy: Toy) {
        viewModelScope.launch {
            repository.toggleWishlist(toy.id)
        }
    }

    // Cart
    val cartItems: StateFlow<List<ToyStoreRepository.CartItemWithToy>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addToCart(toy: Toy, quantity: Int = 1, giftWrap: Boolean = false, note: String = "") {
        viewModelScope.launch {
            repository.addToCart(toy.id, quantity, giftWrap, note)
            emitToast("Added ${toy.title} to cart!")
        }
    }

    fun updateCartQuantity(toyId: String, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(toyId, newQty)
        }
    }

    fun updateGiftOption(toyId: String, giftWrap: Boolean, note: String) {
        viewModelScope.launch {
            repository.updateGiftOptions(toyId, giftWrap, note)
        }
    }

    fun removeCartItem(toyId: String) {
        viewModelScope.launch {
            repository.removeCartItem(toyId)
        }
    }

    fun moveWishlistToCart(toy: Toy) {
        viewModelScope.launch {
            repository.addToCart(toy.id, 1)
            repository.removeFromWishlist(toy.id)
            emitToast("Moved ${toy.title} to cart!")
        }
    }

    fun moveAllWishlistToCart() {
        viewModelScope.launch {
            val list = wishlistToys.value
            list.forEach { toy ->
                repository.addToCart(toy.id, 1)
                repository.removeFromWishlist(toy.id)
            }
            emitToast("Moved all ${list.size} items to cart!")
        }
    }

    // Orders & Tracking
    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val orders: StateFlow<List<OrderEntity>> = allOrders

    fun advanceOrderTracking(orderId: String) {
        viewModelScope.launch {
            repository.advanceOrderTrackingStep(orderId)
            emitToast("Live delivery progress updated!")
        }
    }

    // Checkout & Parental Gate
    private val _isParentUnlocked = MutableStateFlow(false)
    val isParentUnlocked: StateFlow<Boolean> = _isParentUnlocked.asStateFlow()

    private val _showParentalGate = MutableStateFlow(false)
    val showParentalGate: StateFlow<Boolean> = _showParentalGate.asStateFlow()

    private val _parentalGatePurpose = MutableStateFlow("Parental Payment Verification")
    val parentalGatePurpose: StateFlow<String> = _parentalGatePurpose.asStateFlow()
    val parentalGateReason: StateFlow<String> = _parentalGatePurpose.asStateFlow()

    private val _parentPin = MutableStateFlow("1234")
    val parentPin: StateFlow<String> = _parentPin.asStateFlow()

    fun setParentPin(pin: String) {
        _parentPin.value = pin
        emitToast("Parent PIN updated!")
    }

    fun verifyParentPin(pin: String): Boolean = pin.trim() == _parentPin.value

    fun triggerParentalGate(purpose: String = "Parental Payment Verification") {
        _parentalGatePurpose.value = purpose
        _showParentalGate.value = true
    }

    fun dismissParentalGate() {
        _showParentalGate.value = false
    }

    fun onParentalGateSuccess() {
        _isParentUnlocked.value = true
        _showParentalGate.value = false
        emitToast("Parental authorization confirmed!")
    }

    fun lockParentMode() {
        _isParentUnlocked.value = false
    }

    // Place Order
    fun placeOrder(address: String, couponCode: String = "") {
        viewModelScope.launch {
            val items = cartItems.value
            if (items.isEmpty()) return@launch

            val subtotal = items.sumOf { (it.toy.price * it.entity.quantity) + (if (it.entity.giftWrap) 0.0 else 0.0) } // Gift wrap is free at Kiddy Zone
            val discount = if (couponCode.contains("KIDDY10", ignoreCase = true) || couponCode.contains("WELCOME", ignoreCase = true)) 10.0 else 0.0

            val orderAddress = if (_fulfillmentType.value == "CLICK_AND_COLLECT") {
                "Store Pickup: ${_selectedStore.value.nameEn} (${_selectedStore.value.mallEn})"
            } else {
                "${_deliveryArea.value} - $address"
            }

            val order = repository.placeOrder(
                items = items,
                totalAmount = (subtotal - discount).coerceAtLeast(0.0),
                deliveryAddress = orderAddress,
                discountApplied = discount
            )
            _selectedOrderId.value = order.orderId
            navigateTo(StoreNavScreen.ORDER_DETAIL)
            emitToast("Order confirmed! Earned ${order.totalAmount.toInt()} Kiddy Points!")
        }
    }

    // Rewards
    val rewardsProfile: StateFlow<RewardsProfileEntity> = repository.rewardsProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RewardsProfileEntity())

    private val _lastSpinResult = MutableStateFlow<Int?>(null)
    val lastSpinResult: StateFlow<Int?> = _lastSpinResult.asStateFlow()

    fun spinRewardsWheel() {
        viewModelScope.launch {
            val won = repository.spinRewardsWheel()
            _lastSpinResult.value = won
            emitToast("🎉 You won $won Kiddy Points!")
        }
    }

    fun clearSpinResult() {
        _lastSpinResult.value = null
    }

    fun redeemVoucher(cost: Int, title: String, code: String) {
        viewModelScope.launch {
            val success = repository.redeemVoucher(cost, title, code)
            if (success) {
                emitToast("Redeemed voucher: $title ($code)!")
            } else {
                emitToast("Not enough Kiddy Points!")
            }
        }
    }

    // Support Chat
    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendChatMessage(message: String) {
        viewModelScope.launch {
            repository.sendUserChatMessage(message)
        }
    }

    // Accessibility & Inclusivity Settings
    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    private val _isKidMode = MutableStateFlow(false)
    val isKidMode: StateFlow<Boolean> = _isKidMode.asStateFlow()

    fun toggleKidMode() {
        _isKidMode.value = !_isKidMode.value
        if (_isKidMode.value) {
            _isParentUnlocked.value = false
            emitToast("Kid Safe Mode Activated: Big friendly cards enabled!")
        } else {
            emitToast("Parent Mode Activated")
        }
    }

    private val _fontSizeMultiplier = MutableStateFlow(1.0f) // 1.0f, 1.15f, 1.3f
    val fontSizeMultiplier: StateFlow<Float> = _fontSizeMultiplier.asStateFlow()

    fun setFontSizeMultiplier(scale: Float) {
        _fontSizeMultiplier.value = scale
    }

    private val _hapticFeedback = MutableStateFlow(true)
    val hapticFeedback: StateFlow<Boolean> = _hapticFeedback.asStateFlow()
    val hapticFeedbackEnabled: StateFlow<Boolean> = _hapticFeedback.asStateFlow()

    fun toggleHapticFeedback() {
        _hapticFeedback.value = !_hapticFeedback.value
    }

    fun toggleHaptics() = toggleHapticFeedback()

    // Qatar Localization & Fulfillment
    private val _isArabic = MutableStateFlow(false)
    val isArabic: StateFlow<Boolean> = _isArabic.asStateFlow()

    fun toggleLanguage() {
        _isArabic.value = !_isArabic.value
        emitToast(if (_isArabic.value) "تم التبديل إلى اللغة العربية" else "Switched to English")
    }

    private val _fulfillmentType = MutableStateFlow("DELIVERY") // "DELIVERY" or "CLICK_AND_COLLECT"
    val fulfillmentType: StateFlow<String> = _fulfillmentType.asStateFlow()

    fun setFulfillmentType(type: String) {
        _fulfillmentType.value = type
    }

    private val _selectedStore = MutableStateFlow(QatarStoreDirectory.stores[0])
    val selectedStore: StateFlow<QatarStore> = _selectedStore.asStateFlow()

    fun selectQatarStore(store: QatarStore) {
        _selectedStore.value = store
        emitToast("Selected pickup store: ${store.nameEn}")
    }

    private val _deliveryArea = MutableStateFlow("Doha & West Bay")
    val deliveryArea: StateFlow<String> = _deliveryArea.asStateFlow()

    fun setDeliveryArea(area: String) {
        _deliveryArea.value = area
    }

    private val _paymentMethod = MutableStateFlow("NAPS_DEBIT") // "NAPS_DEBIT", "CASH_ON_DELIVERY", "APPLE_PAY"
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    private val _giftWrapTheme = MutableStateFlow("Classic Kiddy Red Ribbon")
    val giftWrapTheme: StateFlow<String> = _giftWrapTheme.asStateFlow()

    fun setGiftWrapTheme(theme: String) {
        _giftWrapTheme.value = theme
    }

    // Toast/Snackbar Events
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toastMessage.emit(msg)
        }
    }

    fun getToy(id: String): Toy? = repository.getToyById(id)
}
