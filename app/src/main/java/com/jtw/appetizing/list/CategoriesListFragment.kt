package com.jtw.appetizing.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtw.appetizing.*
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.network.Success
import com.jtw.appetizing.network.Uninitialized
import com.jtw.util.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject

class CategoriesListFragment : DisposableFragment() {

    @Inject lateinit var presenter: CategoryListPresenter
    @Inject lateinit var modelStore: ModelStore

    override fun inject(component: MainActivityComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelStore.state.accept(AppState(
                categories = Uninitialized,
                chosenCategory = ChosenCategory.None
        ))

        modelStore.onEvent(RequestLoadCategoriesEvent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.list, container, false)
        view.tag = "CategoriesListFragment"
        addToDisposable(
                presenter.bind(view, modelStore)
        )
        activity?.title = getString(R.string.categories)
        return view
    }
}

class CategoryListPresenter @Inject constructor(
        private val adapter: StringsAdapter
) {

    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        val recycler = view.recycler

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
       
        val dividerItemDecoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
        recycler.addItemDecoration(dividerItemDecoration)

        disposable += adapter.itemClicks
                .subscribe {
                    modelStore.onEvent(ChoseCategoryEvent(it))
                }

        disposable += modelStore.state
                .map { it.categories }
                .filterIsInstance<Success<*>>()
                .firstElement()
                .subscribe { recycler.scheduleLayoutAnimation() }

        disposable += modelStore.state
                .subscribe(
                        { state ->
                            val items = state.categories.get()
                            // recycler.scheduleLayoutAnimation()
                            log("submit: $items")
                            adapter.submitList(items ?: emptyList())
                            log("submitted")
                        },
                        {/* TODO JTW */ }
                )

        return disposable
    }
}


