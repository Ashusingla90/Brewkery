package com.example.brewkery.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.brewkery.presentation.cart.CartScreen
import com.example.brewkery.presentation.detail.ItemDetailScreen
import com.example.brewkery.presentation.menu.MenuScreen
import com.example.brewkery.presentation.order.OrderStatusScreen


@Composable
fun BrewNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.MENU) {
        composable(Routes.MENU) {
            MenuScreen(
                onItemClick = { navController.navigate(Routes.detail(it)) },
                onCartClick = { navController.navigate(Routes.CART) },
                onOrderClick = { navController.navigate(Routes.ORDER) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) {
            ItemDetailScreen(
                onBack = { navController.popBackStack() },
                onAddedToCart = {
                    navController.navigate(Routes.CART) { popUpTo(Routes.MENU) }
                }
            )
        }

        composable(Routes.CART) {
            CartScreen(
                onBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Routes.ORDER) { popUpTo(Routes.MENU) }
                }
            )
        }

        composable(Routes.ORDER) {
            OrderStatusScreen(
                onBackToMenu = { navController.popBackStack(Routes.MENU, inclusive = false) }
            )
        }
    }
}