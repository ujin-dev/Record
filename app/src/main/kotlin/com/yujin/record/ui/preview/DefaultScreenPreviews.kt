package com.yujin.record.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "ScreenPreviewLight",
    showBackground = true,
    showSystemUi = true,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "ScreenPreviewDark",
    showBackground = true,
    showSystemUi = true,
)
annotation class DefaultScreenPreviews