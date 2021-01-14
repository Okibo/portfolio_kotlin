package pl.portfolio.kotlinapp.nasafeed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.portfolio.kotlinapp.R

class FeedAdapter(private val feedList: ArrayList<FeedEntry>): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    fun getlistsize() = feedList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.nasafeed_record, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nasaTitle.text = feedList[position].title
        viewHolder.nasaDesc.text = feedList[position].description
        viewHolder.nasaPubdate.text = feedList[position].pubdate
        viewHolder.nasaSource.text = feedList[position].source
    }

    override fun getItemCount() = feedList.size


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val nasaTitle: TextView = v.findViewById(R.id.nasaTitle)
        val nasaPubdate: TextView = v.findViewById(R.id.nasaPubdate)
        val nasaDesc: TextView = v.findViewById(R.id.nasaDesc)
        val nasaSource: TextView = v.findViewById(R.id.nasaSource)

    }
}