package com.jtw.appetizing.feature.singlecategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.R
import com.jtw.appetizing.core.ChoseMealEvent
import com.jtw.appetizing.core.Navigator
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.feature.categories.SimpleViewHolder
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class MealsAdapter @Inject constructor() : ListAdapter<MealWithThumbnail, SimpleViewHolder>(DiffUtil) {

    val itemClicks = PublishRelay.create<ChoseMealEvent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val textView = holder.itemView.text_view
        val imageView = holder.itemView.image
        val meal = getItem(position)
        textView.text = meal.name
        Glide.with(imageView).load(meal.thumbnailUrl).into(imageView)

        holder.itemView.setOnClickListener {
            imageView.transitionName = Navigator.transitionNameImage
            textView.transitionName = Navigator.transitionNameText
            itemClicks.accept(ChoseMealEvent(
                    meal.id,
                    meal.name,
                    meal.thumbnailUrl,
                    textView,
                    imageView
            ))
        }
    }
}

private object DiffUtil : DiffUtil.ItemCallback<MealWithThumbnail>() {
    /** Do these represent the same core item */
    override fun areItemsTheSame(oldItem: MealWithThumbnail, newItem: MealWithThumbnail): Boolean {
        return oldItem.id == newItem.id
    }

    /** Assuming these items DO represent the same core item, is their contents the same */
    override fun areContentsTheSame(oldItem: MealWithThumbnail, newItem: MealWithThumbnail): Boolean {
        return oldItem == newItem
    }
}
