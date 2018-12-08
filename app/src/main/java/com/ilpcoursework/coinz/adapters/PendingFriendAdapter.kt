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

class PendingFriendAdapter(private val myDataset: MutableList<friend>, private var activity: MyFriendActivity) :
        RecyclerView.Adapter<PendingFriendAdapter.MyViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and

    // the view holder is a view that gives referrence to each item 's view in the list of friends
    class MyViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // holder should contain a member variable
        // for any view that will be set when rendering the row
        var textView: TextView = itemView.findViewById<View>(R.id.pendingfriend) as TextView
        var refuseButton: Button = itemView.findViewById<View>(R.id.refuseButton) as Button
        var acceptButton: Button = itemView.findViewById<View>(R.id.acceptButton) as Button
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val myview = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_pendingfriend_view, parent, false)
        // set the view's size, margins, paddings and layout parameters
        //here
        return MyViewHolder(myview)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position].tostring()

        holder.refuseButton.setOnClickListener { _ ->


            activity.refuseFriend(position)

        }
        holder.acceptButton.setOnClickListener { _ ->


            activity.acceptFriend(position)

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}