package com.yujin.record.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yujin.record.app.RecordApplication
import com.yujin.record.feature.calendar.CalendarScreen
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
            CalendarScreen(
                onCreateReviewClick = {
                    navController.navigate(route = RecordRoute.ReviewCreate)
                }
            )
        }

        composable<RecordRoute.ReviewCreate> {
            ReviewEditRoute(
                onBackClick = navController::popBackStack,
                onNavigateToDetail = onNavigateToDetail,
                reviewRepository = (navController.context.applicationContext as RecordApplication).reviewRepository,
            )
        }

        composable<RecordRoute.ReviewEdit> { backStackEntry ->
            val review: RecordRoute.ReviewEdit = backStackEntry.toRoute()
            ReviewEditRoute(
                reviewId = review.reviewId,
                onBackClick = navController::popBackStack,
                onNavigateToDetail = onNavigateToDetail,
                reviewRepository = (navController.context.applicationContext as RecordApplication).reviewRepository,
            )
        }

        composable<RecordRoute.ReviewDetail> { backStackEntry ->
            val review: RecordRoute.ReviewDetail = backStackEntry.toRoute()
            ReviewDetailRoute(
                reviewId = review.reviewId,
                onBackClick = navController::popBackStack,
                reviewRepository = (navController.context.applicationContext as RecordApplication).reviewRepository,
            )
        }
    }
}

