package com.example.zussathia.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zussathia.data.model.CategoryData
import com.example.zussathia.ui.theme.AccentBlack
import com.example.zussathia.ui.theme.CardBackground
import com.example.zussathia.ui.theme.TextDark

@Composable
fun CategorySection(
    categories: List<CategoryData>,
    selectedID: String,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextDark
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    categoryTitle = category.name,
                    isSelected = category._id == selectedID,
                    onSelected = { onCategorySelected(category._id) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(categoryTitle: String, isSelected: Boolean, onSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .background(
                color = if (isSelected) AccentBlack else CardBackground,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onSelected() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = categoryTitle,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else TextDark,
            style = MaterialTheme.typography.bodyMedium

        )
    }
}