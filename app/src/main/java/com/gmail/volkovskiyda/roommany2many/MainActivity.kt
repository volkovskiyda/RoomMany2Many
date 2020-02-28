package com.gmail.volkovskiyda.roommany2many

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val app by lazy { application as App }

    init {
        lifecycleScope.launchWhenCreated {
            if (!app.sharedPref.getBoolean("data_populated", false)) {
                populateDatabase()
                app.sharedPref.edit { putBoolean("data_populated", true) }
            }

            logDatabase()
        }
    }

    private suspend fun populateDatabase() {
        with(app.database.dao()) {
            insertOwners(
                listOf(
                    Owner("Dima", 23, "android"),
                    Owner("Liza", 25, "design"),
                    Owner("Anna", 3, "kid")
                )
            )
            insertDogs(
                listOf(
                    Dog("Chelsea", 3, Coordinate(1.0, 1.0)),
                    Dog("Bonie", 4, Coordinate(2.0, 2.0)),
                    Dog("Lucky", 2, Coordinate(3.0, 3.0)),
                    Dog("Rex", 5, Coordinate(4.0, 4.0)),
                    Dog("Richy", 1, Coordinate(5.0, 5.0))
                )
            )
            insertOwnersAndDogs(
                listOf(
                    OwnerAndDog(1, 1),
                    OwnerAndDog(1, 2),
                    OwnerAndDog(1, 4),

                    OwnerAndDog(2, 1),
                    OwnerAndDog(2, 2),
                    OwnerAndDog(2, 3),

                    OwnerAndDog(3, 3),
                    OwnerAndDog(3, 5)
                )
            )
        }
    }

    private suspend fun logDatabase() {
        with(app.database.dao()) {
            val owners = owners()
            val dogs = dogs()
            val ownersAndDogs = ownersAndDogs()
            val ownerWithDogs = ownerWithDogs()
            val dogWithOwners = dogWithOwners()

            println(owners)
            println(dogs)
            println(ownersAndDogs)
            println(ownerWithDogs)
            println(dogWithOwners)
        }
    }
}