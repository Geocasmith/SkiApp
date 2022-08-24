package com.example.project1

import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.project1.ui.theme.Project1Theme

@Composable
fun Navigation() {
    val navController = rememberNavController()
//    Credit to https://www.youtube.com/watch?v=4gUeyNkGE3g for teaching me how to use navigation
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
//        specify routes as strings
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.SkiList.route) {
            SkiList(navController = navController)
        }

    }
}
    @Composable
    fun HomeScreen (navController: NavController){

            Project1Theme {
                Surface {
//                    SkiList(packing = items)
//                    Text(getMountainData("-43.471667","171.526444"))
                    Column(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            //fill the width of screen
                            modifier = Modifier.fillMaxWidth(),
                            model = "https://www.weatherbit.io/static/img/icons/c04n.png",
                            contentDescription = null
//                                https://www.weatherbit.io/api/codes
                        )

                        val imageHutt: Painter = painterResource(id = R.drawable.mounthutt)
//                        Image(painter = imageHutt,contentDescription = "")
                        IconButton(onClick = { navController.navigate(Screens.SkiList.route) }) {
                            Image(painter = imageHutt, contentDescription ="" )
                        }

//                        val imageCoronet: Painter = painterResource(id = R.drawable.coronet)
//                        Image(painter = imageCoronet,contentDescription = "")

//                    androidx.compose.material3.Text(getMountainData(latitude = "1", longitude = "1").toString())
                        Button(onClick =
                        { navController.navigate(Screens.SkiList.route) }
                        ) {
                            Text("Ski List")
                        }

                        Button(onClick =
                        { navController.navigate(Screens.SkiList.route) }
                        ) {
                            Text("Weather")
                        }
                    }

                    }

                }
            }





    @Composable()
    fun SkiList(navController: NavController) {
        val packing = listOf<String>(
            "Skiis","Helmet", "Poles","Gloves","Lunch","Water Bottle","Coffee","Jacket","Socks","Boots","Shirt","Hoodie","Goggles"
        )
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
            items(packing) { item ->
                SkiItem(item)
                //divider with hex color #d3d3d3
                Divider(color = Color(0x11000100))
            }
        }

    }
