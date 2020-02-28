package com.gmail.volkovskiyda.roommany2many

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.kotlintest.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var database: Database
    private lateinit var dao: Dao

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
        dao = database.dao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun shouldInsertAndGetOwners() = runBlocking {
        val owners = listOf(
            Owner("0", 0, "0"),
            Owner("1", 1, "1")
        ).map { owner -> owner.copy(id = owner.age + 1L) }
        dao.insertOwners(owners)
        dao.owners() shouldBe owners
    }

    @Test
    fun shouldInsertAndGetDogs() = runBlocking {
        val dogs = listOf(
            Dog("0", 0, Coordinate(0.0, 0.0)),
            Dog("1", 1, Coordinate(1.0, 1.0))
        ).map { dog -> dog.copy(id = dog.age + 1L) }
        dao.insertDogs(dogs)
        dao.dogs() shouldBe dogs
    }

    @Test
    fun shouldInsertAndGetOwnerAndDogs() = runBlocking {
        val ownersAndDogs = listOf(
            OwnerAndDog(0, 0),
            OwnerAndDog(0, 1),
            OwnerAndDog(1, 0),
            OwnerAndDog(1, 1)
        )
        dao.insertOwnersAndDogs(ownersAndDogs)
        dao.ownersAndDogs() shouldBe ownersAndDogs
    }

    @Test
    fun shouldInsertAndGetOwnerWithDogs() = runBlocking {
        val dog0 = Dog("0", 0, Coordinate(0.0, 0.0))
        val dog1 = Dog("1", 1, Coordinate(1.0, 1.0))
        val dog2 = Dog("2", 2, Coordinate(2.0, 2.0))
        val dog3 = Dog("3", 3, Coordinate(3.0, 3.0))
        val dog4 = Dog("4", 4, Coordinate(4.0, 4.0))
        val ownerWithDogs = listOf(
            OwnerWithDogs(Owner("0", 0, "0"), listOf(dog0, dog1)),
            OwnerWithDogs(Owner("1", 1, "1"), listOf(dog1, dog2, dog3)),
            OwnerWithDogs(Owner("2", 2, "2"), listOf(dog2, dog4)),
            OwnerWithDogs(Owner("3", 3, "3"), listOf(dog3, dog4))
        ).map { ownerDogs ->
            ownerDogs.copy(
                ownerDogs.owner.copy(id = ownerDogs.owner.age + 1L),
                ownerDogs.dogs.map { dog -> dog.copy(id = dog.age + 1L) })
        }

        dao.insertOwners(ownerWithDogs.map(OwnerWithDogs::owner))
        dao.insertDogs(ownerWithDogs.flatMap(OwnerWithDogs::dogs))
        dao.insertOwnersAndDogs(ownerWithDogs.flatMap { ownerDogs ->
            ownerDogs.dogs.map { dog ->
                OwnerAndDog(
                    ownerDogs.owner.id,
                    dog.id
                )
            }
        })

        dao.ownerWithDogs() shouldBe ownerWithDogs
    }

    @Test
    fun shouldInsertAndGetDogWithOwners() = runBlocking {
        val owner0 = Owner("0", 0, "0")
        val owner1 = Owner("1", 1, "1")
        val owner2 = Owner("2", 2, "2")
        val owner3 = Owner("3", 3, "3")
        val owner4 = Owner("4", 4, "4")
        val dogWithOwners = listOf(
            DogWithOwners(Dog("0", 0, Coordinate(0.0, 0.0)), listOf(owner0, owner1)),
            DogWithOwners(Dog("1", 1, Coordinate(1.0, 1.0)), listOf(owner1, owner2, owner3)),
            DogWithOwners(Dog("2", 2, Coordinate(2.0, 2.0)), listOf(owner2, owner4)),
            DogWithOwners(Dog("3", 3, Coordinate(3.0, 3.0)), listOf(owner3, owner4))
        ).map { dogOwners ->
            dogOwners.copy(
                dogOwners.dog.copy(id = dogOwners.dog.age + 1L),
                dogOwners.owners.map { owner -> owner.copy(id = owner.age + 1L) })
        }

        dao.insertDogs(dogWithOwners.map(DogWithOwners::dog))
        dao.insertOwners(dogWithOwners.flatMap(DogWithOwners::owners))
        dao.insertOwnersAndDogs(dogWithOwners.flatMap { dogOwners ->
            dogOwners.owners.map { owner ->
                OwnerAndDog(
                    owner.id,
                    dogOwners.dog.id
                )
            }
        })

        dao.dogWithOwners() shouldBe dogWithOwners
    }
}