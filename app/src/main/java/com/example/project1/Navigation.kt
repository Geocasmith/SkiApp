package com.example.project1

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.ui.theme.Project1Theme
import org.json.JSONObject


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
//        composable(route=Screens.MountainScreen.route,
//            arguments = listOf(navArgument("userId") { defaultValue = "me" }) {
//                backStackEntry -> MountainScreen(backStackEntry.arguments?.getString("mountain"),navController = navController)
//        }
                    composable(
                    Screens.MountainScreen.route+"/{userId}",
            arguments = listOf(navArgument("userId") { defaultValue = "me" })
        ) { backStackEntry ->
                        MountainScreen(backStackEntry.arguments?.getString("userId"),navController)
        }

    }
}
    @Composable
    fun HomeScreen (navController: NavController) {
        val ctx = LocalContext.current
        Project1Theme {
            Surface {
//                    SkiList(packing = items)
//                    Text(getMountainData("-43.471667","171.526444"))
                Column(modifier = Modifier.fillMaxSize()) {

//                        AsyncImage(
//                            //fill the width of screen
//                            modifier = Modifier.fillMaxWidth(),
//                            model = "https://www.weatherbit.io/static/img/icons/c04n.png",
//                            contentDescription = null
////                                https://www.weatherbit.io/api/codes
//                        )

                    val imageHutt: Painter = painterResource(id = R.drawable.mounthutt)
//                        IconButton(onClick = { navController.navigate(Screens.MountainScreen.route+"/{mounthutt}") }) {
//                            Image(painter = imageHutt, contentDescription ="" )
//                        }
                    val imageCoronet: Painter = painterResource(id = R.drawable.coronet)
//                        IconButton(onClick = { navController.navigate(Screens.MountainScreen.route+"/{coronet}") }) {
//                            Image(painter = imageCoronet, contentDescription ="" )
//                        }
                    val imageRuapehu: Painter = painterResource(id = R.drawable.ruapehu)
//                        IconButton(onClick = { navController.navigate(Screens.MountainScreen.route+"/{ruapehu}") }) {
//                            Image(painter = imageRuapehu, contentDescription ="" )
//                        }
                    val imageCardrona: Painter = painterResource(id = R.drawable.cardrona)
//                        IconButton(onClick = { navController.navigate(Screens.MountainScreen.route+"/{cardrona}") }) {
//                            Image(painter = imageCardrona, contentDescription ="" )
//                        }
                    //create lazylist of imageicons with routes to each mountain
                    val imageIcons = listOf("mounthutt", "coronet", "ruapehu", "cardrona")
                    LazyColumn(modifier = Modifier.fillMaxSize()) {

//                        mountains
                        items(imageIcons) {
                            //sets the drawable id to the mountain name
                            var drawableId = ctx.getResources()
                                .getIdentifier(it, "drawable", ctx.getPackageName());
                            val imageMountain: Painter = painterResource(id = drawableId)
                            IconButton(onClick = { navController.navigate(Screens.MountainScreen.route + "/{$it}") }) {
                                Image(painter = imageMountain, contentDescription = "")
                            }

                        }
                    }
                    //Create imageicon for ski list
                    val imageSkiList: Painter = painterResource(id = R.drawable.packing)
                    IconButton(onClick = { navController.navigate(Screens.SkiList.route) }) {
                        Image(painter = imageSkiList, contentDescription = "")
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
//@Composable
//fun MountainScreen(mountain: String, callback: VolleyCallback, navController: NavHostController) {
//
//    val weatherReport = produceState<String>(
//        initialValue = "",
//        producer = {
//            value = getMountain(latitude = 1, longitude = 1, callback =)
//        }
//    )
//}
@Composable
fun GetMountain( navController: NavHostController) {
    val url =
        "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=-43.4717&lon=171.5264"

    val weather = remember { mutableStateOf<String>("") }
// Request a string response from the provided URL.
    //make a volley request and display the response on screen
    val request = StringRequest(
        Request.Method.GET, url,
        Response.Listener<String> { response ->
            weather.value = response
            // Display the first 10 characters of the response.
            //androidx.compose.material3.Text(response.substring(0, 10))
        },
        Response.ErrorListener { error ->
//            androidx.compose.material3.Text("That didn't work!")
        }
    )
// Add the request to the RequestQueue.

}

@Composable
fun MountainScreen(mountain:String?, navController: NavHostController) {

    var latitude = "-43.471667"
    var longitude= "171.526444"
//remove the first and last characters
    val mountainNoBraces = mountain?.substring(1, mountain.length - 1)

    val ctx = LocalContext.current
    val queue = Volley.newRequestQueue(ctx)


    val url =
        "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=${latitude}&lon=${longitude}"
    var weatherDescription: JSONObject = JSONObject()
    val weatherStore = remember { mutableStateOf<String>("")}
    val codeStore = remember { mutableStateOf<String>("")}
//    val imageStore = remember { mutableStateOf<AsyncImage>("")}

// Request a string response from the provided URL.
    val stringRequest = object : StringRequest(
        Request.Method.GET, url,
        Response.Listener<String> { response ->
//            println(response.J)
            //turn response into json
            val json = JSONObject(response).getJSONArray("data").getJSONObject(0)

            //gets the key weather from the json object
            val weatherDescription = json.getJSONObject("weather")
            //gets the description from the json object
            val description = weatherDescription.getString("description")
            val code = weatherDescription.getString("code")

            weatherStore.value = description
            codeStore.value = code
        },
        Response.ErrorListener { println("ERROR") }) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["X-RapidAPI-Key"] = "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907"
            headers["X-RapidAPI-Host"] = "weatherbit-v1-mashape.p.rapidapi.com"
            return headers
        }
    }

    queue.add(stringRequest)

    Column(modifier = Modifier.fillMaxSize()) {
        //animation to fade in the image
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 200)
            ),
            exit = fadeOut()
        ) {


            //sets the drawable id to the mountain name
            var drawableId= ctx.getResources().getIdentifier(mountainNoBraces, "drawable", ctx.getPackageName());
            val imageMountain: Painter = painterResource(id = drawableId)
            Image(painter = imageMountain, contentDescription = "")
        }

        //get the title from resources
        val title = ctx.getResources().getIdentifier(mountainNoBraces+"Title", "string", ctx.getPackageName());
        Text(ctx.getString(title))

        //get the description from resources
        val description = ctx.getResources().getIdentifier(mountainNoBraces+"Description", "string", ctx.getPackageName());
        Text(ctx.getString(description))

        Text("Current Weather: ${weatherStore.value}")

        Text("Mountain: ${mountainNoBraces}")


//    Open google maps
        Button(onClick =
        {
            ctx.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=${latitude},${longitude}&daddr=${latitude},${longitude}")
                )
            )
        }
        ) {
            Text("Get Directions")
        }
    }

}