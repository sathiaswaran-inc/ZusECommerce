package com.example.zussathia.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.zussathia.data.model.ProductData
import com.example.zussathia.ui.StoreViewModel
import com.example.zussathia.ui.theme.BackgroundGrey
import com.example.zussathia.ui.theme.CardBackground
import com.example.zussathia.ui.theme.TextDark
import com.example.zussathia.ui.theme.TextMuted

@Composable
fun TitleSection(
    title: String = "Discover",
    subTitle: String = "Your Best Items",
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit = {},
    isSearchActive: Boolean,
    onSearchActiveChanged: (Boolean) -> Unit,
    cartCount: Int = 0,
    onCartClick: () -> Unit = {}
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val searchBarWidth by animateDpAsState(
        targetValue = if (isSearchActive) 340.dp else 110.dp,
        label = "SearchBarWidth"
    )
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = !isSearchActive,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextDark
                )
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Light),
                    color = TextMuted
                )
            }
        }

        Row(
            modifier = Modifier
                .width(searchBarWidth)
                .height(54.dp)
                .background(CardBackground, CircleShape)
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text(
                            text = "Search product name",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark,
                        disabledTextColor = TextDark,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = TextDark
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextDark),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() })
                )
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                        onSearchActiveChanged(false)
                        keyboardController?.hide()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Search",
                        tint = TextDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White, CircleShape),
                        onClick = { onSearchActiveChanged(true) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Open Search",
                            tint = TextDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(text = cartCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White, CircleShape),
                            onClick = onCartClick
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Open Cart",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ProductGridSelection(
    products: List<ProductData>,
    storeViewModel: StoreViewModel,
    onProductClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val chunks = products.chunked(2)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (rowItems in chunks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (product in rowItems) {
                        val isCurrentlyFav = storeViewModel.favProducts[product._id] ?: false
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onProductClick(product._id) }
                        ) {
                            ProductCard(
                                product = product,
                                isFavorited = isCurrentlyFav,
                                onFavoriteClick = {
                                    if (isCurrentlyFav) {
                                        storeViewModel.deleteItemFromWishList(product._id)
                                    } else {
                                        storeViewModel.addItemToWishList(product._id)
                                    }
                                }
                            )
                        }
                    }
                    if (rowItems.size == 1) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductData,
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(18.dp))
                .background(BackgroundGrey)
        ) {
            AsyncImage(
                model = product.imageCover,
                contentDescription = "product",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(24.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
            ) {
                IconButton(
                    onClick = { onFavoriteClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "fav",
                        tint = if (isFavorited) Color.Red else TextDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(
                        Color.White.copy(alpha = 0.9f),
                        RoundedCornerShape(topStart = 12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = product.ratingsAverage.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = product.brand.image,
                contentDescription = "brand",
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(BackgroundGrey)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = product.brand.name,
                style = MaterialTheme.typography.labelMedium,
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product.title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = TextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = product.description.replace("\t", " "),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            minLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${product.sold} sold",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = TextMuted
                )
                Text(
                    text = "${product.quantity} left",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (product.quantity < 20) Color.Red else Color(0xFF4CAF50)
                )
            }
        }
    }
}