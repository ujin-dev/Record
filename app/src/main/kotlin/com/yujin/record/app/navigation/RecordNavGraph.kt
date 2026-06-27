package com.yujin.record.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yujin.record.app.RecordApplication
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

        val reviewRepository =
            (navController.context.applicationContext as RecordApplication).reviewRepository

        composable<RecordRoute.Calendar> {
            CalendarRoute(
                onNavigateToCreate = {
                    navController.navigate(route = RecordRoute.ReviewCreate)
                },
                onNavigateToDetail = onNavigateToDetail,
                reviewRepository = reviewRepository,
            )
        }

        composable<RecordRoute.ReviewCreate> {
            ReviewEditRoute(
                onBackClick = navController::popBackStack,
                onNavigateToDetail = onNavigateToDetail,
                reviewRepository = reviewRepository,
            )
        }

        composable<RecordRoute.ReviewEdit> { backStackEntry ->
            val review: RecordRoute.ReviewEdit = backStackEntry.toRoute()
            ReviewEditRoute(
                reviewId = review.reviewId,
                onBackClick = navController::popBackStack,
                onNavigateToDetail = onNavigateToDetail,
                reviewRepository = reviewRepository,
            )
        }

        composable<RecordRoute.ReviewDetail> { backStackEntry ->
            val review: RecordRoute.ReviewDetail = backStackEntry.toRoute()
            ReviewDetailRoute(
                reviewId = review.reviewId,
                onBackClick = navController::popBackStack,
                reviewRepository = reviewRepository,
            )
        }
    }
}

