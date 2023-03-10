package edu.projectportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import edu.bu.R
import edu.projectportal.database.Project
import edu.projectportal.database.Secret

class MainActivity : AppCompatActivity() {
    //private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val project = Project(
            id = 1,
            title = "Netflix",
            description = "Movie App",
            authors = "",
            projectLinks = "",
            isFav = false,
            keywords = "",
            programmingLanguagesUsed = ""
        )
        //mainActivityViewModel.addProject(project)

        //mainActivityViewModel.projectList.observe(this) { projects ->

    }

}