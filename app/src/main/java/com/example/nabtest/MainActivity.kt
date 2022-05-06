package com.example.nabtest

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nabtest.ui.main.MainFragment
import com.example.nabtest.utils.RootUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onStart() {
        super.onStart()
        if (RootUtils.isDeviceRooted()) {
            AlertDialog.Builder(this)
                .setMessage("You can not use this app on a rooted device")
                .setPositiveButton("OK") { _, _ -> finish() }
                .setCancelable(false)
                .create().show()
        }
    }
}