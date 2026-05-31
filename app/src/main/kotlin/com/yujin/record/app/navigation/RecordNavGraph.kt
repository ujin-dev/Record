package com.yujin.record.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yujin.record.feature.calendar.CalendarScreen

@Composable
fun RecordNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RecordRoute.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = RecordRoute.Home.route) {
            CalendarScreen()
        }
    }
}