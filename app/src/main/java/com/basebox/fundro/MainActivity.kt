package com.basebox.fundro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.basebox.fundro.ui.auth.login.LoginScreen
import com.basebox.fundro.ui.auth.register.RegisterScreen
import com.basebox.fundro.ui.group.detail.GroupDetailScreen
import com.basebox.fundro.ui.home.HomeScreen
import com.basebox.fundro.ui.onboarding.OnboardingScreen
import com.basebox.fundro.ui.splash.SplashScreen
import com.basebox.fundro.ui.theme.FundroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Install splash screen
        installSplashScreen()

        setContent {
            FundroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }

                        composable("onboarding") {
                            OnboardingScreen(navController = navController)
                        }

                        composable("login") {
                            LoginScreen(navController = navController)
                        }

                        composable("register") {
                            RegisterScreen(navController = navController)
                        }

                        composable("home") {
                            HomeScreen(navController = navController)
                        }

                        composable(
                            route = "group/{groupId}",
                            arguments = listOf(
                                navArgument("groupId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
                            GroupDetailScreen(
                                navController = navController,
                                groupId = groupId
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FundroTheme {
        Greeting("Android")
    }
}