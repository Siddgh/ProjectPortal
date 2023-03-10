package edu.projectportal.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Query("SELECT * FROM projects_list")
    fun getProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM projects_list where id = :projId")
    fun getProject(projId: Int): LiveData<Project>

    @Query("SELECT * FROM projects_list where isFav = 1")
    fun getFavProjects(): LiveData<List<Project>>

}