package eu.tutorials.myshoppinglistapp

import androidx.annotation.DrawableRes

sealed class Measure (val title:String,@DrawableRes val icon: Int){


   object kilogram:Measure("kg",R.drawable.kg)
    object liter:Measure("l",R.drawable.liter)

    object  piece:Measure("pcs",R.drawable.piece)





}
val measure = listOf(
   Measure.kilogram,
    Measure.liter,
    Measure.piece

)