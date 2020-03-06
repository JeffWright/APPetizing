package com.jtw.appetizing.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.R
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class StringsAdapter @Inject constructor() : ListAdapter<String, SimpleViewHolder>(StringDiffUtil) {

    val itemClicks = PublishRelay.create<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val textView = holder.itemView.text_view
        val value = getItem(position)
        textView.text = value

        // holder.itemView.image.visibility = View.GONE

        holder.itemView.setOnClickListener { itemClicks.accept(value) }
    }
}

class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}

object StringDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}
