package com.phoenix.phoenixnotes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phoenix.phoenixnotes.data.model.Note
import com.phoenix.phoenixnotes.ui.createNote.CreateNote
import com.phoenix.phoenixnotes.ui.home.Home
import com.phoenix.phoenixnotes.ui.theme.PhoenixnotesTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalUnsignedTypes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhoenixnotesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { Home(navController) }
                        composable("createNote") {
                            val note =
                                navController.previousBackStackEntry?.arguments?.getSerializable("note") as Note?
                            CreateNote(navController, note)
                        }
                    }
                }
            }
        }

    }
}
