package com.villejuif.eartheventtraker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.*

class MapsActivity : AppCompatActivity(){

    private lateinit var appConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val navController = this.findNavController(R.id.myNavHostFragment)

        appConfiguration = AppBarConfiguration.Builder(R.id.earthEventsFragment).build()


        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
         navController.navigate(R.id.action_mapsFragment_to_earthEventsFragment)
                return NavigationUI.navigateUp(navController, appConfiguration)
    }

}
