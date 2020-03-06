package com.jtw.appetizing.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jtw.appetizing.R
import com.jtw.appetizing.dagger.DaggerFragment
import com.jtw.appetizing.dagger.MainActivityComponent
import com.jtw.appetizing.network.Uninitialized
import com.jtw.appetizing.plusAssign
import com.jtw.util.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.list.view.*
import javax.inject.Inject

class CategoriesListFragment : DaggerFragment() {

    @Inject lateinit var presenter: CategoryListPresenter
    @Inject lateinit var modelStore: ModelStore

    private var disposable: CompositeDisposable? = CompositeDisposable()

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
        val disposable = disposable ?: CompositeDisposable()
        disposable += presenter.bind(view, modelStore)
        this.disposable = disposable

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.clear()
        disposable = null
    }

}

class CategoryListPresenter @Inject constructor(
        private val adapter: MyAdapter
) {

    fun bind(view: View, modelStore: ModelStore): Disposable {
        val disposable = CompositeDisposable()

        val recycler = view.recycler

        view.header.text = "Meal Categories"

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)

        disposable += adapter.itemClicks
                .subscribe {
                    modelStore.onEvent(ChoseCategoryEvent(it))
                }

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

