package eu.tutorials.myshoppinglistapp
import android.net.Uri
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController

import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListApp(
    locationUtils: LocationUtils,
    viewModel: MainViewModel,
    navController: NavController,
    context: Context,
    address: String
){
    var isFav by remember {
        mutableStateOf(false)
    }
    val routeLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    val scaffoldState = rememberScaffoldState()
  var catSort by remember{ mutableStateOf(false) }
    var urSort by remember{ mutableStateOf(true) }
    var favSort by remember{ mutableStateOf(false) }
    var sortindex by remember{ mutableStateOf(0) }
    var sortExpanded by remember {
    mutableStateOf(false)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions() ,
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                // I HAVE ACCESS to location

                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if(rationaleRequired){
                    Toast.makeText(context,
                        "Location Permission is required for this feature to work", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Toast.makeText(context,
                        "Location Permission is required. Please enable it in the Android Settings",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

androidx.compose.material.Scaffold(modifier=Modifier.fillMaxWidth(),scaffoldState=scaffoldState,
    floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier
                .padding( bottom = 463.dp,start=260.dp)
                .size(30.dp),
            contentColor = Color.White,
            shape= RoundedCornerShape(2.dp),
            backgroundColor = colorResource(id = R.color.second_color) ,
            onClick = {
                if(sortindex==2){
                    sortindex=0
                }
                else
                {
                    sortindex++
                }
            }) {


                    Icon(painter = painterResource(id = sorts[sortindex].icon), contentDescription = null,modifier=Modifier.size(23.dp),tint=Color.White)





        }
    }
){
    Column(
    modifier= Modifier
        .fillMaxSize().padding(top=20.dp),
    verticalArrangement = Arrangement.Top

){


val itemList = viewModel.getAllItems.collectAsState(initial = listOf())

    LazyColumn(modifier= Modifier.fillMaxWidth().padding(top=10.dp)){
        if(sortindex==0){ urgencies.forEach(){
            stickyHeader { Row(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically){
                Icon(painter= painterResource(id = it.icon),contentDescription = null,tint= colorResource(
                    id = it.color
                ))
                LazyRow(modifier= Modifier
                    .fillMaxWidth()
                ) { items(itemList.value){
                        item ->
                    if( item.urgency==it.title && item.isEditing && item.is_done==false){
                        ShoppingItemEditor(viewModel,item = item, onEditComplete = {
                                editedName, editedQuantity,editedCategory,editedMeasure,editedUrgency ->
                            viewModel.updateAnItem(ShoppingItem(item.id,editedName,editedQuantity,false,item.address,editedUrgency,item.is_done,editedCategory,item.favourite,editedMeasure))

                        }
                        )
                    }else if(item.urgency==it.title && item.is_done==false){
                        ShoppingListItem(viewModel,item = item ,
                            onEditClick = {

                                // finding out which item we are editing and changing is "isEditing boolean" to true

                                viewModel.updateAnItem(ShoppingItem(item.id,name=item.name, quantity = item.quantity,urgency=item.urgency, address = item.address, isEditing = true, is_done = item.is_done, category = item.category, favourite = item.favourite, measure = item.measure))
                            },context)
                    }
                }
                }

            }  }}}
        if(sortindex==1){ categories.forEach(){
            stickyHeader { Row(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 0.dp,top=10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically){
                Column(modifier=Modifier.height(240.dp).width(30.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(painter= painterResource(id = it.icon),contentDescription = null,modifier=Modifier.size(28.dp),tint= colorResource(
                        id = R.color.second_color
                    ))}
                LazyRow(modifier= Modifier
                    .fillMaxWidth()
                ) { items(itemList.value){
                        item ->
                    if( item.category==it.title && item.isEditing && item.is_done==false){
                        ShoppingItemEditor(viewModel,item = item, onEditComplete = {
                                editedName, editedQuantity,editedCategory,editedMeasure,editedUrgency ->
                            viewModel.updateAnItem(ShoppingItem(item.id,editedName,editedQuantity,false,item.address,editedUrgency,item.is_done,editedCategory,item.favourite,editedMeasure))

                        }
                        )
                    }else if(item.category==it.title && item.is_done==false){
                        ShoppingListItem(viewModel,item = item ,
                            onEditClick = {

                                // finding out which item we are editing and changing is "isEditing boolean" to true

                                viewModel.updateAnItem(ShoppingItem(item.id,name=item.name, quantity = item.quantity,urgency=item.urgency, address = item.address, isEditing = true, is_done = item.is_done, category = item.category, favourite = item.favourite, measure = item.measure))
                            },context)
                    }
                }
                }

            }  }}}
        if(sortindex==2){
            favs.forEach(){
                stickyHeader { Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 0.dp,top=10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Column(modifier=Modifier.height(240.dp).width(30.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        Icon(painter= painterResource(id = it.icon),contentDescription = null,modifier=Modifier.size(28.dp),tint= colorResource(
                            id = R.color.second_color
                        ))}
                    LazyRow(modifier= Modifier
                        .fillMaxWidth()
                    ) { items(itemList.value){
                            item ->
                        if( item.favourite==true && item.isEditing && item.is_done==false){
                            ShoppingItemEditor(viewModel,item = item, onEditComplete = {
                                    editedName, editedQuantity,editedCategory,editedMeasure,editedUrgency ->
                                viewModel.updateAnItem(ShoppingItem(item.id,editedName,editedQuantity,false,item.address,editedUrgency,item.is_done,editedCategory,item.favourite,editedMeasure))

                            }
                            )
                        }else if(item.favourite==true && item.is_done==false){
                            ShoppingListItem(viewModel,item = item ,
                                onEditClick = {

                                    // finding out which item we are editing and changing is "isEditing boolean" to true

                                    viewModel.updateAnItem(ShoppingItem(item.id,name=item.name, quantity = item.quantity,urgency=item.urgency, address = item.address, isEditing = true, is_done = item.is_done, category = item.category, favourite = item.favourite, measure = item.measure))
                                },context)
                        }
                    }
                    }

                }  }}
        }


    }


 }


}}



@Composable
fun ShoppingItemEditor(viewModel:MainViewModel,item: ShoppingItem, onEditComplete: (String, Int,String,String,String) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var editedCategory by remember{ mutableStateOf(item.category) }
    var editedAddress by remember {
        mutableStateOf(item.address)}
    var editedMeasure by remember {
        mutableStateOf(item.measure)
    }
    var editedUrgeny by remember { mutableStateOf(item.urgency) }

    var catListClicked by remember{ mutableStateOf(false) }
var mesListClicked by remember { mutableStateOf(false) }
    var urListClicked by remember { mutableStateOf(false) }
    var ur_index by remember{ mutableStateOf(0) }

    Card(modifier = Modifier
        .padding(16.dp)
        .size(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(if(editedUrgeny=="Very Urgent") R.color.third_color
            else if(editedUrgeny=="Urgent") R.color.first_color else R.color.second_color),
        ),
        border = BorderStroke(3.dp, color =colorResource(if(editedUrgeny=="Very Urgent") R.color.third_color
        else if(editedUrgeny=="Urgent") R.color.first_color else R.color.second_color))) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    colorResource(
                        if (editedUrgeny == "Very Urgent") R.color.third_color
                        else if (editedUrgeny == "Urgent") R.color.first_color else R.color.second_color
                    )
                )
                .padding(8.dp),

        )
        {
            Column() {
                ShoppingNameEditField(
                    value = editedName,
                    onValueChanged = { editedName = it },
                    label="Name"
                )

                Row(verticalAlignment = Alignment.CenterVertically){
                    ShoppingQuantityEditField(
                        value = editedQuantity,
                        onValueChanged = { editedQuantity = it },
                        label="Quantity"
                    )
                    if(mesListClicked==true)
                    {
                        LazyColumn(modifier= Modifier
                            .size(36.dp)
                            .border(width = 1.dp, color = Color.White)
                            .clip(
                                RoundedCornerShape(100.dp)
                            )){
                            items(measure) { mes->
                                IconButton(onClick = { editedMeasure=mes.title
                                    mesListClicked=false}) {

                                    Icon(painter= painterResource(id = mes.icon),tint=Color.White, contentDescription = "",modifier=Modifier.size(28.dp))

                                }
                            }
                        }

                    }
                    else
                    {
                        Row(modifier = Modifier.size(32.dp)){
                            measure.forEach(){
                                if(editedMeasure==it.title){
                                    IconButton(onClick = {
                                        mesListClicked=true}) {

                                        Icon(painter= painterResource(id = it.icon),tint=Color.White, contentDescription = "",modifier=Modifier.size(28.dp))

                                    }                                }


                            }
                        }
                    }

                }
                Row(modifier= Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    if(catListClicked==true)
                    {
                        LazyColumn(modifier= Modifier
                            .size(36.dp)
                            .border(width = 1.dp, color = Color.White)
                            .clip(
                                RoundedCornerShape(8.dp)
                            )){
                            items(categories) { cat->
                                IconButton(onClick = { editedCategory=cat.title
                                catListClicked=false}) {
                                    Icon(painter= painterResource(id = cat.icon),tint=Color.White, contentDescription = "",modifier=Modifier.size(28.dp))

                                }
                            }
                        }

                    }
                    else
                    {
                        Column(modifier = Modifier.size(32.dp)){
                            categories.forEach(){
                                if(editedCategory==it.title){
                                    IconButton(onClick = {
                                        catListClicked=true}) {
                                        Icon(painter= painterResource(id = it.icon),tint=Color.White, contentDescription = "",modifier=Modifier.size(28.dp))

                                    }                                }


                            }
                        }
                    }






                        Row(modifier = Modifier.size(28.dp)){


                                Box(modifier= Modifier
                                    .fillMaxSize()
                                    .background(colorResource(id = urgencies[ur_index].color))
                                    .border(width = 1.dp, color = Color.White)
                                    .clickable {
                                        if (ur_index == 2) {
                                            ur_index = 0
                                        } else {
                                            ur_index++
                                        }
                                        editedUrgeny = urgencies[ur_index].title


                                    })



                        }

                    IconButton(
                        onClick = {

                            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1,editedCategory,editedMeasure,editedUrgeny)
                        }
                    ) {
                        Icon(painterResource(id = R.drawable.baseline_save_24),tint=Color.White, contentDescription = "",modifier= Modifier.size(28.dp))
                    }

                }

            }


        }
    }


}


