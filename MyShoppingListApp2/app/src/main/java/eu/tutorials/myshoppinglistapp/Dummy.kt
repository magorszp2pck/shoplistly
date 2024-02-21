package eu.tutorials.myshoppinglistapp

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon: Int, val name:String)

val libraries = listOf<Lib>(
    Lib(R.drawable.baseline_add_circle_24, "Playlist"),
    Lib(R.drawable.baseline_add_circle_24,"Artists"),
    Lib(R.drawable.baseline_add_circle_24,"Album"), Lib(
        R.drawable.baseline_checklist_24,"Songs"
    ),Lib(R.drawable.baseline_home_filled_24,"Genre")
)