package eu.tutorials.myshoppinglistapp

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: ItemDatabase
    val itemRepository by lazy{
        ItemRepository(database.itemDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context,ItemDatabase::class.java,"shopping_listitem_database.db").build()
    }
}