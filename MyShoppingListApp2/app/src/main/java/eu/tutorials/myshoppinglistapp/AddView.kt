package eu.tutorials.myshoppinglistapp


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter",
    "UnrememberedMutableState"
)
@Composable
fun Add(

    locationUtils: LocationUtils,
    viewModel: MainViewModel,
    navController: NavController,
    context: Context,
    address: String
){
    var itemUrgency by remember { mutableStateOf("") }
    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName  by remember { mutableStateOf("")}
    var oExpanded by remember { mutableStateOf(false) }
    var catExpanded by remember { mutableStateOf(false) }
    var mesExpanded by remember { mutableStateOf(false) }
    var itemQuantity  by remember { mutableStateOf("")}
    val color1= colorResource(id = R.color.second_color)
    val color2= colorResource(id = R.color.third_color)
    val color3= colorResource(id = R.color.first_color)





    var itemMeasure by remember {
        mutableStateOf("")
    }
    val snackMessage = remember{mutableStateOf("")}
    val scope = rememberCoroutineScope()
    var itemCategory by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
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

    var addresss by remember{mutableStateOf("")}
    androidx.compose.material.Scaffold(scaffoldState=scaffoldState){






            AlertDialog(onDismissRequest = { showDialog=false },
                confirmButton = {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){


                        Button(modifier= Modifier.width(125.dp), colors = ButtonDefaults.buttonColors(containerColor =
                        if(itemUrgency=="Very Urgent")  color2
                        else if(itemUrgency=="Urgent") color3
                        else color1),
                      onClick = { oExpanded = true


                        }) {
                            if(itemUrgency!=""){
                                Text(modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(start = 0.dp),text = itemUrgency,fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp,fontWeight = FontWeight.SemiBold)
                            }
                            else{
                                androidx.compose.material3.Text(text = "Urgency",fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                androidx.compose.material3.Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Arrow Down"
                                )
                            }


                        }
                        DropdownMenu(expanded = oExpanded, onDismissRequest = { oExpanded = false }) {
                            DropdownMenuItem(modifier= Modifier
                                .background(color2)
                                .padding(start = 4.dp),
                                text = { androidx.compose.material3.Text("Very Urgent",fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp,fontWeight = FontWeight.SemiBold) },
                                onClick = {
                                    oExpanded = false
                                    itemUrgency="Very Urgent"


                                }
                            )
                            DropdownMenuItem(modifier= Modifier.background(color3),
                                text = { androidx.compose.material3.Text("Urgent",fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp,fontWeight = FontWeight.SemiBold) },
                                onClick = {
                                    oExpanded = false
                                    itemUrgency="Urgent"
                                }
                            )
                            DropdownMenuItem(modifier= Modifier.background(color1),
                                text = { androidx.compose.material3.Text("Not Urgent",fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp,fontWeight = FontWeight.SemiBold) },
                                onClick = {
                                    oExpanded = false
                                    itemUrgency="Not Urgent"

                                }
                            )

                        }
                        Button(modifier= Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(start = 4.dp),onClick = {
                            if(locationUtils.hasLocationPermission(context)){
                                locationUtils.requestLocationUpdates(viewModel)

                                navController.navigate("locationscreen"){
                                    this.launchSingleTop
                                }
                                addresss = address
                            }else{
                                requestPermissionLauncher.launch(arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ))
                            }
                        }) {
                            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            if (addresss != "") {
                                Icon(
                                    modifier = Modifier.padding(end = 2.dp).size(22.dp),
                                    painter = painterResource(id = R.drawable.selected),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    text = "Address",
                                    modifier = Modifier.padding(start = 3.dp),
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.not_selected),
                                    modifier = Modifier.padding(end = 2.dp).size(22.dp),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    text = "Address",
                                    modifier = Modifier.padding(start = 3.dp),
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        }



                    }
Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
    Row(){
    Button(onClick = { catExpanded = true
    }) {
if(itemCategory==""){
    androidx.compose.material3.Text(text = "Category",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)
    androidx.compose.material3.Icon(
        Icons.Default.ArrowDropDown,
        contentDescription = "Arrow Down"
    )
}
        else if(itemCategory=="Groceries"){

    Icon(
        painter = painterResource(id = R.drawable.groceries),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp)
    )
    Text("Groceries",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Healthcare"){

    Icon(
        painter = painterResource(id = R.drawable.healthcare),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp).size(24.dp)
    )
    Text("Healthcare",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Household"){

    Icon(
        painter = painterResource(id = R.drawable.household),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp)
    )
    Text("Household",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Beauty"){

    Icon(
        painter = painterResource(id = R.drawable.beauty),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp)
    )
    Text("Beauty",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Clothing"){

    Icon(
        painter = painterResource(id = R.drawable.clothing),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp)
    )
    Text("Clothing",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Office Supplies"){

    Icon(
        painter = painterResource(id = R.drawable.office),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp)
    )
    Text("Office Supplies",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Electronics"){

    Icon(
        painter = painterResource(id = R.drawable.electronics),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp).size(24.dp)
    )
    Text("Electronics",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}
else if(itemCategory=="Pet Supplies"){

    Icon(
        painter = painterResource(id = R.drawable.pets),
        contentDescription = null,tint=Color.White
        ,modifier= Modifier.padding(end=6.dp).size(24.dp)
    )
    Text("Pet Supplies",fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)

}

    }



}
    Row(modifier= Modifier
        .padding(end = 46.dp)
        .width(50.dp)){
        DropdownMenu(modifier= Modifier
            .height(150.dp)
            .background(color1),expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
            categories.forEach {
                DropdownMenuItem(text = {Text(it.title,fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace, color = Color.White,fontSize = 12.sp, textAlign = TextAlign.Center)  }, leadingIcon = { Icon(
                    painter = painterResource(id = it.icon),
                    contentDescription = it.title,tint=Color.White,modifier=Modifier.size(24.dp)
                )}, onClick = {
                    itemCategory=it.title
                    catExpanded=false})
            }

        }



    }}




                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){

                        IconButton(onClick = {navController.navigateUp()}){
                            Column(horizontalAlignment = Alignment.CenterHorizontally){

                                Icon(modifier=Modifier.size(40.dp),painter = painterResource(id = R.drawable.cancel), contentDescription = null,tint=color1 )
                                Text(text="Cancel",fontFamily = FontFamily.Monospace, fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, color=color1)
                            }                        }
                        IconButton(onClick = {
                            if(itemName.isNotBlank()){

                                val newItem = ShoppingItem(

                                    name = itemName,
                                    quantity = itemQuantity.toInt(),
                                    address = address,
                                    urgency = itemUrgency,
                                    category = itemCategory,
                                    measure = itemMeasure
                                )
                                snackMessage.value="The item has been created"
                                viewModel.addAnItem(newItem)
                                navController.navigateUp()
                                itemName = ""
                            }
                            else
                            {
                                snackMessage.value = "Enter fields to create an item"
                            }
                            scope.launch { scaffoldState.snackbarHostState.showSnackbar(snackMessage.value) }
                        }){Column(horizontalAlignment = Alignment.CenterHorizontally){

                            Icon(modifier=Modifier.size(40.dp),painter = painterResource(id = R.drawable.add), contentDescription = null,tint=color1 )
                            Text(text="Add",fontFamily = FontFamily.Monospace, fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, color=color1)
                        }

                        }

                    }

                },
                title = { androidx.compose.material3.Text("ADD SHOPPING ITEM", modifier= Modifier.padding(start=10.dp),fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,color=color1, textAlign = TextAlign.Center) },
                text = {
                    Column {
                        Row(){
                            ShoppingNameField("Name",itemName) { itemName = it }
                        }

Row(horizontalArrangement = Arrangement.SpaceEvenly){
Column {
    ShoppingQuantityField(label = "Quantity", value = itemQuantity, onValueChanged ={itemQuantity = it } )
}
    Column {
        Row(){
            Button(modifier= Modifier
                .padding(top = 20.dp)
                .width(120.dp),onClick = { mesExpanded = true
            }) {




                if(itemMeasure!=""){
                    if(itemMeasure=="kg"){

                        Text(modifier= Modifier.padding(end=8.dp),text=Measure.kilogram.title,fontFamily = FontFamily.Monospace, fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, color=Color.White)
                        Icon(modifier= Modifier.padding(start=8.dp),
                            painter = painterResource(id = Measure.kilogram.icon),
                            contentDescription = Measure.kilogram.title,tint=Color.White
                        )

                    }
                    else if(itemMeasure=="l"){
                        Text(modifier= Modifier.padding(end=8.dp),text=Measure.liter.title,fontFamily = FontFamily.Monospace, fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, color=Color.White)
                        Icon(modifier= Modifier.padding(start=8.dp),
                            painter = painterResource(id = Measure.liter.icon),
                            contentDescription = Measure.liter.title,tint=Color.White
                        )

                    }
                    else if(itemMeasure=="pcs"){
                        Text(modifier= Modifier.padding(end=8.dp),text=Measure.piece.title,fontFamily = FontFamily.Monospace, fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold, color=Color.White)
                        Icon(modifier= Modifier.padding(start=8.dp),
                            painter = painterResource(id = Measure.piece.icon),
                            contentDescription = Measure.piece.title,tint=Color.White
                        )

                    }
                }
                else
                {
                    androidx.compose.material3.Text(text = "Measure",fontFamily = FontFamily.Monospace, fontSize = 12.sp, textAlign = TextAlign.Center,fontWeight = FontWeight.SemiBold)

                    androidx.compose.material3.Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Down"
                    )
                }



            }
        }

        Row(modifier = Modifier.padding(start=4.dp)){
            DropdownMenu(modifier= Modifier.background(color1),expanded = mesExpanded, onDismissRequest = {mesExpanded = false }) {
                measure.forEach {
                    DropdownMenuItem(text = {Text(it.title,color=Color.White)  }, trailingIcon = { Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,tint=Color.White
                    )}, onClick = {
                        itemMeasure=it.title
                        mesExpanded=false})
                }

            }
        }
    }







}

                    }
                }
            )

        }

    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingQuantityField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChanged,
        label = { androidx.compose.material3.Text(text = label, color = colorResource(id = R.color.second_color),fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, textAlign = TextAlign.Center) },
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = colorResource(id = R.color.second_color),
            focusedBorderColor = colorResource(id = R.color.second_color),
            unfocusedBorderColor = colorResource(id = R.color.second_color),
            cursorColor = colorResource(id = R.color.second_color),
            focusedLabelColor = colorResource(id = R.color.second_color),
            unfocusedLabelColor = colorResource(id = R.color.second_color),
            unfocusedTextColor = colorResource(id = R.color.second_color)


        )
    )


}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ShoppingNameField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    var focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value, onValueChange = onValueChanged,
        label = { androidx.compose.material3.Text(text = label, color = colorResource(id = R.color.second_color), fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace,fontSize = 12.sp, textAlign = TextAlign.Center) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {

                keyboardController?.hide()

            }

        ),

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = colorResource(id = R.color.second_color),
            focusedBorderColor = colorResource(id = R.color.second_color),
            unfocusedBorderColor = colorResource(id = R.color.second_color),
            cursorColor = colorResource(id = R.color.second_color),
            focusedLabelColor = colorResource(id = R.color.second_color),
            unfocusedLabelColor = colorResource(id = R.color.second_color),
            unfocusedTextColor = colorResource(id = R.color.second_color)


        )
    )


}