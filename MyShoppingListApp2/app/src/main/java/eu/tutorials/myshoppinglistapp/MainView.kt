package eu.tutorials.myshoppinglistapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import eu.tutorials.myshoppinglistapp.screensInBottom


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView() {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember { mutableStateOf(false) }
var showDialog by remember{ mutableStateOf(false) }
    fun showHelpDialog(){
        showDialog=true
    }

    val modifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    // Allow us to find out on which "View" we current are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val modalSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp

    val bottomBar: @Composable () -> Unit = {
        if (currentRoute!="start") {
            BottomNavigation(
                Modifier.wrapContentSize(),
                backgroundColor = colorResource(id = R.color.second_color)
            ) {
                screensInBottom.forEach() { item: Screen.BottomScreen ->
                    val isSelected = currentRoute == item.bRoute
                    Log.d(
                        "Navigation",
                        "Item: ${item.bTitle}, Current Route: $currentRoute, Is Selected: $isSelected"
                    )
                    val tint = if (isSelected) androidx.compose.ui.graphics.Color.White else Color.Black
                    BottomNavigationItem(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterVertically),
                        selected = currentRoute == item.bRoute,
                        onClick = {
                            controller.navigate(item.bRoute)
                            title.value = item.bTitle
                        },
                        icon = {

                            Icon(
                                modifier = Modifier.size(34.dp),
                                tint = tint,
                                contentDescription = item.bTitle,
                                painter = painterResource(id = item.icon)
                            )
                        },
                        label = {
                            Text(
                                text = item.bTitle,
                                color = tint,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        },

                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black

                    )
                }
            }
        }
    }

        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(
                topStart = roundedCornerRadius,
                topEnd = roundedCornerRadius
            ),
            sheetContent = {
                MoreBottomSheet(modifier = modifier)
            }) {
            Scaffold(
                bottomBar = bottomBar, scaffoldState = scaffoldState,

                topBar = {
                    if(currentRoute!="start") {
                        TopAppBar(
                            backgroundColor = Color.White,
                            title = {

                                Icon(
                                    modifier = Modifier
                                        .padding(start = 60.dp, end = 8.dp)
                                        .size(200.dp),
                                    tint = colorResource(id = R.color.second_color),
                                    painter = painterResource(id = R.drawable.app_logo),

                                    contentDescription = "Settings"
                                )
                            },

                            actions = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            if (modalSheetState.isVisible)
                                                modalSheetState.hide()
                                            else
                                                modalSheetState.show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = null,
                                        tint = colorResource(id = R.color.second_color)
                                    )
                                }
                            }


                        )
                    }
                }


            ) {
                Navigation(navController = controller, viewModel = viewModel, pd = it)


            }



}



}



@Composable
fun MoreBottomSheet(modifier: Modifier){
    var showDialog by remember{ mutableStateOf(false) }
    fun showHelpDialog(){
        showDialog=true
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                colorResource(id = R.color.second_color)
            )
    ){
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween){


            Row(modifier = modifier
                .padding(16.dp)
                .clickable { showHelpDialog() }) {
                androidx.compose.material.Icon(tint= Color.White,
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_help_24),
                    contentDescription = "Help"
                )
                androidx.compose.material.Text(text = "Help", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
    }
    if(showDialog){
        Dialog(

            onDismissRequest = { showDialog=false },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        ){
            Box(
                modifier = Modifier
                    .height(500.dp)
                    .background(color = Color.White)
                    .border(
                        1.dp,
                        colorResource(id = R.color.second_color),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp)
            ){
                ShoplistlyHelp()
            }


        }

    }
}
@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {


    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.Start.bRoute, modifier = Modifier.padding(pd)
    ) {

        composable(Screen.BottomScreen.ShoppingList.bRoute) {
            ShoppingListApp(
                locationUtils = locationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formatted_address.toString()
            )
        }
         dialog("locationscreen"){
             viewModel.location.value?.let{it1 ->

                 LocationSelectionScreen(location = it1, onLocationSelected = {locationdata->
                     viewModel.fetchAddress("${locationdata.latitude},${locationdata.longitude}")
                     navController.popBackStack()
                 })
             }
         }



        composable(Screen.BottomScreen.Check.bRoute) {

            Check(viewModel)
        }
        composable(Screen.BottomScreen.Start.bRoute){
            Start(viewModel,navController = navController)
        }

        composable(Screen.BottomScreen.Add.bRoute) {
            Add( locationUtils = locationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formatted_address.toString())
        }


    }
}

@Composable
fun ShoplistlyHelp() {
    var showDialog by remember { mutableStateOf(false) }

    val textStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = colorResource(id = R.color.second_color)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)

    ) {
        // Welcome Section
        item {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = "Welcome to ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth(),
                    style = textStyle, textAlign = TextAlign.Center
                )
                Icon(painter = painterResource(id = R.drawable.app_logo),modifier= Modifier
                    .width(180.dp)
                    .height(60.dp), contentDescription = null,tint= colorResource(
                    id = R.color.second_color
                ))}
        }

        // Goal Section
        item {
            Text(
                text = "Our goal is to make your shopping and preparation easier and more efficient, all within an app that helps organize your tasks, saving you time.",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                style = textStyle
            )
        }

        // Home Page Section
        item {
            Section("Home Page") {
                ListItem("Your shopping list items are neatly organized on cards.")
                ListItem("Each card contains important information like names, categories, and addresses.")
                ListItem("Use the EDIT button to make changes or mark items as DONE to move them to the DONE list.")
                ListItem("Find the ROUTE button to discover the optimal path for each item.")
                ListItem("Sorting and filtering options in the top-right corner for urgency(that's by colors), categories, and favorites!")
            }
        }

        // ADD Page Section
        item {
            Section("Add Page") {
                ListItem("Here you can expand your list by providing all the details.")
                ListItem("Manually add the informations, then hit the CHECK button or use the X button to undo.")
            }
        }

        // Checklist Page Section
        item {
            Section("Checklist Page") {
                ListItem("A checklist-style page for your purchased items.")
                ListItem("Easily restore with a left-swipe or swipe right to completely remove items.")
            }
        }

        // Bonus Tip Section
        item {
            Section("Bonus Tip!") {
                ListItem("Check out the three dots in the top-right corner for this!")
            }
        }

        // Closing Section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Thumbs Up",
                    tint = colorResource(id = R.color.second_color),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "We hope this guide helps you make the most of Shoplistly! Feel free to reach out if you have any questions or need assistance. Happy shopping!",
                    fontSize = 14.sp,
                    style = textStyle
                )
            }
        }
    }


}

@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.second_color)
            )
        )
        content()
    }
}

@Composable
fun ListItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            tint = colorResource(id = R.color.second_color),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.second_color)
            )
        )
    }
}