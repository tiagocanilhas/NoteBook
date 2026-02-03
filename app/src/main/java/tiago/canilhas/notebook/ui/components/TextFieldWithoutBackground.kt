package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldWithoutBackground(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.LightGray)
            )
        },
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.LightGray
        ),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun TextFieldWithoutBackgroundPreview() {
    TextFieldWithoutBackground(
        value = "Sample Title",
        onValueChange = {}
    )
}