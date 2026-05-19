package com.yujin.record.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.yujin.record.app.navigation.RecordNavGraph

@Composable
fun RecordApp() {
    val navController = rememberNavController()
    RecordNavGraph(navController = navController)
}