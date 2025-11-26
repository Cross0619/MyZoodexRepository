package com.example.myzoodex.navigation

sealed class Screen(val route: String) {
    object List : Screen("animalList")
    object Detail : Screen("animalDetail/{animalId}") {
        fun createRoute(animalId: Int) = "animalDetail/$animalId"
    }
    object Splash : Screen("splash")
}
