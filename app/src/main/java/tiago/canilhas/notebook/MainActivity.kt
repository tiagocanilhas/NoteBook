package tiago.canilhas.notebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import tiago.canilhas.notebook.ui.AppViewModelFactory
import tiago.canilhas.notebook.ui.Navigation
import tiago.canilhas.notebook.ui.theme.NotebookTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as Container)

        val factory = AppViewModelFactory(appContainer.repository)

        setContent {
            NotebookTheme {
                Navigation(
                    factory = factory,
                )
            }
        }
    }
}
