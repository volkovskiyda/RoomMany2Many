package com.gmail.volkovskiyda.roommany2many

import androidx.room.*
import androidx.room.Dao as RoomDao
import androidx.room.Database as RoomDb

@RoomDb(entities = [Owner::class, Dog::class, OwnerAndDog::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao
}

@RoomDao
abstract class Dao {
    @Query("SELECT * FROM owner")
    abstract suspend fun owners(): List<Owner>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOwners(owners: List<Owner>): List<Long>

    @Query("SELECT * FROM dog")
    abstract suspend fun dogs(): List<Dog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDogs(dogs: List<Dog>): List<Long>

    @Query("SELECT * FROM owner_and_dog")
    abstract suspend fun ownersAndDogs(): List<OwnerAndDog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOwnersAndDogs(ownersAndDogs: List<OwnerAndDog>): List<Long>

    @Transaction
    @Query("SELECT * FROM owner")
    abstract suspend fun ownerWithDogs(): List<OwnerWithDogs>

    @Transaction
    @Query("SELECT * FROM dog")
    abstract suspend fun dogWithOwners(): List<DogWithOwners>
}

data class Coordinate(val lat: Double, val lon: Double)

@Entity
data class Owner(
    val name: String, val age: Int, val position: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity
data class Dog(
    val name: String, val age: Int, @Embedded(prefix = "location_") val location: Coordinate,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(tableName = "owner_and_dog", primaryKeys = ["ownerId", "dogId"])
data class OwnerAndDog(val ownerId: Long, val dogId: Long)

data class OwnerWithDogs(
    @Embedded val owner: Owner,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = Dog::class,
        associateBy = Junction(OwnerAndDog::class, parentColumn = "ownerId", entityColumn = "dogId")
    )
    val dogs: List<Dog>
)

data class DogWithOwners(
    @Embedded val dog: Dog,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = Owner::class,
        associateBy = Junction(OwnerAndDog::class, parentColumn = "dogId", entityColumn = "ownerId")
    )
    val owners: List<Owner>
)