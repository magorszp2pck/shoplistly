package eu.tutorials.myshoppinglistapp

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreen(
        val bTitle: String, val bRoute: String, @DrawableRes val icon: Int
    ) : Screen(bTitle, bRoute) {
        object ShoppingList : BottomScreen("Home", "shopping_list", R.drawable.baseline_home_filled_24)

        object Add : BottomScreen(
            "Add", "add", R.drawable.baseline_add_circle_24
        )

        object Check : BottomScreen(
            "Check", "check",
            R.drawable.baseline_checklist_24
        )
        object Start: BottomScreen(
            "Start","start",R.drawable.baseline_keyboard_double_arrow_right_24
        )
    }
}




val screensInBottom = listOf(
    Screen.BottomScreen.ShoppingList,
    Screen.BottomScreen.Add,
    Screen.BottomScreen.Check

)

