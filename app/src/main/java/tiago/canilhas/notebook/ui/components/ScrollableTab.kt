package tiago.canilhas.notebook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tiago.canilhas.notebook.R

@Composable
fun ScrollableTab(
    options: List<String>,
    selectedIndex: Int,
    backgroundColor: Color,
    optionSelectedColor: Color,
    optionUnselectedColor: Color,
    addColor: Color,
    onOptionSelected: (Int) -> Unit,
    onAddClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .border(Tab.TAB_BORDER_SIZE.dp, Tab.BORDER_COLOR),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            itemsIndexed(items = options, key = { index, option -> "$index-$option" }) { index, option ->
                TabOption(
                    option = option,
                    isSelected = index == selectedIndex,
                    selectedColor = optionSelectedColor,
                    unselectedColor = optionUnselectedColor,
                    onOptionSelected = { onOptionSelected(index) }
                )
            }
        }

        TabAdd(
            color = addColor,
            onAddSelected = onAddClicked
        )
    }

}

object Tab {
    const val OPTION_HEIGHT = 40
    const val OPTION_WIDTH = 200
    const val PADDING = 2
    const val OPTION_BORDER_SIZE = 0.1f
    const val TAB_BORDER_SIZE = 1f
    val BORDER_COLOR = Color.Black

    const val FONT_SIZE = 28
    val TEXT_COLOR = Color.White
}

@Composable
fun TabOption(
    option: String,
    isSelected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    onOptionSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(Tab.OPTION_WIDTH.dp)
            .height(Tab.OPTION_HEIGHT.dp)
            .background(color =
                if (isSelected) selectedColor
                else unselectedColor
            )
            .border(Tab.OPTION_BORDER_SIZE.dp, Tab.BORDER_COLOR)
            .clickable { onOptionSelected() }
            .padding(Tab.PADDING.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = option,
            fontSize = Tab.FONT_SIZE.sp,
            color = Tab.TEXT_COLOR,
        )
    }
}


@Composable
fun TabAdd(
    onAddSelected: () -> Unit,
    color: Color,
) {
    Box(
        modifier = Modifier
            .width(Tab.OPTION_WIDTH.dp)
            .height(Tab.OPTION_HEIGHT.dp)
            .border(Tab.OPTION_BORDER_SIZE.dp, Tab.BORDER_COLOR)
            .background(color = color)
            .clickable { onAddSelected() }
            .padding(Tab.PADDING.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "+",
            fontSize = Tab.FONT_SIZE.sp,
            color = Tab.TEXT_COLOR,
        )
    }
}

@Preview
@Composable
fun ScrollableTabPreview() {
    ScrollableTab(
        options = listOf("Tab 1", "Tab 2", "Tab 3"),
        selectedIndex = 0,
        backgroundColor = colorResource(R.color.section_tab_background),
        optionSelectedColor = colorResource(R.color.section_tab_option_selected),
        optionUnselectedColor = colorResource(R.color.section_tab_option_unselected),
        addColor = colorResource(R.color.section_tab_add),
        onOptionSelected = {},
        onAddClicked = {}
    )
}