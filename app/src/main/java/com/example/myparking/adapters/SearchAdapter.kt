package com.example.myparking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myparking.R
import com.example.myparking.models.SearchResult
import org.w3c.dom.Text

class SearchAdapter(val suggestions: ArrayList<SearchResult>, val listener: OnSearchListener) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout,parent,false)
        return SearchViewHolder(view,listener)
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.description.text = suggestion.title
        holder.label.text = suggestion.vicinity
        holder.searchRes = suggestion
    }

    inner class SearchViewHolder(val view: View, val listener: OnSearchListener): RecyclerView.ViewHolder(view),
        View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }
        lateinit var searchRes: SearchResult
        val label = view.findViewById<TextView>(R.id.label)
        val description = view.findViewById<TextView>(R.id.description)

        override fun onClick(v: View?) {
            listener.onSearchClick(searchRes)
        }
    }

}
interface OnSearchListener {
    fun onSearchClick(searchResult: SearchResult)
}