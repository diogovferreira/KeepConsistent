package com.dfcoding.keepconsistent.ui.login.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.dfcoding.keepconsistent.util.isValidEmail
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class ForgotPasswordScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<ForgotPasswordScreenViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        ForgotPasswordScreenStateless(
            goBack = { navigator.pop() },
            recoverPassword = { viewModel.recoverPassword(email = it) },
            uiState = uiState,
            onDismissError = { viewModel.resetState() }
        )
    }

}


@Composable
fun ForgotPasswordScreenStateless(
    goBack: () -> Unit = {},
    recoverPassword: (String) -> Unit = {},
    uiState: ForgotPasswordUiState,
    onDismissError: () -> Unit = {}
) {

    var email by remember { mutableStateOf("") }
    val isEmailValid = isValidEmail(email)
    var hasAttemptedSubmit by remember { mutableStateOf(false)}

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
                text = "Recover Password",
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
                        placeholder = "Email",
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        isError = hasAttemptedSubmit && !isEmailValid,
                    )



                    ButtonComponent(text = "Recover Password", onClick = {
                        hasAttemptedSubmit = true
                        if (isEmailValid && hasAttemptedSubmit) {
                            recoverPassword(email)
                        }

                    })

                }
            }


        }
    }

    when (uiState) {
        is ForgotPasswordUiState.Success -> {
            Dialog(onDismissRequest = {}) {
                InfoDisplayComponent(
                    "Check your email",
                    "We sent a password reset link to $email.",
                    "Back to login",
                    goBack,
                    isError = false
                )
            }
        }

        is ForgotPasswordUiState.Error -> {
            Dialog(onDismissRequest = onDismissError) {
                InfoDisplayComponent(
                    "Something went wrong",
                    uiState.message,
                    "Try again",
                    onDismissError,
                    isError = true
                )
            }
        }

        ForgotPasswordUiState.Loading -> LoadingComponent()

        else -> {}
    }
}

@Preview
@Composable
fun ForgotPasswordScreenStatelessPreview() {
    KeepConsistentTheme { ForgotPasswordScreenStateless(goBack = {}, recoverPassword = {}, uiState = ForgotPasswordUiState.Idle) }

}


