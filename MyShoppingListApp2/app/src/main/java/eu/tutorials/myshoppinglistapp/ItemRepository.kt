package eu.tutorials.myshoppinglistapp

import kotlinx.coroutines.flow.Flow


class ItemRepository(private val itemDao: ItemDao) {
    suspend fun addAnItem(item:ShoppingItem){
        itemDao.addAnItem(item)
    }
    fun getAllItems():Flow<List<ShoppingItem>> = itemDao.getAllItems()
    fun getAnItem(id:Int): Flow<ShoppingItem>{
        return itemDao.getAnItem(id)
    }
    suspend fun updateAnItem(item:ShoppingItem){
        itemDao.updateAnItem(item)
    }
    suspend fun deleteAnItem(item:ShoppingItem){
        itemDao.deleteAnItem(item)
    }
}