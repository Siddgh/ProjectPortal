package edu.projectportal.database

import androidx.lifecycle.LiveData

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun addProject(project: Project) {
        projectDao.addProject(project)
    }

    suspend fun updateProject(project: Project) {
        projectDao.updateProject(project)
    }

    fun getProjects(): LiveData<List<Project>> = projectDao.getProjects()

    fun getProject(projId: Int): LiveData<Project> = projectDao.getProject(projId)

    fun getFavProjects(): LiveData<List<Project>> = projectDao.getFavProjects()

}