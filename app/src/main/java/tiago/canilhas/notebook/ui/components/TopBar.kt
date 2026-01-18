package tiago.canilhas.notebook.ui.components

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tiago.canilhas.notebook.R

@Composable
fun TopBar(
    toggleTab: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .height(80.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = R.drawable.tab_icon),
            contentDescription = "tab_icon",
            modifier = Modifier
                .size(60.dp)
                .clickable { toggleTab() }
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        toggleTab = {}
    )
}