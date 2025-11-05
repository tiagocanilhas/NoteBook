package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tiago.canilhas.notebook.R

@Composable
fun Card(
    modifier: Modifier = Modifier,
    underCardText: String = "",
    cardText: String = "",
    image: Int? = null,
    onClick: (() -> Unit)? = null
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                .background(Color.Black)
                .clickable { onClick?.invoke() }
        ) {
            Image(
                painter = painterResource(id = image ?: R.drawable.default_card_image),
                contentDescription = underCardText,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = cardText,
                fontSize = 50.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Text(text = underCardText)
    }
}

@Preview
@Composable
fun CardPreview() {
    Card(
        underCardText = "Sample Card",
        cardText = "Card Text"
    )
}