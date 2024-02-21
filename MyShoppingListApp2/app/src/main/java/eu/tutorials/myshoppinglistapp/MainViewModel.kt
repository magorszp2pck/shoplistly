package eu.tutorials.myshoppinglistapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch



class MainViewModel(private val itemRepository: ItemRepository = Graph.itemRepository  ):ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.ShoppingList)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen:Screen){
        _currentScreen.value = screen
    }
    private  val _location= mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address: State<List<GeocodingResult>> = _address


    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latlng: String){
        viewModelScope.launch {
            val result = RetrofitClient.create().getAddressFromCoordinates(
                latlng,
                "AIzaSyAsisZVWSEEBjNQA_nRM5ctI-x1fF9VckY"
            )
            _address.value = result.results
        }

    }
    lateinit var getAllItems: Flow<List<ShoppingItem>>

    init {
        viewModelScope.launch {
            getAllItems= itemRepository.getAllItems()
        }
    }

    fun updateAnItem(item: ShoppingItem){
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.updateAnItem(item)
        }
    }
    fun getAnItem(id:Int):Flow<ShoppingItem>{
        return itemRepository.getAnItem(id)
    }
    fun addAnItem(item: ShoppingItem){
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.addAnItem(item)
        }
    }
    fun deleteAnItem(item: ShoppingItem){
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.deleteAnItem(item)
        }
    }

}