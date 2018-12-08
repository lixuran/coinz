package com.ilpcoursework.coinz.adapters


import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.DAO.friend
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.activities.MywalletActivity

class FriendDialogAdapter(private val myDataset: MutableList<friend>, private var userstore: User, private var activity: MywalletActivity) :
        RecyclerView.Adapter<FriendDialogAdapter.MyViewHolder>() {
    // Provide a reference to the views for each data item Complex data items may need more than one view per item, and
    // the view holder is a view that gives referrence to each item 's view in the list of friends
    class MyViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // holder should contain a member variable
        // for any view that will be set when rendering the row
        var textView: TextView = itemView.findViewById<View>(R.id.friend) as TextView
        var sendButton: Button = itemView.findViewById<View>(R.id.deleteButton) as Button

    }

    // Create new views (invoked by the layout manager)
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val myview = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_friend_view, null, false)

        return MyViewHolder(myview)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from the dataset at this position

        // replace the textview text with the friend information and set button listener
        holder.textView.text = myDataset[position].tostring()
        holder.sendButton.text = "send"
        holder.sendButton.setOnClickListener { _ ->
            activity.sendFriend(userstore.selectedcoin,position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}