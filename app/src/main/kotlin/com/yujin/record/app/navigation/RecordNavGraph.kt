package com.yujin.record.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yujin.record.feature.calendar.CalendarRoute
import com.yujin.record.feature.review.detail.ReviewDetailRoute
import com.yujin.record.feature.review.form.ReviewFormRoute

@Composable
fun RecordNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RecordRoute.Calendar,
    ) {
        val onNavigateToDetail: (Long) -> Unit = { id ->
            navController.navigate(route = RecordRoute.ReviewDetail(id))
        }

        composable<RecordRoute.Calendar> {
            CalendarRoute(
                onNavigateToCreate = {
                    navController.navigate(route = RecordRoute.ReviewForm())
                },
                onNavigateToDetail = onNavigateToDetail,
            )
        }

        composable<RecordRoute.ReviewForm> {
            ReviewFormRoute(
                onBackClick = navController::popBackStack,
                onNavigateToDetail = onNavigateToDetail,
            )
        }

        composable<RecordRoute.ReviewDetail> {
            ReviewDetailRoute(
                onBackClick = navController::popBackStack,
            )
        }
    }
}

