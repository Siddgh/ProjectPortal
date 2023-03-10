package edu.projectportal.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects_list")
data class Project(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "desc")
    var description: String,

    @ColumnInfo(name = "authors")
    var authors: String,

    @ColumnInfo(name = "projectLinks")
    var projectLinks: String,

    @ColumnInfo(name = "isFav")
    var isFav: Boolean = false,

    @ColumnInfo(name = "keywords")
    var keywords: String,

    @ColumnInfo(name = "programmingLanguagesUsed")
    var programmingLanguagesUsed: String
)