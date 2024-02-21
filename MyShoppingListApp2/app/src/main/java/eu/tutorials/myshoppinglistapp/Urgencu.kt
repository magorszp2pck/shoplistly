package eu.tutorials.myshoppinglistapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class Urgencu (val title:String,@ColorRes val color: Int,@DrawableRes val icon: Int){


    object VeryUrgent : Urgencu("Very Urgent",R.color.third_color,R.drawable.very)
    object Urgent: Urgencu("Urgent", R.color.first_color,R.drawable.urgenturgent)
    object NotUrgent : Urgencu("Not Urgent", R.color.second_color,R.drawable.notnot)



}
val urgencies = listOf(
   Urgencu.VeryUrgent,
    Urgencu.Urgent,
    Urgencu.NotUrgent

)