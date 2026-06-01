package com.yujin.record.feature.review.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.yujin.record.core.ui.preview.DefaultScreenPreviews
import com.yujin.record.core.ui.theme.RecordTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewEditScreen(
    reviewId: Long? = null,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {} // TODO
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "리뷰 작성") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSaveClick,
                        enabled = true,
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "사진 가져오기"
                        )
                    }

                    IconButton(
                        onClick = onSaveClick,
                        enabled = true,
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "북마크"
                        )
                    }

                    IconButton(
                        onClick = onSaveClick,
                        enabled = true,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "저장"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // system insets
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp) // content padding
        ) {
            var selectedIndex by remember { mutableIntStateOf(0) }
            var title by remember { mutableStateOf("") }
            var creator by remember { mutableStateOf("") }
            var content by remember { mutableStateOf("") }
            var shortReview by remember { mutableStateOf("") }
            var rating by remember { mutableIntStateOf(3) }
            val chips = remember { mutableStateListOf<String>() }
            var tag by remember { mutableStateOf("") }


            val options = listOf("영화", "책")

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        label = { Text(text = label) }
                    )
                }
            }

            // title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it.trimStart() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "제목") },
                singleLine = true,
            )

            // creator
            OutlinedTextField(
                value = creator,
                onValueChange = { creator = it.trimStart() },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(if (selectedIndex == 0) "감독" else "저자")
                },
                singleLine = true,
            )

            // content
            OutlinedTextField(
                value = content,
                onValueChange = { content = it.trimStart() },
                label = { Text("상세 리뷰") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            // short review
            OutlinedTextField(
                value = shortReview,
                onValueChange = { shortReview = it.trimStart() },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("한줄평")
                },
                singleLine = true,
            )

            // rating
            // improvement : https://developer.android.com/develop/ui/compose/touch-input/scroll/scroll-modifiers#scrollable-modifier
            Row {
                repeat(5) { index ->
                    IconButton(onClick = { rating = index + 1 }) {
                        Icon(
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                }
            }

            // tag chips & input field
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                chips.forEach { label ->
                    InputChip(
                        selected = true,
                        onClick = {},
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "삭제",
                                modifier = Modifier
                                    .size(InputChipDefaults.IconSize)
                                    .clickable {
                                        chips.remove(label)
                                    }
                            )
                        },
                        label = { Text(text = label) },
                        modifier = Modifier.height(InputChipDefaults.Height)
                    )
                }

                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it.trim() }, // block blank
                    placeholder = { Text("태그 입력") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            chips.add(tag.trim())
                            tag = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "추가"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@DefaultScreenPreviews
fun ReviewEditScreenPreview() {
    RecordTheme {
        ReviewEditScreen()
    }
}