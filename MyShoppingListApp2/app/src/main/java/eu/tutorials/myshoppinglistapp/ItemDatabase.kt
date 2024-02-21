package eu.tutorials.myshoppinglistapp

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [ShoppingItem::class],
    version=1,
    exportSchema = false
)
abstract class ItemDatabase : RoomDatabase(){
    abstract fun itemDao(): ItemDao
}