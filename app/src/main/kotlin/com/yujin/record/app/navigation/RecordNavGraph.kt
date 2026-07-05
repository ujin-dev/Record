package com.yujin.record.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yujin.record.feature.calendar.CalendarRoute
import com.yujin.record.feature.review.detail.ReviewDetailRoute
import com.yujin.record.feature.review.edit.ReviewEditRoute

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
                    navController.navigate(route = RecordRoute.ReviewCreate)
                },
                onNavigateToDetail = onNavigateToDetail,
            )
        }

        composable<RecordRoute.ReviewCreate> {
            ReviewEditRoute(
                onBackClick = navController::popBackStack,
                onNavigateToDetail = onNavigateToDetail,
            )
        }

        composable<RecordRoute.ReviewEdit> {
            ReviewEditRoute(
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

