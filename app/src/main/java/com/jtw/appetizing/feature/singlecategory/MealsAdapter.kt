package com.jtw.appetizing.feature.singlecategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.*
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.feature.categories.SimpleViewHolder
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class MealsAdapter @Inject constructor() : ListAdapter<MealWithThumbnail, SimpleViewHolder>(DiffUtil) {

    val itemClicks = PublishRelay.create<MealWithThumbnail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val textView = holder.itemView.text_view
        val imageView = holder.itemView.image
        val meal = getItem(position)
        textView.text = meal.strMeal
        Glide.with(imageView).load(meal.mealThumb).into(imageView)

        holder.itemView.setOnClickListener {
            sharedElementViewImage = imageView
            sharedElementViewText = textView
            imageView.transitionName = transitionNameImage
            textView.transitionName = transitionNameText
            itemClicks.accept(meal)
        }
    }
}

private object DiffUtil : DiffUtil.ItemCallback<MealWithThumbnail>() {
    /** Do these represent the same core item */
    override fun areItemsTheSame(oldItem: MealWithThumbnail, newItem: MealWithThumbnail): Boolean {
        return oldItem.idMeal == newItem.idMeal
    }

    /** Assuming these items DO represent the same core item, is their contents the same */
    override fun areContentsTheSame(oldItem: MealWithThumbnail, newItem: MealWithThumbnail): Boolean {
        return oldItem == newItem
    }
}
