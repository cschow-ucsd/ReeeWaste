package project.ucsd.reee_waste.android.ui.rw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_item.view.*
import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.backendless.model.Item

class ItemsAdapter(
        val items: List<Item>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.rv_item, parent, false)
        return object : RecyclerView.ViewHolder(root) {}
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.bind(item)
    }
}

private fun View.bind(item: Item) {
    textviewItemName.text = item.title
    textviewItemPrice.text = "${context.getString(R.string.currency)}${item.price}"
    textviewItemType.text = item.type
}
