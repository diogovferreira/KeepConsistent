package com.dfcoding.keepconsistent.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.ui.components.AppTextField
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.components.InfoDisplayComponent
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.dfcoding.keepconsistent.ui.signup.SignUpScreen
import com.dfcoding.keepconsistent.util.isValidEmail
import com.dfcoding.keepconsistent.navigation.RootScreen
import com.dfcoding.keepconsistent.ui.forgotpassword.ForgotPasswordScreen
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.app_name
import keepconsistent.composeapp.generated.resources.ic_app
import keepconsistent.composeapp.generated.resources.ic_google_login
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val supabaseClient = koinInject<SupabaseClient>()

        val googleAuthState = supabaseClient.composeAuth.rememberSignInWithGoogle(
            onResult = { result ->
                when(result){
                    NativeSignInResult.ClosedByUser -> viewModel.resetState()
                    is NativeSignInResult.Error -> viewModel.setError(result.message)
                    is NativeSignInResult.NetworkError -> viewModel.setError(result.message)
                    NativeSignInResult.Success -> viewModel.setSuccess()
                }
            }
        )

        LoginScreenStateless(
            uiState = uiState,
            onSignUp = { navigator.push(SignUpScreen()) },
            onLogin = { viewModel.signIn(it.first, it.second) },
            onDismissError = { viewModel.resetState() },
            onLoginSuccess = { navigator.replaceAll(RootScreen()) },
            onGoogleLogin = { viewModel.setLoading()
                googleAuthState.startFlow() },
            onForgotPassword = { navigator.push(ForgotPasswordScreen()) })
    }
}

@Composable
fun LoginScreenStateless(
    uiState: AuthUiState,
    onSignUp: () -> Unit = {},
    onLogin: (Pair<String, String>) -> Unit = {},
    onDismissError: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isValidEmail = email.isNotEmpty() && isValidEmail(email)

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding().padding(top = 100.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(bottom = 24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(Res.drawable.ic_app),
                    contentDescription = "App icon",
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(Res.string.app_name),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
            }

            Text(
                modifier = Modifier.padding(top = 28.dp),
                text = "Hello Welcome back!",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier.padding(top = 28.dp),
                text = "Sign in to continue",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 16.sp
            )

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    AppTextField(
                        placeholder = "Email",
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )
                    AppTextField(
                        placeholder = "Password",
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPassword = true
                    )

                    Text(
                        "Forgot Password?",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.End).clickable{
                            onForgotPassword()
                        }
                    )
                    ButtonComponent(text = "Login", onClick = {
                        if (isValidEmail && password.isNotEmpty()) {
                            onLogin(Pair(email, password))
                        }

                    })
                }
            }

            Text(
                modifier = Modifier.padding(bottom = 14.dp),
                text = "Or Login with",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Image(
                modifier = Modifier.clickable { onGoogleLogin() },
                painter = painterResource(Res.drawable.ic_google_login),
                contentDescription = "Google Login Icon"
            )

            Row(modifier = Modifier.padding(top = 18.dp)) {
                Text(
                    text = "Don't have an account?",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp).clickable { onSignUp() },
                    text = "Sign up",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }


        }

        LaunchedEffect(uiState) {
            if (uiState is AuthUiState.Success) {
                onLoginSuccess()
            }
        }

        when(uiState){
            is AuthUiState.Error -> {
                Dialog(onDismissRequest = onDismissError) {
                    InfoDisplayComponent(
                        topText = "Something went wrong",
                        bottomText = uiState.message,
                        buttonText = "Try again",
                        buttonAction = onDismissError,
                        isError = true
                    )
                }
            }
            AuthUiState.Idle -> {}
            AuthUiState.Loading -> LoadingComponent()
            else ->{}
        }
    }
}


@Preview
@Composable
fun LoginScreenStatelessPreview() {
    KeepConsistentTheme { LoginScreenStateless(AuthUiState.Idle, onSignUp = {}, onLogin = {}) }
}