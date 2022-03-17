package com.wwinc.moviewall

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.wwinc.moviewall.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    var permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    lateinit var sharedPrefReviewdorNot : SharedPreferences
    var userEntryCounter : Int = 0
    private var mFirebaseAnalytics: FirebaseAnalytics? = null



    @SuppressLint("CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.elevation = 0f

        val sharedPreferencesEditor = getSharedPreferences("RATING", Context.MODE_PRIVATE)!!.edit()
        val sharedPrefTrackingUserActivity = getSharedPreferences("USERACTIVITY", Context.MODE_PRIVATE)
        val editorSharedPrefTrackingUserActivity = getSharedPreferences("USERACTIVITY", Context.MODE_PRIVATE).edit()
        val sharedPreferences : SharedPreferences.Editor = this.getSharedPreferences("POSITION_TRACKER", Context.MODE_PRIVATE).edit()
        sharedPreferences.putString("POSTITION_2", null).apply()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        //Navigation
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.containerFragment)
        NavigationUI.setupActionBarWithNavController(this, navController,drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)


        if (!checkPermissionGranted(permission)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, 123)
            }
        }


        // Rate Us Method
        userEntryCounter = sharedPrefTrackingUserActivity.getInt("ACTIVITY", 0)
        sharedPrefReviewdorNot = getSharedPreferences("RATING", Context.MODE_PRIVATE)

        if(!sharedPrefReviewdorNot.getBoolean("checkRating", false)){
            // Detect User Activity
            editorSharedPrefTrackingUserActivity.putInt("ACTIVITY", userEntryCounter + 1)
            editorSharedPrefTrackingUserActivity.apply()

            if(userEntryCounter == 5 || userEntryCounter == 10 || userEntryCounter == 15){
                val alert  = AlertDialog.Builder(this)
                alert.setTitle("Love Us ?")
                alert.setMessage("If you are enjoying our app, You can leave a feedback for us")
                alert.setPositiveButton("Rate us"){_, _ ->
                    sharedPreferencesEditor.putBoolean("checkRating", true).apply()
                    try {
                        startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID.toString() + "&showAllReviews=true")
                        )
                    )
                    Toast.makeText(
                        this,
                        "Your Review on Playstore helps us to improve our apps",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID.toString() + "&showAllReviews=true")
                        )
                    )
                }

                }
                alert.setNegativeButton("Later"){dialog, _ ->
                    dialog.dismiss()
                }

                Handler().postDelayed({
                    alert.show()
                }, 5000)
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.containerFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)

    }

    fun checkPermissionGranted(permission : Array<String>) : Boolean{
        var allsuccess = true
        for (i in permission.indices){
            if(checkCallingOrSelfPermission(permission[i]) == PackageManager.PERMISSION_DENIED){
                allsuccess = false
            }
        }
        return allsuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123){
            var allsuccess = true
            for (i in permissions.indices){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allsuccess = false
                    var requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain){
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, " Go to Permission and Try Again ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    val folder = File(Environment.getExternalStorageDirectory(),"DCIM" + File.separator + resources.getString(R.string.app_name))
                    if (!folder.exists()){
                        folder.mkdirs()
                    }
                }
            }
        }

    }




}


