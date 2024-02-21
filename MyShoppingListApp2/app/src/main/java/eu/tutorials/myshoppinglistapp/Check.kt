package eu.tutorials.myshoppinglistapp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Check(
    viewModel: MainViewModel
){
     val mainScope = CoroutineScope(Dispatchers.Main)

val listState :LazyListState = rememberLazyListState()
   val itemlist = viewModel.getAllItems.collectAsState(initial = listOf())
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .fillMaxWidth(),state= listState
        ){
        items(itemlist.value, key={item->item.id}){
                item ->
if(item.is_done==true ){
    val dismissState = rememberDismissState(confirmStateChange = {
        if(it == DismissValue.DismissedToEnd ){
         viewModel.deleteAnItem(item)
        }
        else if(it==DismissValue.DismissedToStart){

            viewModel.updateAnItem(ShoppingItem(item.id,item.name,item.quantity,item.isEditing,item.address,item.urgency,false,item.category,item.favourite,item.measure))
        }
        true
    })
    SwipeToDismiss(state = dismissState,
        background = {
            val color by animateColorAsState(if(dismissState.dismissDirection
                == DismissDirection.EndToStart) colorResource(id = R.color.backup_yellow)
            else if(dismissState.dismissDirection
                == DismissDirection.StartToEnd)Color.Red else Color.Transparent
                , label = "")
            val alignment=  if(dismissState.dismissDirection
                == DismissDirection.EndToStart)  Alignment.CenterEnd
            else if(dismissState.dismissDirection
                == DismissDirection.StartToEnd)  Alignment.CenterStart
            else Alignment.Center


            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ){
                if(dismissState.dismissDirection
                    == DismissDirection.StartToEnd)  Icon(Icons.Default.Delete, contentDescription = "Done",tint=Color.White)
                else if(dismissState.dismissDirection
                    == DismissDirection.EndToStart)  Icon(painter= painterResource(id = R.drawable.baseline_settings_backup_restore_24), contentDescription = "Delete",tint=Color.White)

            }

        },
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.35f) },
        dismissContent = {
            ShoppingListDoneItem(viewModel = viewModel, item = item) {

            }

        })
}



        }
    }

}

@Composable
fun ShoppingListDoneItem(
    viewModel: MainViewModel,
    item: ShoppingItem,
    onEditClick: () -> Unit

){
    val address_parts=item.address.split(", ")
    val address_form=address_parts.dropLast(2).joinToString(" ")

    Card(modifier = Modifier
        .padding(16.dp)
        .height(150.dp)
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(if(item.urgency=="Very Urgent") R.color.third_color
            else if(item.urgency=="Urgent") R.color.first_color else R.color.second_color),
        ),
        border = BorderStroke(3.dp, color = colorResource(if(item.urgency=="Very Urgent") R.color.third_color
        else if(item.urgency=="Urgent") R.color.first_color else R.color.second_color))){
        Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Icon(painter= painterResource(id = R.drawable.checkbox),tint=Color.White,modifier= Modifier
                .padding(25.dp)
                .size(40.dp), contentDescription = null)
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(130.dp)
                    .background(Color.White)
            )
            Column(){
                Column(modifier = Modifier
                    .padding(10.dp)
                    .width(200.dp)) {
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .height(25.dp), horizontalArrangement = Arrangement.SpaceBetween){

                        Row{  categories.forEach { if(it.title==item.category) androidx.compose.material3.Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = null,
                            tint = Color.White
                        )
                        }
                            Text(text = ": ${item.name}", color = Color.White,  fontFamily = FontFamily.Monospace, fontSize = 15.sp)}


                        Row(horizontalArrangement = Arrangement.End){

                            IconButton(modifier=Modifier.padding(end=0.dp),onClick = { if(item.favourite==false){
                                viewModel.updateAnItem(ShoppingItem(item.id,item.name,item.quantity,item.isEditing,item.address,item.urgency,item.is_done,item.category,true,item.measure))
                            }
                            else
                            {
                                viewModel.updateAnItem(ShoppingItem(item.id,item.name,item.quantity,item.isEditing,item.address,item.urgency,item.is_done,item.category,false,item.measure))
                            }
                            }) {
                                if(item.favourite==false){
                                    androidx.compose.material3.Icon(
                                        painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                                        modifier = Modifier.padding(end = 0.dp),
                                        tint = Color.White,
                                        contentDescription = null
                                    )
                                }
                                else
                                {
                                    androidx.compose.material3.Icon(
                                        painter = painterResource(id = R.drawable.baseline_favorite_24),
                                        modifier = Modifier.padding(end = 0.dp),
                                        tint = Color.White,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                    }
                    Row(modifier= Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Row{ measure.forEach { if(it.title==item.measure) androidx.compose.material3.Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = null,
                            tint = Color.White
                        )
                        }
                            Text(text = ": ${item.quantity}", color = Color.White,  fontFamily = FontFamily.Monospace, fontSize = 15.sp)
                            measure.forEach { if(it.title==item.measure)
                            {
                                Text(text= it.title,color = Color.White,  fontFamily = FontFamily.Monospace, fontSize = 15.sp,modifier= Modifier.padding(start=4.dp))

                            }}

                        }

                    }
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                        .padding(top = 4.dp)){
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(text = ": $address_form", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 15.sp)
                    }
                }


            }

        }


    }
}

