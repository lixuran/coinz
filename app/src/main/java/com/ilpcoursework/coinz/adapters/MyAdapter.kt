package com.ilpcoursework.coinz.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.activities.MywalletActivity2

class MyAdapter(private val myDataset: MutableList<Coin>, userstore: User, activity2: MywalletActivity2) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var coinsmapping = hashMapOf("SHIL" to R.drawable.bluecoin,"DOLR" to R.drawable.greencoin,"QUID" to R.drawable.yellowcoin,"PENY" to R.drawable.redcoin)
    private var userstore = userstore
    private var activity2 = activity2
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
        var bankingButton: Button
        var giftButton: Button
        var imageView: ImageView
        init {
            imageView = itemView.findViewById<View>(R.id.imageView3) as ImageView

            textView = itemView.findViewById<View>(R.id.coininfo) as TextView
            bankingButton = itemView.findViewById<View>(R.id.bankingButton) as Button
            giftButton = itemView.findViewById<View>(R.id.giftButton) as Button

        }// Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val myview = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters
        //here
        return MyViewHolder(myview)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position].tostring()
        val clean=myDataset[position].currency.removePrefix("\"")
        val clean2=clean.removeSuffix("\"")
        holder.imageView.setImageResource(coinsmapping[ clean2]!!)
        holder.giftButton.text ="send gift"
        holder.bankingButton.text = "save it"
        holder.giftButton.setOnClickListener { view ->

                activity2.showdialog(myDataset[position])


        }
        holder.bankingButton.setOnClickListener { view ->
            activity2.sendtobank(position)

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}