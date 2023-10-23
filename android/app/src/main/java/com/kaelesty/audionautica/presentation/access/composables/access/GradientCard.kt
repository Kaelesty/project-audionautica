package com.kaelesty.audionautica.presentation.access.composables.access

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradientCard(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit = {}
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.padding(8.dp)
		,
		colors = CardDefaults.cardColors(
			MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
		)
	) {
		Box(
			modifier = Modifier
				.padding(8.dp)
				.background(
					brush = Brush.linearGradient(
						listOf(
							MaterialTheme.colorScheme.surface,
							MaterialTheme.colorScheme.surfaceVariant
						)
					),
					shape = RoundedCornerShape(8.dp)
				)
				.fillMaxSize()
		) {
			content()
		}
	}
}