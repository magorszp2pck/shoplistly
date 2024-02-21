package eu.tutorials.myshoppinglistapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.nio.file.WatchEvent

@Composable
fun Start(
viewModel: MainViewModel,
    navController: NavController
)
{

    Column(modifier= Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.second_color)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
Icon(painter= painterResource(id = R.drawable.app_logo),contentDescription = null,tint= Color.White)

            IconButton(onClick = {

                navController.navigate(Screen.BottomScreen.ShoppingList.bRoute) },modifier= Modifier
                .width(200.dp)
                .border(width = 2.dp, color = Color.White)
                .background(colorResource(id = R.color.second_color))) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Text("LET'S GET STARTED!", color = Color.White, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                    Icon(painter= painterResource(id = R.drawable.baseline_keyboard_double_arrow_right_24),contentDescription = null,tint=Color.White)
                }


        }

    }

}

