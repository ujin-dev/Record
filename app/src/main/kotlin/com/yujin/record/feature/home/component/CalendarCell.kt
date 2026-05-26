package com.yujin.record.feature.home.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yujin.record.R
import com.yujin.record.core.ui.theme.RecordTheme

@Composable
fun CalendarCell(date: Int = 0, reviewCount: Int = 0, imageResId: Int) {
    Box {
        ReviewThumbnail(resId = imageResId)
        Text(text = date.toString(), modifier = Modifier.align(Center))
        if (reviewCount > 1) {
            Text(text = "+${reviewCount - 1}", modifier = Modifier.align(BottomEnd))
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "light",
    showBackground = true,
    widthDp = 60, heightDp = 80
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "dark",
    showBackground = true,
    widthDp = 60, heightDp = 80
)
@Composable
fun CalendarCellPreview() {
    RecordTheme {
        CalendarCell(1, 3, R.drawable.movie_thumb_prada2)
    }
}