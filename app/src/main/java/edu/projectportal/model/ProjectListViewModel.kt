package edu.projectportal.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.projectportal.ProjectApplication
import edu.projectportal.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ProjectRepository
    private var readAllProjects: LiveData<List<Project>>

    init {
        val projectDao = (application as ProjectApplication).projectDatabase.projectDao()
        repository = ProjectRepository(projectDao)
        readAllProjects = projectDao.getProjects()
    }

    val projectList = repository.getProjects()

    fun addProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProject(project)
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProject(project)
        }
    }

    fun getProject(projId: Int) = repository.getProject(projId)

    private val _favProjectList: LiveData<List<Project>> = repository.getFavProjects()
    val favProjectList: LiveData<List<Project>> get() = _favProjectList

}