@Composable
fun ShoppingListItem(
    viewModel: MainViewModel,
    item: ShoppingItem,
    onEditClick: () -> Unit,context:Context

){
    val address_parts=item.address.split(", ")
    val address_form=address_parts.dropLast(2).joinToString(" ")
    var selectedTransportMode by remember { mutableStateOf("driving") }
    var showDialog by remember { mutableStateOf(false) }
    Card(modifier = Modifier
        .padding(16.dp)
        .size(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(if(item.urgency=="Very Urgent") R.color.third_color
            else if(item.urgency=="Urgent") R.color.first_color else R.color.second_color),
        ),
        border = BorderStroke(3.dp, color = colorResource(if(item.urgency=="Very Urgent") R.color.third_color
        else if(item.urgency=="Urgent") R.color.first_color else R.color.second_color))){
        Column(modifier = Modifier
            .weight(1f)
            .padding(10.dp)
            .fillMaxSize()) {
            Row(modifier= Modifier
                .fillMaxWidth()
                .height(25.dp), horizontalArrangement = Arrangement.SpaceBetween){

                    Row{  categories.forEach { if(it.title==item.category) Icon(painter= painterResource(id = it.icon), contentDescription = null,tint=Color.White) }
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
        Icon(painter= painterResource(id = R.drawable.baseline_favorite_border_24),modifier=Modifier.padding(end=0.dp),tint=Color.White, contentDescription = null)
    }
    else
    {
        Icon(painter= painterResource(id = R.drawable.baseline_favorite_24),modifier=Modifier.padding(end=0.dp),tint=Color.White, contentDescription = null)
    }
}
                }

            }
            Row(modifier= Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Row{ measure.forEach { if(it.title==item.measure) Icon(painter= painterResource(id = it.icon), contentDescription = null,tint=Color.White) }
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
                Icon(imageVector = Icons.Default.LocationOn,contentDescription = null,tint= Color.White)
                Text(text = ": $address_form", color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 15.sp)
            }
        }

        Row(modifier = Modifier
            .height(32.dp)){
            IconButton(modifier=Modifier.padding(start=12.dp,end=6.dp,bottom=10.dp),onClick = onEditClick){
                Icon( painterResource(id = R.drawable.baseline_edit_square_24), contentDescription = null,tint=Color.White)
            }
            IconButton(modifier=Modifier.padding(start=12.dp,end=6.dp,bottom=10.dp),onClick = {showDialog=true }) {
                Icon(painterResource(id = R.drawable.baseline_route_24), contentDescription = null,tint= Color.White)


            }
            if(showDialog){
                TransportModeDialog(
                    selectedTransportMode = selectedTransportMode,
                    onTransportModeSelected = {
                        selectedTransportMode = it
                        showDialog = false

                        // Launch Google Maps with the selected transport mode
                        val uri = Uri.parse("google.navigation:q=${item.address}&mode=$selectedTransportMode")
                        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    }
                )

            }
            IconButton(modifier=Modifier.padding(start=12.dp,end=6.dp,bottom=10.dp),onClick = {viewModel.updateAnItem(ShoppingItem(id=item.id,item.name,item.quantity,false,item.address,item.urgency,is_done = true,item.category,item.favourite,item.measure))}){
                Icon(painterResource(id = R.drawable.baseline_done_outline_24), contentDescription = null,tint= Color.White)
            }


        }
    }
}

