package com.jtw.appetizing.feature.singlecategory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jtw.appetizing.R
import com.jtw.appetizing.core.AppState
import com.jtw.appetizing.core.Event
import com.jtw.appetizing.core.ModelStore
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MealListPresenterTest {

    private val adapter = mockk<MealsAdapter>(relaxed = true)
    private val modelStore = ModelStoreStub()

    private val objectUnderTest = MealListPresenter(adapter)

    private val view = mockk<View>()
    private val recycler = mockk<RecyclerView>(relaxed = true)

    @Before
    fun setup() {
        every { view.findViewById<RecyclerView>(R.id.recycler) } returns recycler
    }

    @Test
    fun `bind sets adapter on recyclerview`() {
        objectUnderTest.bind(view, modelStore, {})

        verify { recycler.adapter = adapter }
    }
}

class ModelStoreStub : ModelStore {
    override val state: BehaviorRelay<AppState> = BehaviorRelay.create()

    override fun onEvent(event: Event) {
    }

}