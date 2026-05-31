package com.yujin.record.core.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "light",
    showBackground = true,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "dark",
    showBackground = true,
)
annotation class DefaultViewPreviews