package edu.projectportal.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.bu.R
import edu.projectportal.database.Project

class ProjectListRecyclerAdapter :
    RecyclerView.Adapter<ProjectListRecyclerAdapter.ViewHolder>() {

    private var projects = emptyList<Project>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.projectTitle.text = projects[position].title
        holder.projectDesc.text = projects[position].description
        holder.cardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("action", "OpenProject")
            bundle.putInt("position", projects[position].id)
            holder.view.findNavController()
                .navigate(R.id.action_projectListFragment_to_detailFragment2, bundle)
        }
    }

    fun setData(project: List<Project>) {
        this.projects = project
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = projects.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
        var projectTitle: TextView
        var projectDesc: TextView
        var cardView: CardView

        init {
            projectTitle = view.findViewById(R.id.tv_rv_list_name)
            projectDesc = view.findViewById(R.id.tv_rv_list_desc)
            cardView = view.findViewById(R.id.cv_project_item)
        }
    }
}