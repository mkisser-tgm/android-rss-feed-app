package dev.kisser.rssfeed.ui.recyclerView

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import dev.kisser.rssfeed.R
import dev.kisser.rssfeed.entity.Feed
import dev.kisser.rssfeed.util.simpleFormat
import dev.kisser.rssfeed.viewmodel.FeedViewModel
import java.text.SimpleDateFormat

class FeedListAdapter internal constructor(
    activity: FragmentActivity
) : RecyclerView.Adapter<FeedListAdapter.FeedViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private val feedViewModel = ViewModelProvider(activity).get(FeedViewModel::class.java)
    private var feeds = emptyList<Feed>()

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feedTitle: TextView = itemView.findViewById(R.id.feedTitle)
        val lastEntryDate: TextView = itemView.findViewById(R.id.lastEntryDate)
        val deleteItem: ImageButton = itemView.findViewById(R.id.deleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_feed_item, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val current = feeds[position]
        // https://stackoverflow.com/questions/25703360/regular-expression-extract-subdomain-domain
        val domainRegex = "^(?:https?://)?(?:[^@/\\n]+@)?(?:www\\.)?([^:/?\\n]+)".toRegex()

        if (current.feedTitle != null) {
            holder.feedTitle.text = current.feedTitle
        } else {
            holder.feedTitle.text = domainRegex.find(current.feedUrl)?.groups?.get(1)?.value ?: "Not\navailable"
        }

        if (current.lastEntryDate != null) {
            holder.lastEntryDate.text = simpleFormat.format(current.lastEntryDate)
        } else {
            holder.lastEntryDate.text = "Not\navailable"
        }

        holder.deleteItem.setOnClickListener {
            feedViewModel.delete(current)
        }
    }

    internal fun setFeeds(feeds: List<Feed>){
        this.feeds = feeds
        notifyDataSetChanged()
    }

    override fun getItemCount() = feeds.size
}
