package edu.projectportal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bu.R
import edu.bu.databinding.FragmentProjectlistBinding
import edu.projectportal.adapters.ProjectListRecyclerAdapter
import edu.projectportal.model.ProjectListViewModel
import edu.projectportal.preferences.Prefs

class ProjectListFragment : Fragment() {
    private lateinit var projectListViewModel: ProjectListViewModel

    private var _binding: FragmentProjectlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = Prefs(requireContext())

        val adapter = ProjectListRecyclerAdapter()
        _binding?.rvProjectlist?.layoutManager = LinearLayoutManager(context)
        _binding?.rvProjectlist?.adapter = adapter

        projectListViewModel = ViewModelProvider(this).get(ProjectListViewModel::class.java)

        if (prefs.onlyFav()) {
            _binding?.cbOnlyfav?.isChecked = true
            projectListViewModel.favProjectList.observe(viewLifecycleOwner) { projects ->
                adapter.setData(projects)
            }
        } else {
            _binding?.cbOnlyfav?.isChecked = false
            projectListViewModel.projectList.observe(viewLifecycleOwner, Observer { projects ->
                adapter.setData(projects)
            })
        }

        _binding?.cbOnlyfav!!.setOnCheckedChangeListener { _, isChecked ->
            prefs.onlyFavValueChange(isChecked)
            if (isChecked) {
                projectListViewModel.favProjectList.observe(viewLifecycleOwner) { projects ->
                    adapter.setData(projects)
                }
            } else {
                projectListViewModel.projectList.observe(viewLifecycleOwner, Observer { projects ->
                    adapter.setData(projects)
                })
            }
        }

        _binding?.fabAddNewproject?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("action", "AddNew")
            findNavController().navigate(R.id.action_projectListFragment_to_editFragment2, bundle)
        }
    }

}