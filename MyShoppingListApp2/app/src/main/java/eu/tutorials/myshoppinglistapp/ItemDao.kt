package eu.tutorials.myshoppinglistapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addAnItem(itemEntity:ShoppingItem)

    @Query("SELECT * FROM `shopping_listitem_table`")
    abstract fun getAllItems(): Flow<List<ShoppingItem>>

    @Update
    abstract suspend fun updateAnItem(wishEntity:ShoppingItem)

    @Delete
    abstract suspend fun deleteAnItem(wishEntity: ShoppingItem)

    @Query("Select * from `shopping_listitem_table` WHERE id=:id")
    abstract fun getAnItem(id:Int):Flow<ShoppingItem>
}