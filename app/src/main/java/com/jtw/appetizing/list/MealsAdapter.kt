package com.jtw.appetizing.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.jakewharton.rxrelay2.PublishRelay
import com.jtw.appetizing.R
import com.jtw.appetizing.network.MealWithThumb
import com.jtw.appetizing.sharedElementView
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class MealsAdapter @Inject constructor() : ListAdapter<MealWithThumb, SimpleViewHolder>(DiffUtil) {

    val itemClicks = PublishRelay.create<MealWithThumb>()

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
        Glide.with(imageView).load(meal.strMealThumb).into(imageView)
        sharedElementView = imageView

        holder.itemView.setOnClickListener { itemClicks.accept(meal) }
    }
}

object DiffUtil : DiffUtil.ItemCallback<MealWithThumb>() {
    /** Do these represent the same core item */
    override fun areItemsTheSame(oldItem: MealWithThumb, newItem: MealWithThumb): Boolean {
        return oldItem.idMeal == newItem.idMeal
    }

    /** Assuming these items DO represent the same core item, is their contents the same */
    override fun areContentsTheSame(oldItem: MealWithThumb, newItem: MealWithThumb): Boolean {
        return oldItem == newItem
    }
}
