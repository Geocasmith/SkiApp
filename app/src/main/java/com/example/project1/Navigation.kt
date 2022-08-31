    package com.example.project1

    import android.content.Intent
    import android.net.Uri
    import android.widget.Toast
    import androidx.compose.animation.AnimatedVisibility
    import androidx.compose.animation.ExperimentalAnimationApi
    import androidx.compose.animation.core.tween
    import androidx.compose.animation.scaleIn
    import androidx.compose.animation.scaleOut
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.*
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.*
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.ExperimentalComposeUiApi
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.painter.Painter
    import androidx.compose.ui.input.key.Key
    import androidx.compose.ui.input.key.KeyEvent
    import androidx.compose.ui.input.key.key
    import androidx.compose.ui.input.key.onKeyEvent
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.ViewModel
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
    import kotlinx.coroutines.launch
    import org.json.JSONObject

    @Composable
    fun Navigation(packingViewModel: packingViewModel) {
        val navController = rememberNavController()
    //    Credit to https://www.youtube.com/watch?v=4gUeyNkGE3g for teaching me how to use navigation
        NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
    //        specify routes as strings
            composable(Screens.HomeScreen.route) {
                HomeScreen(navController = navController)
            }
            composable(Screens.SkiList.route) {

                SkiList(packingViewModel)
            }
            composable(Screens.MountainScreen.route+"/{userId}",
                arguments = listOf(navArgument("userId") { defaultValue = "me" })
            ) { entryPoint -> MountainScreen(entryPoint.arguments?.getString("userId"),navController,packingViewModel) }
            }

        }


    /**
     * Home screen for the app. Displays the list of mountains and ski packing iconButtons.
     */
        @Composable
        fun HomeScreen (navController: NavController) {
            val ctx = LocalContext.current
            Project1Theme {
                Surface {
                    Column() {
                        //create lazylist of imageicons with routes to each mountain
                        val imageIcons = listOf("mounthutt", "coronet", "ruapehu", "cardrona","packing")

                        Column(Modifier.fillMaxWidth()) {
                            LazyColumn(Modifier.weight(1f)) {
                                items(imageIcons) {

                                    //sets the drawable id to the mountain name. Gets and draws the mountain imageIcon
                                    var drawableId = ctx.getResources()
                                        .getIdentifier(it, "drawable", ctx.getPackageName());
                                    val imageMountain: Painter = painterResource(id = drawableId)
                                    IconButton(onClick = { navController.navigate(Screens.MountainScreen.route + "/{$it}") }) {
                                        Image(
                                            painter = imageMountain,
                                            contentDescription = ctx.getString(R.string.contentDescription),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                }
                            }
                        }
                    }

                    }

                }

            }

    /**
     * Displays the packing checklist of items to pack
     */
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable()
    fun SkiList(packingViewModel: packingViewModel) {

        //reads packing.txt to get json ski list
        val ctx = LocalContext.current
        val scope = rememberCoroutineScope()
        Column() {


            TextField(
                value = packingViewModel.newItemInput,
                onValueChange = {
                    packingViewModel.changeNewItemInput(it)
                },
                label = { Text(text = ctx.getString(R.string.newItem)) },
                modifier = Modifier
                    .fillMaxWidth()
                        //pressing enter adds to the list
                    .onKeyEvent { event: KeyEvent ->
                        if (event.key == Key.Enter
                        ) {
                            scope.launch {
                                packingViewModel.addItem(packingViewModel.newItemInput)
                                packingViewModel.changeNewItemInput("")
                            }
                        }
                        false
                    }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(packingViewModel.packing) { item ->
                    SkiItem(item)
                    //divider with hex color #d3d3d3
                    Divider(color = Color(0x11000100))
                }

            }



        }
    }

    /*
    * An item with a checkbox and text in same row
    */
    @Composable
    fun SkiItem(item: String) {
        Row(
        ) {
            val checked = rememberSaveable { mutableStateOf(false) }
            Checkbox(
                checked = checked.value,
                onCheckedChange = { checked.value = it },
                colors = CheckboxDefaults.colors(),
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 33.dp)
            )
            Text(
                text = item,
                style = MaterialTheme.typography.headlineMedium, modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 25.dp)
            )
        }
    }

    /**
     * Displays the mountain information
     */
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MountainScreen(mountain:String?, navController: NavHostController,packingViewModel: packingViewModel) {
        //remove the first and last characters
        val mountainNoBraces = mountain?.substring(1, mountain.length - 1)

        //Renders the ski list instead of mountain view page. I did this so I can put them all into a lazycolumn
        if(mountainNoBraces=="packing"){
            SkiList(packingViewModel)
        }else {
            val ctx = LocalContext.current
            val queue = Volley.newRequestQueue(ctx)

            //gets long and lat from the xml
            val getLatitutde = ctx.getResources()
                .getIdentifier(mountainNoBraces + "Lat", "string", ctx.getPackageName());
            val getLongitude = ctx.getResources()
                .getIdentifier(mountainNoBraces + "Lon", "string", ctx.getPackageName());
            var latitude = ctx.getString(getLatitutde)
            val longitude = ctx.getString(getLongitude)

            val url =
                "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=${latitude}&lon=${longitude}"
            var weatherDescription: JSONObject = JSONObject()
            val weatherStore = remember { mutableStateOf<String>("") }
            val codeStore = remember { mutableStateOf<String>("") }
            //    val imageStore = remember { mutableStateOf<AsyncImage>("")}

            // Request a string response from the provided URL using volley library.
            val stringRequest = object : StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    val json = JSONObject(response).getJSONArray("data").getJSONObject(0)
                    //gets the key weather from the json object
                    val weatherDescription = json.getJSONObject("weather")
                    //gets the description from the json object
                    val description = weatherDescription.getString("description")
                    val code = weatherDescription.getString("code")

                    weatherStore.value = description
                    codeStore.value = code
                },
                Response.ErrorListener { }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-RapidAPI-Key"] = "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907"
                    headers["X-RapidAPI-Host"] = "weatherbit-v1-mashape.p.rapidapi.com"
                    return headers
                }
            }

            //put the stringrequest in a launchedeffect to make it asynchronous
            LaunchedEffect(stringRequest) {
                queue.add(stringRequest)
            }

            //makes the page scrollable
            var scrollState = rememberScrollState()
            var visible by rememberSaveable { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //put switch with text Show Map in a row and have a toast when clicked
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(3.dp)) {
                        Text(text = ctx.getString(R.string.showMap), modifier = Modifier.padding(16.dp))
                        //switch which toggles the map view and makes a toast
                        Switch(checked = visible, onCheckedChange ={
                            visible = it
                            if (visible) {
                                Toast.makeText(ctx, ctx.getString(R.string.mapShown), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(ctx, ctx.getString(R.string.mapHidden), Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    //Shows the Mountain
                    var drawableId = ctx.getResources()
                        .getIdentifier(mountainNoBraces, "drawable", ctx.getPackageName());
                    val imageMountain: Painter = painterResource(id = drawableId)
                    Image(painter = imageMountain, contentDescription = ctx.getString(R.string.contentDescription))

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
                            contentDescription = ctx.getString(R.string.contentDescription),
                            modifier = Modifier.fillMaxWidth()
                        )
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

                    //Gets the weather from the API and displays it
                    Text(ctx.getString(R.string.currentWeather)+weatherStore.value)

                    //    Open google maps button
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
                        Text(ctx.getString(R.string.directionButton))
                    }
                }
            }
        }

    }

    /**
     * Class to hold the viewmodel
     */
    class packingViewModel: ViewModel() {
        //mutablestate of list of strings
        var packing by mutableStateOf<List<String>>(emptyList())
        //mutablestateof textfield value
        var newItemInput by mutableStateOf("")


        fun addItem(item:String) {
            //adds the textfield value to the list
            packing = packing.plus(item)
        }
        fun changeNewItemInput(item:String) {
            //changes the textfield value
            newItemInput = item
        }


    }