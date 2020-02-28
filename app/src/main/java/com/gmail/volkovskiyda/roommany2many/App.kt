package com.gmail.volkovskiyda.roommany2many

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room

class App : Application() {
    val sharedPref: SharedPreferences by lazy { getSharedPreferences("pref", MODE_PRIVATE) }
    val database: Database by lazy {
        Room.databaseBuilder(this, Database::class.java, "database")
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }
}