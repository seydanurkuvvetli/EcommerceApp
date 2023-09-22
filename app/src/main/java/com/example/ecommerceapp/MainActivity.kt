package com.example.ecommerceapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import com.example.ecommerceapp.Workmanager.SendNotificationWorker
import com.example.ecommerceapp.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesBasket: SharedPreferences
    private var auth= Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private var bottomNavVisibility = View.VISIBLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //To check if there are any products in the basket.
        sharedPreferencesBasket = this.getSharedPreferences(
            "com.example.ecommerceapp",
            Context.MODE_PRIVATE
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavVisibility = when (destination.id) {
                R.id.signInFragment,R.id.signUpFragment,R.id.splashFragment-> View.GONE
                else -> View.VISIBLE
            }
            setBottomNavVisibility(bottomNavVisibility)
        }
    }
    override fun onResume() {
        super.onResume()
        WorkManager.getInstance(this@MainActivity).cancelAllWork()
    }

    override fun onStop() {
        super.onStop()
        if(auth.currentUser!=null) {
            val isProductInBag = sharedPreferencesBasket.getBoolean("isProductInBag", false)
            if (isProductInBag) {
                startWorkManager()
            }
        }else{
            WorkManager.getInstance(this@MainActivity).cancelAllWork()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        WorkManager.getInstance(this@MainActivity).cancelAllWork()
    }

    private fun startWorkManager(){
        val constraints= Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val myWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<SendNotificationWorker>(1,
            TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this@MainActivity).enqueue(myWorkRequest)
    }

    private fun setBottomNavVisibility(visibility: Int) {
        binding.bottomNavigationView.visibility = visibility
    }
}
