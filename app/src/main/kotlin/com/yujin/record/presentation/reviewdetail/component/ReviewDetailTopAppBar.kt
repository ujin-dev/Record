package com.yujin.record.presentation.reviewdetail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailTopAppBar(
    backClick: () -> Unit,
    editClick: () -> Unit,
    deleteClick: () -> Unit,
    showActions: Boolean,
) {
    TopAppBar(
        title = {},
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        navigationIcon = {
            TopAppBarIconButton(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "뒤로가기",
                onClick = backClick
            )
        },
        actions = {
            if (showActions) {
                TopAppBarIconButton(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "수정하기",
                    onClick = editClick
                )

                TopAppBarIconButton(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "삭제하기",
                    onClick = deleteClick
                )
            }
        },
    )
}