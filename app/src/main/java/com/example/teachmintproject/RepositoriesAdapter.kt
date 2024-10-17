package com.example.teachmintproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoriesAdapter(private val onItemClick: (Repository) -> Unit) : RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder>() {

    private val items = mutableListOf<Repository>()

    fun submitList(newItems: List<Repository>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = items[position]
        holder.bind(repository)
        holder.itemView.setOnClickListener { onItemClick(repository) }
    }

    override fun getItemCount() = items.size

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.repository_name)

        fun bind(repository: Repository) {
            nameTextView.text = repository.name
        }
    }
}
