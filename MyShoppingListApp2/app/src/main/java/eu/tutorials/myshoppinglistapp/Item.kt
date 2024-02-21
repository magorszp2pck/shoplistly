package eu.tutorials.myshoppinglistapp

import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="shopping_listitem_table")
data class ShoppingItem(
    @PrimaryKey(autoGenerate= true)
    val id:Int=0,
    @ColumnInfo(name="item-name")
                        var name: String,
    @ColumnInfo(name="item-quantity")
                        var quantity:Int,
    @ColumnInfo(name="item-editing")
                        var isEditing: Boolean = false,
    @ColumnInfo(name="item-adress")
                        var address: String = "",
    @ColumnInfo(name="item-urgency")
                       var urgency: String = "",
    @ColumnInfo(name="is-done")
    var is_done: Boolean = false,
    @ColumnInfo(name="category")
    var category: String= "",
    @ColumnInfo(name="fav")
    var favourite: Boolean = false,
    @ColumnInfo(name="measure")
    var measure: String=""

)