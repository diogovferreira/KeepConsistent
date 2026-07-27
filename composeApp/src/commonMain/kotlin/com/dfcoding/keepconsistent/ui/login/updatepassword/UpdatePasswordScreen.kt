package com.dfcoding.keepconsistent.ui.login.updatepassword

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
import com.dfcoding.keepconsistent.deeplink.PasswordRecoveryState
import com.dfcoding.keepconsistent.navigation.RootScreen
import com.dfcoding.keepconsistent.ui.components.AppTextField
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.components.InfoDisplayComponent
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class UpdatePasswordScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<UpdatePasswordScreenViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        UpdatePasswordScreenStateless(
            goBack = { navigator.pop() },
            updatePassword = { viewModel.updatePassword(it) },
            onDismissError = { viewModel.resetState() },
            uiState = uiState,
            onSuccess = {
                PasswordRecoveryState.consume()
                navigator.replaceAll(RootScreen()) })
    }
}

@Composable
fun UpdatePasswordScreenStateless(
    goBack: () -> Unit = {},
    updatePassword: (String) -> Unit = {},
    uiState: UpdatePasswordUiState = UpdatePasswordUiState.Idle,
    onDismissError: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                text = "New Password",
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
                        isError = !doPasswordsMatch,
                    )


                    ButtonComponent(text = "Update", onClick = {
                        if (doPasswordsMatch) {
                            updatePassword(password)
                        }

                    })
                }
            }
        }
    }

    LaunchedEffect(uiState) { if (uiState is UpdatePasswordUiState.Success) onSuccess() }


    when (uiState) {
        is UpdatePasswordUiState.Error -> Dialog(onDismissRequest = onDismissError) {
            InfoDisplayComponent(
                "Something went wrong",
                uiState.message,
                "Try again",
                onDismissError,
                isError = true
            )
        }

        UpdatePasswordUiState.Loading -> LoadingComponent()
        else -> {}
    }
}


@Preview
@Composable
fun UpdatePasswordScreenStatelessPreview() {
    KeepConsistentTheme { UpdatePasswordScreenStateless(goBack = {}) }
}