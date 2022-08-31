package com.example.project1

sealed class Screens(val route:String) {
    object HomeScreen : Screens("home")
    object SkiList : Screens("skilist")
    object MountainScreen : Screens("mountain")
    object WeatherScreen : Screens("mountain")
}