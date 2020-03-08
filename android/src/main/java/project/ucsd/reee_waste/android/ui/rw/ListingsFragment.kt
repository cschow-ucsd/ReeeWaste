package project.ucsd.reee_waste.android.ui.rw


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_listings.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.*

import project.ucsd.reee_waste.android.ui.rwErrorToast
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.service.RwService
import project.ucsd.reee_waste.backendless.service.WhereHelper

@UnstableDefault
class ListingsFragment : Fragment() {
    private val rwServiceViewModel: RwServiceViewModel by activityViewModels()
    private val rwService: RwService
        get() = rwServiceViewModel.rwServiceInstance

    private val scope = MainScope()

    private val items: MutableList<Item> = mutableListOf()
    private lateinit var adapter: ItemsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemsAdapter(items, true, View.OnClickListener { v ->
            val position = rvListingsItems.getChildLayoutPosition(v)
            val intent = Intent(activity, DisplayItemActivity::class.java)
            intent.putExtra(DisplayItemActivity.OBJECT_ID, items[position].objectId)
            intent.putExtra(DisplayItemActivity.CAN_PURCHASE, false)
            startActivityForResult(intent, DisplayItemActivity.REQUEST_CODE)
        })

        rvListingsItems.adapter = adapter
        rvListingsItems.layoutManager = LinearLayoutManager(context)

        swipeListingsRvContainer.setColorSchemeColors(context!!.getColor(R.color.colorAccent))
        swipeListingsRvContainer.setOnRefreshListener {
            fetchItemsAsync()
        }

        fabListingsPost.setOnClickListener {
            val intent = Intent(activity, PostItemActivity::class.java)
            startActivityForResult(intent, PostItemActivity.REQUEST_CODE)
        }

        fetchItemsAsync()
    }

    private fun fetchItemsAsync(
    ) = scope.launch {
        val user = (activity?.application as RwApplication).currentUser!!
        val itemsResponse = try {
            rwService.searchItemsAsync(
                    pageSize = 100,
                    offset = 0,
                    where = WhereHelper.searchWithOwner(
                            forSaleOnly = false,
                            ownerId = user.objectId
                    )
            ).await()
        } catch (e: Exception) {
            rwErrorToast(e)
            return@launch
        } finally {
            swipeListingsRvContainer.isRefreshing = false
        }

        items.clear()
        items.addAll(itemsResponse.results)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PostItemActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            fetchItemsAsync()
        }
    }
}
