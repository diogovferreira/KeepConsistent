package com.dfcoding.keepconsistent.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theme.KeepConsistentTheme
import com.theme.PoppinsFontFamily
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_rounded_arrow_right
import keepconsistent.composeapp.generated.resources.ic_work
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CategoryItem(
    category: String,
    icon: DrawableResource,
    tasks: Int = 0,
    onCategoryClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
            .width(140.dp)
            .height(100.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(end = 20.dp).weight(1f),
                text = category,
                fontFamily = PoppinsFontFamily(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(icon),
                contentDescription = "Icon"
            )
        }

        Text(
            text = "$tasks tasks",
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .clickable(onClick = { onCategoryClick() }),
            verticalAlignment = Alignment.Bottom,

        ) {
            Text(
                text = "Go to Tasks",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                painter = painterResource(Res.drawable.ic_rounded_arrow_right),
                contentDescription = "Icon",
                modifier = Modifier.padding(start = 10.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview
@Composable
fun CategoriesItemPreview() {
    KeepConsistentTheme { CategoryItem("Work To-do", icon = Res.drawable.ic_work) }
}