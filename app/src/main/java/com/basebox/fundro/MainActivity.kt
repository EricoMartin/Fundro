package com.basebox.fundro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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
import com.basebox.fundro.core.payment.PaystackHelper
import com.basebox.fundro.ui.auth.login.LoginScreen
import com.basebox.fundro.ui.auth.register.RegisterScreen
import com.basebox.fundro.ui.components.feedback.ProvideFeedbackManager
import com.basebox.fundro.ui.group.create.CreateGroupScreen
import com.basebox.fundro.ui.group.detail.GroupDetailScreen
import com.basebox.fundro.ui.group.members.AddMembersScreen
import com.basebox.fundro.ui.home.HomeScreen
import com.basebox.fundro.ui.notification.NotificationsScreen
import com.basebox.fundro.ui.onboarding.OnboardingScreen
import com.basebox.fundro.ui.payment.initiate.PaymentScreen
import com.basebox.fundro.ui.payment.verify.PaymentVerificationScreen
import com.basebox.fundro.ui.profile.ProfileScreen
import com.basebox.fundro.ui.profile.edit.EditProfileScreen
import com.basebox.fundro.ui.profile.kyc.KycScreen
import com.basebox.fundro.ui.splash.SplashScreen
import com.basebox.fundro.ui.theme.FundroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen()

        PaystackHelper.initialize()


        setContent {
            FundroTheme {
                ProvideFeedbackManager {
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

                            composable("create-group") {
                                CreateGroupScreen(navController = navController)
                            }

                            composable(
                                route = "group/{groupId}",
                                arguments = listOf(
                                    navArgument("groupId") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val groupId =
                                    backStackEntry.arguments?.getString("groupId")
                                        ?: return@composable
                                GroupDetailScreen(
                                    navController = navController,
                                    groupId = groupId
                                )
                            }

                            composable(
                                route = "group/{groupId}/add-members",
                                arguments = listOf(
                                    navArgument("groupId") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val groupId =
                                    backStackEntry.arguments?.getString("groupId")
                                        ?: return@composable
                                AddMembersScreen(
                                    navController = navController,
                                    groupId = groupId
                                )
                            }

                            composable(
                                route = "payment/{groupId}",
                                arguments = listOf(
                                    navArgument("groupId") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val groupId = backStackEntry.arguments?.getString("groupId")
                                    ?: return@composable
                                PaymentScreen(
                                    navController = navController,
                                    groupId = groupId
                                )
                            }

                            composable(
                                route = "payment/{contributionId}/verify",
                                arguments = listOf(
                                    navArgument("contributionId") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val contributionId =
                                    backStackEntry.arguments?.getString("contributionId")
                                        ?: return@composable
                                PaymentVerificationScreen(
                                    navController = navController,
                                    contributionId = contributionId
                                )
                            }

                            composable("profile") {
                                ProfileScreen(navController = navController)
                            }
                            composable("profile/edit") {
                                EditProfileScreen(navController = navController)
                            }
                            composable("profile/kyc") {
                                KycScreen(navController = navController)
                            }

                            composable("notifications") {
                                NotificationsScreen(navController = navController)
                            }
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