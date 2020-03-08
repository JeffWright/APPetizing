package com.jtw.appetizing.feature.singlecategory

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jtw.appetizing.R
import com.jtw.appetizing.core.ChosenCategory
import com.jtw.appetizing.domain.MealCategory
import com.jtw.appetizing.domain.MealWithThumbnail
import com.jtw.appetizing.network.Fail
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.network.Uninitialized
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MealsListViewTest {
    private val adapter: MealsAdapter = mockk(relaxed = true)
    private val objectUnderTest = MealsListView(adapter)
    private val view = mockk<ViewGroup>(relaxed = true)

    private val recyclerView = mockk<RecyclerView>(relaxed = true)
    private val loadingView = mockk<LinearLayout>(relaxed = true)
    private val errorView = mockk<LinearLayout>(relaxed = true)

    private val defaultModel = ChosenCategory.Actual(
            MealCategory("Breakfast"),
            mealsInCategory = Uninitialized
    )
    private val sampleMeal: MealWithThumbnail = mockk()

    @Before
    fun setup() {
        every { view.findViewById<RecyclerView>(R.id.recycler) } returns recyclerView
        every { view.findViewById<LinearLayout>(R.id.loading) } returns loadingView
        every { view.findViewById<LinearLayout>(R.id.error) } returns errorView
    }

    @Test
    fun `shows correct loading state based on model`() {
        val model = defaultModel

        objectUnderTest.render(view, model)

        verify { recyclerView.visibility = View.GONE }
        verify { errorView.visibility = View.GONE }

        verify { loadingView.visibility = View.VISIBLE }
    }

    @Test
    fun `shows correct success state based on model`() {
        val model = defaultModel.copy(
                mealsInCategory = Success(listOf(sampleMeal))
        )
        objectUnderTest.render(view, model)

        verify { loadingView.visibility = View.GONE }
        verify { errorView.visibility = View.GONE }

        verify { recyclerView.visibility = View.VISIBLE }
        verify { adapter.submitList(listOf(sampleMeal)) }
    }

    @Test
    fun `shows correct error state based on model`() {
        val model = defaultModel.copy(
                mealsInCategory = Fail(Exception("ohno"))
        )
        objectUnderTest.render(view, model)

        verify { loadingView.visibility = View.GONE }
        verify { recyclerView.visibility = View.GONE }

        verify { errorView.visibility = View.VISIBLE }
    }
}