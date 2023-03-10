package edu.projectportal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.bu.R
import edu.projectportal.database.Project
import edu.projectportal.model.ProjectListViewModel

class EditFragment : Fragment() {
    private lateinit var projTitle: EditText
    private lateinit var projDesc: EditText
    private lateinit var authors: EditText
    private lateinit var links: EditText
    private lateinit var progLang: EditText
    private lateinit var keywords: EditText

    private lateinit var addToFav: CheckBox

    private lateinit var submit: FloatingActionButton
    private lateinit var cancel: FloatingActionButton

    private lateinit var projectListViewModel: ProjectListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        projectListViewModel = ViewModelProvider(this)[ProjectListViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action = arguments?.getString("action")
        val position = arguments?.getInt("position")

        projTitle = view.findViewById(R.id.projectNameValue)
        projDesc = view.findViewById(R.id.projectDescValue)
        authors = view.findViewById(R.id.authorsValue)
        links = view.findViewById(R.id.linkValue)
        progLang = view.findViewById(R.id.progLValue)
        submit = view.findViewById(R.id.fabSubmit)
        cancel = view.findViewById(R.id.fabClear)
        addToFav = view.findViewById(R.id.addToFav)
        keywords = view.findViewById(R.id.keywordsValue)

        if (action == "AddNew") {
            submit.setOnClickListener {

                val project = Project(
                    id = 0,
                    title = projTitle.text.toString(),
                    description = projDesc.text.toString(),
                    authors = authors.text.toString(),
                    projectLinks = links.text.toString(),
                    programmingLanguagesUsed = progLang.text.toString(),
                    isFav = addToFav.isChecked,
                    keywords = keywords.text.toString()
                )

                insertDataToDatabase(project)

                view.findNavController().navigate(R.id.action_editFragment2_to_projectListFragment)
            }
        } else {

            projectListViewModel.getProject(position!!).observe(viewLifecycleOwner) { project ->
                projTitle.setText(project.title)
                projDesc.setText(project.description)
                authors.setText(project.authors)
                links.setText(project.projectLinks)
                progLang.setText(project.programmingLanguagesUsed)
                addToFav.isChecked = project.isFav
                keywords.setText(project.keywords)


                submit.setOnClickListener {

                    val project = Project(
                        id = position,
                        title = projTitle.text.toString(),
                        description = projDesc.text.toString(),
                        authors = authors.text.toString(),
                        projectLinks = links.text.toString(),
                        programmingLanguagesUsed = progLang.text.toString(),
                        isFav = addToFav.isChecked,
                        keywords = keywords.text.toString()
                    )

                    updateDataInDatabase(project)

                    view.findNavController()
                        .navigate(R.id.action_editFragment2_to_projectListFragment)
                }

            }
        }

        cancel.setOnClickListener {
            view.findNavController().navigate(R.id.action_editFragment2_to_projectListFragment)
        }

    }

    private fun updateDataInDatabase(project: Project) {

        projectListViewModel.updateProject(project)
        Toast.makeText(requireContext(), "Project Successfully Updated", Toast.LENGTH_LONG).show()
    }

    private fun insertDataToDatabase(project: Project) {
        projectListViewModel.addProject(project)
        Toast.makeText(requireContext(), "Project Successfully Added", Toast.LENGTH_LONG).show()
    }
}