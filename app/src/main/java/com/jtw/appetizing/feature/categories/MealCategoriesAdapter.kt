package com.jtw.appetizing.feature.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.R
import com.jtw.appetizing.domain.MealCategory
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class MealCategoriesAdapter @Inject constructor() : ListAdapter<MealCategory, SimpleViewHolder>(DiffUtil) {

    val itemClicks = PublishRelay.create<MealCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val textView = holder.itemView.text_view
        val value = getItem(position)
        textView.text = value.strCategory

        // holder.itemView.image.visibility = View.GONE

        holder.itemView.setOnClickListener { itemClicks.accept(value) }
    }
}

class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}

private object DiffUtil : DiffUtil.ItemCallback<MealCategory>() {
    override fun areItemsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
        return oldItem == newItem
    }

}
