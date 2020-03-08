package project.ucsd.reee_waste.android.ui.rw


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.android.RwServiceViewModel
import project.ucsd.reee_waste.android.ui.rwErrorToast
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.service.RwService
import project.ucsd.reee_waste.backendless.service.WhereHelper

@UnstableDefault
class DashboardFragment : Fragment(), SearchView.OnQueryTextListener {
    private val rwServiceViewModel: RwServiceViewModel by activityViewModels()
    private val rwService: RwService
        get() = rwServiceViewModel.rwServiceInstance

    private val scope = MainScope()

    private var searchText: String = ""

    private val items: MutableList<Item> = mutableListOf()
    private lateinit var adapter: ItemsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemsAdapter(items)
        rvDashItems.adapter = adapter
        rvDashItems.layoutManager = LinearLayoutManager(context)

        swipeDashRvContainer.setColorSchemeColors(context!!.getColor(R.color.colorAccent))
        swipeDashRvContainer.setOnRefreshListener {
            fetchItemsAsync()
        }

        svDashSearch.setOnQueryTextListener(this)

        fetchItemsAsync()
    }

    private fun fetchItemsAsync(
    ) = scope.launch {
        swipeDashRvContainer.isRefreshing = true

        val itemsResponse = try {
            rwService.searchItemsAsync(
                    pageSize = 100,
                    offset = 0,
                    where = WhereHelper.search(
                            title = searchText,
                            description = searchText
                    )
            ).await()
        } catch (e: Exception) {
            rwErrorToast(e)
            return@launch
        } finally {
            swipeDashRvContainer.isRefreshing = false
        }

        items.clear()
        items.addAll(itemsResponse.results)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("DashboardFragment", "Query: $query")
        searchText = query ?: ""
        fetchItemsAsync()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchText = newText ?: ""
        return true
    }
}
