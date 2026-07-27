package com.dfcoding.keepconsistent.ui.login.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.data.repository.OnBoardingRepository
import com.dfcoding.keepconsistent.ui.components.ButtonComponent
import com.dfcoding.keepconsistent.ui.login.LoginScreen
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import kotlinx.coroutines.launch
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.app_name
import keepconsistent.composeapp.generated.resources.ic_app
import keepconsistent.composeapp.generated.resources.ic_onboard_one
import keepconsistent.composeapp.generated.resources.ic_pen
import keepconsistent.composeapp.generated.resources.ic_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

class OnBoardScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val onBoardingRepository = koinInject<OnBoardingRepository>()

        OnBoardScreenStateless(onFinished = {
            onBoardingRepository.markOnboardingSeen()
            navigator.push(LoginScreen()) })
    }
}


@Composable
fun OnBoardScreenStateless(onFinished: () -> Unit = {}) {
    val pagerState = rememberPagerState(pageCount = { 2 }) // 2 pages for onboard
    val scope = rememberCoroutineScope() // this is needed to advance via button not just swipping
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.navigationBarsPadding().padding(24.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(pagerState.pageCount) { index ->
                    val isActive = index == pagerState.currentPage
                    Box(
                        modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp))
                            .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_app),
                        contentDescription = "App icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(Res.string.app_name),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                }

                Text(
                    "Login",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }

            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                when (page) {
                    0 -> OnBoardingScreenOne()
                    1 -> OnBoardingScreenTwo()

                }
            }



            ButtonComponent(
                if (isLastPage) "Get Started" else "Continue",
                onClick = {
                    if (isLastPage) onFinished() else scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                })


        }
    }

}


@Composable
fun OnBoardingScreenOne() {
    Column() {
        Text(
            "Manage your daily activities with",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 30.sp,
            maxLines = 2
        )
        Text(
            "KeepConsistent",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp
        )
        Image(
            painter = painterResource(Res.drawable.ic_onboard_one),
            contentDescription = "OnBoard image 1"
        )
        Text(
            "Stay consistent, manage tasks, and keep your group on track — the easy way.",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp
        )

    }
}

@Composable
fun OnBoardingScreenTwo() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Stay in sync",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 30.sp,
            maxLines = 2
        )
        Text(modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
            text = "Everything you need to stay organized and build consistency.",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            maxLines = 2
        )
        FeatureItem("Assign Tasks", "Split chores and habits with your group, or keep them for yourself.", Res.drawable.ic_pen)
        FeatureItem("Never Forget", "Get reminded before a task is due, so nothing slips.", Res.drawable.ic_app)
        FeatureItem("Build Streaks", "Track your consistency over time, together.", Res.drawable.ic_profile)



    }
}


@Composable
fun FeatureItem(title: String, description: String, icon: DrawableResource){

    Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)){
        Icon(painter = painterResource(icon), contentDescription = "Feature icon")
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            )
            Text(
                description,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )

        }
    }
}

@Composable
@Preview
fun OnBoardScreenPreview() {
    KeepConsistentTheme {
        OnBoardScreenStateless()
    }
}