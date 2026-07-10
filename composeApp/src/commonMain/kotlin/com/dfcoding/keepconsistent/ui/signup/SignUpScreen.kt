package com.dfcoding.keepconsistent.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.dfcoding.keepconsistent.util.isValidEmail
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import modelrepocompose.composeapp.generated.resources.Res
import modelrepocompose.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class SignUpScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignUpViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()

        SignUpScreenStateless(
            uiState = uiState,
            goBack = { navigator.pop() },
            signUp = { viewModel.signUp(it.first, it.second) },
            onLogin = { navigator.pop() },
            onSignUpSuccess = { navigator.pop() },
            onDismissError = { viewModel.resetState() })
    }
}


@Composable
fun SignUpScreenStateless(
    uiState: SignUpUiState,
    goBack: () -> Unit = {},
    signUp: (Pair<String, String>) -> Unit = {},
    onLogin: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {},
    onDismissError: () -> Unit = {}
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val isEmailValid = isValidEmail(email)
    val doPasswordsMatch = password == confirmPassword

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding().padding(top = 40.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Icon(
                modifier = Modifier.clickable { goBack() },
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = "Back"
            )

            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = "Signup with Email",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp
            )
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppTextField(
                        placeholder = "Name",
                        value = name,
                        onValueChange = { name = it },
                        label = "Name"
                    )

                    AppTextField(
                        placeholder = "Email",
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        isError = hasAttemptedSubmit && !isEmailValid,
                    )
                    AppTextField(
                        placeholder = "Password",
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPassword = true
                    )
                    AppTextField(
                        placeholder = "Confirm Password",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm Password",
                        isPassword = true,
                        isError = hasAttemptedSubmit && !doPasswordsMatch,
                    )


                    ButtonComponent(text = "Create an Account", onClick = {
                        hasAttemptedSubmit = true
                        if (name.isNotEmpty() && isEmailValid && doPasswordsMatch) {
                            signUp(Pair(email, password))
                        }

                    })

                    Text(
                        text = "By creating an account, you agree to the Terms of Service & Privacy Policy",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Row() {
                        Text(
                            text = "Already have an account?",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 14.sp
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp).clickable { onLogin() },
                            text = "Login",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }
            }


        }


        when(uiState){
            is SignUpUiState.Success -> {
                Dialog(onDismissRequest = {}) {
                    InfoDisplayComponent(
                        topText = "Account created",
                        bottomText = "Check your email to confirm your account.",
                        buttonText = "Continue",
                        buttonAction = onSignUpSuccess,
                        isError = false
                    )
                }
            }
            is SignUpUiState.Error ->{
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

            SignUpUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            else -> {}
        }
    }
}


@Preview
@Composable
fun SignUpScreenStatelessPreview() {
    KeepConsistentTheme { SignUpScreenStateless(
        uiState = SignUpUiState.Idle,
        goBack = {},
        signUp = {},
        onLogin = {},
        onSignUpSuccess = {},
        onDismissError = {}
    ) }
}