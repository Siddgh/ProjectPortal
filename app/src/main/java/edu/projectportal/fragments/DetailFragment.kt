package edu.projectportal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.bu.R
import edu.projectportal.model.ProjectListViewModel

class DetailFragment : Fragment() {
    private lateinit var projTitle: TextView
    private lateinit var projDesc: TextView
    private lateinit var editProj: FloatingActionButton
    private lateinit var projAuthors: TextView
    private lateinit var projLinks: TextView
    private lateinit var projKeywords: TextView
    private lateinit var isProjFav: ImageView
    private lateinit var projProgrammingLang: TextView

    lateinit var projectListViewModel: ProjectListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        projectListViewModel = ViewModelProvider(this)[ProjectListViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action = arguments?.getString("action")
        val position = arguments?.getInt("position")

        projTitle = view.findViewById(R.id.projectTitle)
        projDesc = view.findViewById(R.id.projectDesc)
        editProj = view.findViewById(R.id.fabEditProj)
        projAuthors = view.findViewById(R.id.authorsValues)
        projLinks = view.findViewById(R.id.linksValue)
        projKeywords = view.findViewById(R.id.keywords)
        projProgrammingLang = view.findViewById(R.id.progLangValue)
        isProjFav = view.findViewById(R.id.isFav)


        if (action == "OpenProject") {

            projectListViewModel.getProject(position!!).observe(viewLifecycleOwner) { project ->
                projTitle.text = project.title
                projDesc.text = project.description
                projAuthors.text = project.authors
                projLinks.text = project.projectLinks
                projProgrammingLang.text = project.programmingLanguagesUsed
                projKeywords.text = project.keywords
                if (project.isFav) {
                    isProjFav.setImageResource(R.drawable.star_true)
                } else {
                    isProjFav.setImageResource(R.drawable.star_false)
                }
            }
        }

        editProj.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("action", "EditProject")
            bundle.putInt("position", position!!)
            view.findNavController().navigate(R.id.action_detailFragment2_to_editFragment2, bundle)
        }

    }
}