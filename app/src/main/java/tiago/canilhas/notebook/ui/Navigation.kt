package tiago.canilhas.notebook.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tiago.canilhas.notebook.ui.screens.mainScreen.Screen as MainScreen
import tiago.canilhas.notebook.ui.screens.mainScreen.ViewModel as MainViewModel
import tiago.canilhas.notebook.ui.screens.notebookScreen.Screen as NotebookScreen
import tiago.canilhas.notebook.ui.screens.notebookScreen.ViewModel as NotebookViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavBackStackEntry

@Composable
fun Navigation(
    factory: ViewModelProvider.Factory,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Main.ROUTE,
        modifier = Modifier.fillMaxSize(),
        enterTransition = Transitions.enterTransition,
        exitTransition = Transitions.exitTransition,
        popEnterTransition = Transitions.popEnterTransition,
        popExitTransition = Transitions.popExitTransition
    ) {

        composable(route = Routes.Main.ROUTE) {
            val viewModel: MainViewModel = viewModel(factory = factory)

            MainScreen(
                viewModel = viewModel,
                onNotebookClicked = { notebookId ->
                    navController.navigate("detail/$notebookId")
                }
            )
        }

        composable(
            route = Routes.Notebook.ROUTE,
            arguments = listOf(navArgument(Routes.Notebook.ARGUMENT_1) { type = NavType.LongType })
        ) { backStackEntry ->
            val viewModel: NotebookViewModel = viewModel(factory = factory)

            val notebookId = backStackEntry.arguments?.getLong(Routes.Notebook.ARGUMENT_1) ?: 0L

            LaunchedEffect(notebookId) {
                if (notebookId > 0) viewModel.loadNotebookData(notebookId)
            }

            NotebookScreen(
                viewModel = viewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}

object Routes {

    object Main {
        const val ROUTE = "main"
    }

    object Notebook {
        const val ARGUMENT_1 = "notebookId"
        const val ROUTE = "detail/{$ARGUMENT_1}"
    }
}

object Transitions {
    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        EnterTransition.None
    }

    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        ExitTransition.None
    }

    val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
        EnterTransition.None
    }

    val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
        ExitTransition.None
    }
}