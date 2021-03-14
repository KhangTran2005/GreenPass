package com.example.greenpass

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.greenpass.data.Database
import com.example.greenpass.ui.main.LogIn
import com.example.greenpass.ui.main.LogInDirections
import com.example.greenpass.utils.Particulars
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), LogIn.OnLogInListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_userinfo, R.id.nav_covidinfo, R.id.nav_geofence, R.id.nav_infractions
            ), drawerLayout
        )
        //make the app bar change with navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        //make so the display thing changes with navController
        navView.setupWithNavController(navController)

        //get header views
        val avatarView = (navView.getHeaderView(0) as LinearLayout).getChildAt(0) as ImageView
        val nameView = (navView.getHeaderView(0) as LinearLayout).getChildAt(1) as TextView
        val idView = (navView.getHeaderView(0) as LinearLayout).getChildAt(2) as TextView

        //send to main screen if logged in
        if (Particulars.getUsername(baseContext) != null){
            Database.username = Particulars.getUsername(baseContext)!!
            Database.user = Particulars.getUser(baseContext)!!

            Database.user?.let {
                nameView.text = it.name
                idView.text = it.ID
            }

            val action = LogInDirections.loginAccepted()
            navController.navigate(action)
        } else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        when(item.itemId){
            R.id.action_settings -> navController.navigate(R.id.settings)
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
}