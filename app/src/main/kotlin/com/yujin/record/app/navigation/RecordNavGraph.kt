package com.yujin.record.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yujin.record.feature.calendar.CalendarRoute
import com.yujin.record.feature.review.detail.ReviewDetailRoute
import com.yujin.record.feature.review.form.ReviewFormRoute
import kotlinx.datetime.LocalDate
import kotlin.reflect.typeOf

@Composable
fun RecordNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RecordRoute.Calendar,
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

