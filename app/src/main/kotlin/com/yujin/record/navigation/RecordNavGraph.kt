package com.yujin.record.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yujin.record.presentation.calendar.CalendarRoute
import com.yujin.record.presentation.reviewdetail.ReviewDetailRoute
import com.yujin.record.presentation.reviewform.ReviewFormRoute

@Composable
fun RecordNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: RecordRoute = RecordRoute.Calendar,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<RecordRoute.Calendar> {
            CalendarRoute(
                onNavigateToCreate = { createdDate ->
                    navController.navigate(RecordRoute.ReviewForm(createdDate = createdDate))
                },
                onNavigateToDetail = { id ->
                    navController.navigate(RecordRoute.ReviewDetail(id))
                },
            )
        }

        composable<RecordRoute.ReviewForm> {
            ReviewFormRoute(
                onBackClick = navController::popBackStack,
                onNavigateToDetail = { id ->
                    navController.navigate(RecordRoute.ReviewDetail(id)) {
                        popUpTo(RecordRoute.Calendar) {
                            inclusive = false
                        }
                    }
                },
            )
        }

        composable<RecordRoute.ReviewDetail> {
            ReviewDetailRoute(
                onEditClick = { id ->
                    navController.navigate(RecordRoute.ReviewForm(reviewId = id))
                },
                onBackClick = navController::popBackStack,
                onDelete = {
                    navController.navigate(RecordRoute.Calendar) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

