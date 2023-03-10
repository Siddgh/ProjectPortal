package edu.projectportal.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Secret::class], version = 1)
abstract class SecretDatabase : RoomDatabase() {
    abstract fun secretDatabaseDao(): SecretDatabaseDao
}