package eu.tutorials.myshoppinglistapp

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class Favs (val title:String,  @DrawableRes val icon: Int){


    object Favourite : Favs("Very Urgent",R.drawable.baseline_favorite_24)




}
val favs = listOf(
    Favs.Favourite


)