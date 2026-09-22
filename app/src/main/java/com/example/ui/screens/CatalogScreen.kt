package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgeGroup
import com.example.data.model.Toy
import com.example.ui.components.AgeFilterBar
import com.example.ui.components.ProductItemCard
import com.example.ui.components.ToyCard
import com.example.ui.viewmodel.FilterState
import com.example.ui.viewmodel.ToyStoreViewModel

@Composable
fun CatalogScreen(
    viewModel: ToyStoreViewModel,
    filterState: FilterState,
    toys: List<Toy>,
    wishlistIds: Set<String>,
    onOpenFilterSheet: () -> Unit,
    isKidMode: Boolean
) {
    val isArabic by viewModel.isArabic.collectAsState()
    var isGridView by remember { mutableStateOf(true) }

    val activeFilterCount = (if (filterState.ageGroup != AgeGroup.ALL) 1 else 0) +
            (if (filterState.category != com.example.data.model.ToyCategory.ALL) 1 else 0) +
            (if (filterState.brand != "All Brands") 1 else 0) +
            (if (filterState.maxPrice != null) 1 else 0) +
            (if (filterState.bpaFreeOnly) 1 else 0) +
            (if (filterState.stemCertifiedOnly) 1 else 0) +
            (if (filterState.batteriesNotRequiredOnly) 1 else 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("catalog_screen")
    ) {
        // Search & Filter header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = filterState.query,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = {
                        Text(if (isArabic) "ابحث عن اللعبة أو الماركة (ليجو، باربي)..." else "Search toys, brands (LEGO, Barbie)...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (filterState.query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("catalog_search_input")
                )

                BadgedBox(
                    badge = {
                        if (activeFilterCount > 0) {
                            Badge { Text("$activeFilterCount") }
                        }
                    }
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (activeFilterCount > 0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("open_filters_button")
                    ) {
                        IconButton(onClick = onOpenFilterSheet) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter Toys",
                                tint = if (activeFilterCount > 0) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Age-based Navigation Bar
            AgeFilterBar(
                selectedAgeGroup = filterState.ageGroup,
                onSelectAgeGroup = { viewModel.setAgeGroupFilter(it) },
                isKidMode = isKidMode
            )
        }

        // Active Status & Results Count Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isArabic) "${toys.size} لعبة في قطر 🇶🇦" else "${toys.size} Toys in Qatar 🇶🇦",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (filterState.brand != "All Brands") {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clickable { viewModel.setBrandFilter(null) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = filterState.brand,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear brand filter",
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (activeFilterCount > 0 || filterState.query.isNotEmpty()) {
                    Text(
                        text = if (isArabic) "إعادة تعيين" else "Reset",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { viewModel.resetFilters() }
                            .testTag("reset_filters_text")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                IconButton(
                    onClick = { isGridView = !isGridView },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("catalog_layout_toggle")
                ) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = if (isGridView) "Switch to list view" else "Switch to grid view",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Toys List / Grid
        if (toys.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SentimentDissatisfied,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isArabic) "لا توجد ألعاب تطابق خيارات البحث" else "No Toys Match Your Filters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isArabic) "جرب إزالة بعض الفلاتر أو البحث بكلمات أخرى" else "Try clearing some filters or searching for different keywords",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.resetFilters() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(if (isArabic) "مسح جميع الفلاتر" else "Clear All Filters")
                    }
                }
            }
        } else if (isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("product_grid_view"),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(toys, key = { it.id }) { toy ->
                    ProductItemCard(
                        toy = toy,
                        isWishlisted = wishlistIds.contains(toy.id),
                        onClick = { viewModel.openToyDetail(toy) },
                        onAddToCart = { viewModel.addToCart(toy) },
                        onWishlistToggle = { viewModel.toggleWishlist(toy) },
                        isArabic = isArabic
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("product_list_view"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(toys, key = { it.id }) { toy ->
                    ToyCard(
                        toy = toy,
                        isWishlisted = wishlistIds.contains(toy.id),
                        onToyClick = { viewModel.openToyDetail(toy) },
                        onWishlistToggle = { viewModel.toggleWishlist(toy) },
                        onAddToCart = { viewModel.addToCart(toy) },
                        isKidMode = isKidMode
                    )
                }
            }
        }
    }
}