@Composable
fun TransportModeButton(
icon:Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor  = if (isSelected) Color.Gray else Color.Unspecified
        )
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontFamily = FontFamily.Monospace)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportModeDialog(
    selectedTransportMode: String,
    onTransportModeSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* No action on dismiss */ },

        content={    Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            TransportModeButton(
R.drawable.car,
                label = "Driving",
                isSelected = selectedTransportMode == "driving"
            ) { onTransportModeSelected("d") }

            TransportModeButton(
R.drawable.walk,
                label = "Walking",
                isSelected = selectedTransportMode == "w"
            ) { onTransportModeSelected("w") }



            TransportModeButton(
R.drawable.bus,
                label = "Bus",
                isSelected = selectedTransportMode == "transit"
            ) { onTransportModeSelected("transit") }
        }}



    )
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingQuantityEditField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChanged,
        label = { androidx.compose.material3.Text(text = label, color=Color.White,fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, textAlign = TextAlign.Center) },
        modifier = Modifier
            .width(150.dp)
            .padding(6.dp)
            .height(55.dp),
        textStyle = TextStyle(fontSize = 10.sp,color=Color.White,fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            unfocusedTextColor = Color.White


        )
    )


}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ShoppingNameEditField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    var focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value, onValueChange = onValueChanged,
        label = { androidx.compose.material3.Text(text = label, color = Color.White, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace,fontSize = 10.sp, textAlign = TextAlign.Center) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(55.dp)
            .focusRequester(focusRequester),
        textStyle = TextStyle(fontSize = 10.sp,color=Color.White,fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {

                keyboardController?.hide()

            }

        ),

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            unfocusedTextColor = Color.White

        )
    )


}


