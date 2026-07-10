package com.dfcoding.keepconsistent.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import modelrepocompose.composeapp.generated.resources.Res
import modelrepocompose.composeapp.generated.resources.ic_check_circle
import modelrepocompose.composeapp.generated.resources.ic_circle_error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InfoDisplayComponent(
    topText: String,
    bottomText: String,
    buttonText: String,
    buttonAction: () -> Unit,
    isError: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .height(250.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(72.dp),
                painter = painterResource(if(isError) Res.drawable.ic_circle_error else Res.drawable.ic_check_circle),
                contentDescription = "Check Circle"
            )

            Text(
                text = topText,
                fontFamily = PoppinsFontFamily(),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = bottomText,
                fontFamily = PoppinsFontFamily(),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            ButtonComponent(
                text = buttonText,
                onClick = buttonAction
            )
        }

    }
}


@Preview
@Composable
fun InfoDisplayComponentPreview() {
    KeepConsistentTheme {
        InfoDisplayComponent(
            "Successfully Done",
            "Your task has been added successfully",
            "Continue",
            {},
            false)
    }
}