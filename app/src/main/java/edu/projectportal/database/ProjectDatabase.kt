package edu.projectportal.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}