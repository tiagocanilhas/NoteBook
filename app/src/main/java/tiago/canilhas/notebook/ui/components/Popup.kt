package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import tiago.canilhas.notebook.R

@Composable
fun Popup (
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    onAccept: () -> Unit,
    onAcceptText: String,
    onDismiss: () -> Unit,
) {
    val initialValue = rememberSaveable { value }
    val isDisabled = value.isBlank() || value == initialValue

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .width(500.dp)
                .background(Color.DarkGray, RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Button(onClick = onAccept, enabled = !isDisabled) {
                    Text(onAcceptText)
                }
                Button(onClick = onDismiss) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        }
    }
}