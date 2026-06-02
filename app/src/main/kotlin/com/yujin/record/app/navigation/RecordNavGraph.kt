package com.yujin.record.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yujin.record.feature.calendar.CalendarScreen
import com.yujin.record.feature.review.edit.ReviewEditRoute
import com.yujin.record.feature.review.edit.ReviewEditScreen

@Composable
fun RecordNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RecordRoute.Calendar,
    ) {
        composable<RecordRoute.Calendar> {
            CalendarScreen(
                onCreateReviewClick = {
                    navController.navigate(route = RecordRoute.ReviewCreate)
                }
            )
        }

        composable<RecordRoute.ReviewCreate> {
            ReviewEditRoute(
                onBackClick = navController::popBackStack
            )
        }

        composable<RecordRoute.ReviewEdit> { backStackEntry ->
            val review: RecordRoute.ReviewEdit = backStackEntry.toRoute()
            ReviewEditRoute(
                reviewId = review.reviewId,
                onBackClick = navController::popBackStack,
            )
        }
    }
}