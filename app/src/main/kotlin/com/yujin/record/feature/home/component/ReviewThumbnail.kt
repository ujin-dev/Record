package com.yujin.record.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.yujin.record.R

@Composable
fun ReviewThumbnail(modifier: Modifier = Modifier, resId: Int) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}