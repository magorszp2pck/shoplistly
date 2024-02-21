package eu.tutorials.myshoppinglistapp



import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class Categories (val title:String,@DrawableRes val icon: Int){


    object Groceries: Categories ("Groceries",R.drawable.groceries)
    object Healthcare: Categories ("Healthcare",R.drawable.healthcare)
    object Household: Categories ("Household",R.drawable.household)
    object Clothing: Categories ("Clothing",R.drawable.clothing)
    object Electronics: Categories ("Electronics",R.drawable.electronics)
    object Beauty: Categories ("Beauty",R.drawable.beauty)
    object Office: Categories ("Office Supplies",R.drawable.office)
    object Pets: Categories ("Pet Supplies",R.drawable.pets)





}
val categories = listOf(
    Categories.Groceries,
    Categories.Healthcare,
    Categories.Household,
    Categories.Beauty,
    Categories.Clothing,
    Categories.Office,
    Categories.Electronics,
    Categories.Pets

)