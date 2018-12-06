package com.ilpcoursework.coinz.adapters


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.DAO.friend
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.activities.MywalletActivity2

class frienddialogAdapter(private val myDataset: MutableList<friend>, userstore: User, activity: MywalletActivity2) :
        RecyclerView.Adapter<frienddialogAdapter.MyViewHolder>() {
    private var userstore = userstore
    private var activity = activity
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    class MyViewHolder// We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        var textView: TextView
        var sendButton: Button

        init {

            textView = itemView.findViewById<View>(R.id.friend) as TextView
            sendButton = itemView.findViewById<View>(R.id.deleteButton) as Button

        }// Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val myview = LayoutInflater.from(parent.context)
                .inflate(R.layout.myfriend_view, null, false)
        // set the view's size, margins, paddings and layout parameters
        // here
        return MyViewHolder(myview)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position].tostring()

        holder.sendButton.text = "send"

        holder.sendButton.setOnClickListener { view ->



            activity.sendfriend(userstore.selectedcoin,position)

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}