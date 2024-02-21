package eu.tutorials.myshoppinglistapp

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class Sorts (val title:String,  @DrawableRes val icon: Int){


    object Category : Sorts("Very Urgent",R.drawable.category)
    object Urgency: Sorts("Urgent", R.drawable.urgenciess)
    object Fav : Sorts("Not Urgent", R.drawable.baseline_favorite_24)



}
val sorts = listOf(
    Sorts.Urgency,
    Sorts.Category,
    Sorts.Fav

)