    package com.example.project1

    import android.content.Context
    import android.content.Intent
    import android.net.Uri
    import android.util.JsonReader
    import android.util.JsonWriter
    import android.view.KeyEvent.KEYCODE_ENTER
    import android.widget.Toast
    import androidx.compose.animation.*
    import androidx.compose.animation.core.tween
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.*
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowDropDown
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.ExperimentalComposeUiApi
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.painter.Painter
    import androidx.compose.ui.input.key.*
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.input.TextFieldValue
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavController
    import androidx.navigation.NavHostController
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.navArgument
    import com.android.volley.Request
    import com.android.volley.Response
    import com.android.volley.toolbox.StringRequest
    import com.android.volley.toolbox.Volley
    import com.example.project1.ui.theme.Project1Theme
    import kotlinx.coroutines.NonCancellable.cancel
    import kotlinx.coroutines.launch
    import org.json.JSONObject
    import java.io.FileNotFoundException
    import java.io.InputStreamReader
    import java.io.OutputStreamWriter


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
                SkiList()
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
                    Column() {


    //                    Text(getMountainData("-43.471667","171.526444"))
                        //create lazylist of imageicons with routes to each mountain
                        val imageIcons = listOf("mounthutt", "coronet", "ruapehu", "cardrona")

                        Column(Modifier.fillMaxSize()) {
                            LazyColumn(Modifier.weight(1f)) {

                                //                        mountains
                                items(imageIcons) {
                                    //sets the drawable id to the mountain name
                                    var drawableId = ctx.getResources()
                                        .getIdentifier(it, "drawable", ctx.getPackageName());
                                    val imageMountain: Painter = painterResource(id = drawableId)
                                    IconButton(onClick = { navController.navigate(Screens.MountainScreen.route + "/{$it}") }) {
                                        Image(
                                            painter = imageMountain,
                                            contentDescription = "",
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                }
                            }
                            //Create imageicon for ski list
                            val imageSkiList: Painter = painterResource(id = R.drawable.packing)
                            IconButton(onClick = { navController.navigate(Screens.SkiList.route) }) {
                                Image(painter = imageSkiList, contentDescription = "")
                            }
                        }
                    }

                    }

                }

            }





        @Composable()
        fun SkiListOG(navController: NavController) {
            //reads packing.txt to get json ski list
            val ctx = LocalContext.current

                val file = ctx.openFileInput("packing.json")
                val reader = JsonReader(InputStreamReader(file))
                val packing = read(reader)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                ) {
                    items(packing) { item ->
                        SkiItem(item)
                        //divider with hex color #d3d3d3
                        Divider(color = Color(0x11000100))
                    }

                }
                reader.close()
            }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable()
    fun SkiList() {
        //reads packing.txt to get json ski list
        val ctx = LocalContext.current
        val scope = rememberCoroutineScope()


        val file = ctx.openFileInput("packing.json")
        val reader = JsonReader(InputStreamReader(file))
//        val packingFromStorage = read(reader)

        val fileWrite = ctx.openFileOutput("packing.json", Context.MODE_PRIVATE)
        val writer = JsonWriter(OutputStreamWriter(fileWrite))
        val packing = remember{mutableStateListOf<String>()}

//        for (item in packingFromStorage) {
//            packing.add(item)
//        }
        val newItemInput = remember { mutableStateOf(TextFieldValue()) }

        Column() {


            TextField(
                value = newItemInput.value,
                onValueChange = {
                    newItemInput.value = it
                },
                label = { Text("New Item") },
                //on enter add the item
//                modifier = Modifier.fillMaxWidth(),
                //on enter add item to list and clear text field
                modifier = Modifier
                    .fillMaxWidth()
                    .onKeyEvent { event: KeyEvent ->
                        // handle backspace key
                        if (event.key == Key.Enter
                        // also any additional checks of the "list" i.e isNotEmpty()
                        ) {
                            scope.launch {
                                packing.add(newItemInput.value.text)
                                newItemInput.value = TextFieldValue()
                                //write the value to the json
                                write(packing, writer)
                            }
                        }
                        false
                    }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {
                items(packing) { item ->
                    SkiItem(item)
                    //divider with hex color #d3d3d3
                    Divider(color = Color(0x11000100))
                }

            }



        }
        reader.close()
    }

    fun read(reader: JsonReader): List<String> {
        var packing = listOf<String>()
        reader.beginObject()
        while (reader.hasNext()) {
            //add string to list
            reader.nextName()
            packing = packing.plus(reader.nextString())

        }
        reader.endObject()

        return packing
    }

    fun write(packing: List<String>, writer: JsonWriter) {
        writer.beginObject()

        //write each item in packing to json file
        for (item in packing) {
            writer.name(item)
            writer.value(item)
        }

        writer.endObject()
    }
    /**
    * Adds an item to the packing list in json file
     */
    @Composable
    fun addToPacking(item: String) {
        val ctx = LocalContext.current
        val file = ctx.openFileInput("packing.json")
        val reader = JsonReader(InputStreamReader(file))
        val packing = read(reader)
        reader.close()
        val writer = JsonWriter(OutputStreamWriter(ctx.openFileOutput("packing.json", Context.MODE_PRIVATE)))
        write(packing.plus(item), writer)
        writer.close()
    }


    /*
    * An item with a checkbox and text in same row
    */
    @Composable
    fun SkiItem(item: String) {
        Row(

        ) {
            val checked = remember { mutableStateOf(false) }

            Checkbox(
                checked = checked.value,
                onCheckedChange = { checked.value = it },
                colors = CheckboxDefaults.colors(),
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 33.dp)
            )
            androidx.compose.material3.Text(
                text = item,
                style = MaterialTheme.typography.headlineMedium, modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 25.dp)
            )
        }
    }

    @Composable
    fun MapView(){
        val ctx = LocalContext.current
        //create a drop down menu with an option for each mountain
        val mountain = listOf<String>(
            "Hutt", "Coronet", "Ruapehu", "Cardrona"
        )
        var open by remember { mutableStateOf(false) }

        Button(onClick = {open = !open}){
            Text ("Mountains")
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop Down Menu",
            )
        }
        DropdownMenu(
            expanded = open,
            onDismissRequest = {open = false},
        ) {
            mountain.forEach { label ->
                DropdownMenuItem(onClick = {
                    open = false


                }) {
                    Text(text = label)
                }
            }
        }



    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MountainScreen(mountain:String?, navController: NavHostController) {
        //remove the first and last characters
        val mountainNoBraces = mountain?.substring(1, mountain.length - 1)

        if(mountainNoBraces=="SkiList"){
            navController.navigate(Screens.SkiList.route)
        }else {



            var latitude = "-43.471667"
            var longitude = "171.526444"


            val ctx = LocalContext.current
            val queue = Volley.newRequestQueue(ctx)

            Toast.makeText(ctx, "Loading Weather", Toast.LENGTH_SHORT).show()


            val url =
                "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=${latitude}&lon=${longitude}"
            var weatherDescription: JSONObject = JSONObject()
            val weatherStore = remember { mutableStateOf<String>("") }
            val codeStore = remember { mutableStateOf<String>("") }
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

            //put the stringrequest in a launchedeffect to make it asynchronous
            LaunchedEffect(stringRequest) {
                //put the request in the queue
                queue.add(stringRequest)
            }

            var scrollState = rememberScrollState()
            var visible by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    //put switch with text Show Map in a row and have a toast when clicked
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Show Map", modifier = Modifier.padding(16.dp))
                        Switch(checked = visible, onCheckedChange = { visible = it })
                    }
                    //Shows the Mountain
                    var drawableId = ctx.getResources()
                        .getIdentifier(mountainNoBraces, "drawable", ctx.getPackageName());
                    val imageMountain: Painter = painterResource(id = drawableId)
                    Image(painter = imageMountain, contentDescription = "")

//                    Toast.cancel()

                    //hide/show the mountain map
                    AnimatedVisibility(
                        visible = visible,
                        enter = scaleIn(animationSpec = tween(durationMillis = 1000)),
                        exit = scaleOut(animationSpec = tween(durationMillis = 1000))


                    ) {
                        var drawableId = ctx.getResources()
                            .getIdentifier(
                                mountainNoBraces + "_map",
                                "drawable",
                                ctx.getPackageName()
                            );
                        val imageMountain: Painter = painterResource(id = drawableId)
                        Image(
                            painter = imageMountain,
                            contentDescription = "",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    //switch shows toast Hidden or Shown
                    if (visible) {
                        Toast.makeText(ctx, "Map Shown", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(ctx, "Map Hidden", Toast.LENGTH_SHORT).show()
                    }


                    //get the title from resources
                    val title = ctx.getResources()
                        .getIdentifier(mountainNoBraces + "Title", "string", ctx.getPackageName());
                    Text(ctx.getString(title), style = MaterialTheme.typography.titleLarge)

                    //get the description from resources
                    val description = ctx.getResources()
                        .getIdentifier(
                            mountainNoBraces + "Description",
                            "string",
                            ctx.getPackageName()
                        );
                    Text(ctx.getString(description))

                    Text("Current Weather: ${weatherStore.value}")

                    //            //add text box widget to type in notes
                    //            TextField(
                    //                value = "",
                    //                onValueChange = {},
                    //                label = { Text("Notes") },
                    //                modifier = Modifier.fillMaxWidth().padding(16.dp)
                    //            )
                    //            //button to save notes
                    //            Button(onClick = {
                    //                //save notes to database
                    //                //navigate to home screen
                    //                navController.navigate(R.id.homeScreen)
                    //            }) {
                    //                Text("Save")
                    //            }


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


                    //

                }
            }

            //toast to display the weather description
            //    Toast.makeText(ctx, weatherStore.value, Toast.LENGTH_LONG).show()
        }

    }