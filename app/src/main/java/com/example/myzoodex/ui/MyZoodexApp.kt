package com.example.myzoodex.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myzoodex.model.Animal
import com.example.myzoodex.model.SortType
import com.example.myzoodex.ui.screen.AnimalDetailScreen
import com.example.myzoodex.ui.screen.AnimalListScreen
import com.example.myzoodex.ui.screen.SplashScreen
import com.example.myzoodex.ui.AnimalViewModel

@Composable
fun MyZoodexApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: AnimalViewModel = viewModel(
        factory = AnimalViewModel.provideFactory(context.applicationContext as Application)
    )
    val animals by viewModel.animals.collectAsStateWithLifecycle()

    var searchText by remember { mutableStateOf("") }
    var sortType by remember { mutableStateOf(SortType.ID_ASC) }

    val currentList = remember(animals, searchText, sortType) {
        val filtered = if (searchText.isBlank()) {
            animals
        } else {
            animals.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                    it.order.contains(searchText, ignoreCase = true) ||
                    it.family.contains(searchText, ignoreCase = true)
            }
        }
        when (sortType) {
            SortType.ID_ASC -> filtered.sortedBy { it.id }
            SortType.ID_DESC -> filtered.sortedByDescending { it.id }
            SortType.POPULARITY_DESC ->
                filtered.sortedWith(compareByDescending<Animal> { it.popularity }.thenBy { it.id })
            SortType.POPULARITY_ASC -> filtered.sortedWith(compareBy<Animal> { it.popularity }.thenBy { it.id })
            SortType.NAME_ASC -> filtered.sortedBy { it.name }
            SortType.NAME_DESC -> filtered.sortedByDescending { it.name }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("animalList") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("animalList") {
            AnimalListScreen(
                animals = currentList,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                sortType = sortType,
                onSortTypeChange = { sortType = it },
                onAnimalClick = { animalId ->
                    navController.navigate("animalDetail/$animalId")
                }
            )
        }

        composable(
            route = "animalDetail/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getInt("animalId") ?: 1

            AnimalDetailScreen(
                animals = currentList,
                initialAnimalId = animalId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
