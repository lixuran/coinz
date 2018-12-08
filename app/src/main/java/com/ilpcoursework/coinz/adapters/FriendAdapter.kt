package com.ilpcoursework.coinz.adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ilpcoursework.coinz.DAO.friend
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.activities.MyFriendActivity

class FriendAdapter(private val myDataset: MutableList<friend>, private var activity2: MyFriendActivity) :
        RecyclerView.Adapter<FriendAdapter.MyViewHolder>() {
    // Provide a reference to the views for each data item ,Complex data items may need more than one view per item,

    // the view holder is a view that gives referrence to each item 's view in the list of friends
    class MyViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView: TextView = itemView.findViewById<View>(R.id.friend) as TextView
        var deleteButton: Button = itemView.findViewById<View>(R.id.deleteButton) as Button

        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val myview = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_friend_view, parent, false)

        return MyViewHolder(myview)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position].tostring()
        holder.deleteButton.setOnClickListener { _ ->
            activity2.deleteFriend(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}