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
import com.ilpcoursework.coinz.activities.MywalletActivity

class MyAdapter(private val myDataset: MutableList<Coin>, private var userstore: User, private var activity2: MywalletActivity) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var coinsmapping = hashMapOf("SHIL" to R.drawable.bluecoin,"DOLR" to R.drawable.greencoin,"QUID" to R.drawable.yellowcoin,"PENY" to R.drawable.redcoin)
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and

    // the view holder is a view that gives referrence to each item 's view in the list of friends
    class MyViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // holder should contain a member variable
        // for any view that will be set when rendering the row
        var textView: TextView
        var bankingButton: Button
        var giftButton: Button
        var imageView: ImageView
        init {
            imageView = itemView.findViewById<View>(R.id.imageView3) as ImageView

            textView = itemView.findViewById<View>(R.id.coininfo) as TextView
            bankingButton = itemView.findViewById<View>(R.id.bankingButton) as Button
            giftButton = itemView.findViewById<View>(R.id.giftButton) as Button

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val myview = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false)

        return MyViewHolder(myview)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position

        holder.textView.text = myDataset[position].tostring()
        // currency might come with "\""
        val clean=myDataset[position].currency.removePrefix("\"")
        val clean2=clean.removeSuffix("\"")
        // set the view for each coin to be displayed
        holder.imageView.setImageResource(coinsmapping[ clean2]!!)
        holder.giftButton.text ="send gift"
        holder.bankingButton.text = "save it"
        holder.giftButton.setOnClickListener { _ ->

                activity2.show_dialog(myDataset[position])
        }
        holder.bankingButton.setOnClickListener { _ ->
            activity2.sandbank(position)

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}