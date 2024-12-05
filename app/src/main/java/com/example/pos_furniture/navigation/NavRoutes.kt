package com.example.pos_furniture.navigation

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object FurnitureList : NavRoutes("furnitureList")
    object Invoice : NavRoutes("invoice")
}