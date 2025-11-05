package tiago.canilhas.notebook.ui.components

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    toggleTab: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .height(80.dp)
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(
            onClick = toggleTab
        ) {
            Text(text = "Toggle Tab")
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        toggleTab = {}
    )
}