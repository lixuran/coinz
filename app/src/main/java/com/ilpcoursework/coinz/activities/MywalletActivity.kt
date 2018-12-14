package com.ilpcoursework.coinz.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.HelperFunctions
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.adapters.FriendDialogAdapter
import com.ilpcoursework.coinz.adapters.MyAdapter
import kotlinx.android.synthetic.main.activity_mywallet2.*
import kotlinx.android.synthetic.main.app_bar_mywallet2.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
// the activity to store coins to bank and send coins to other users
class MywalletActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerView: RecyclerView    // the recycler view to show the coins of a perticular type
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var frienddialogrecyclerView: RecyclerView // the recycler view to show the friends that a coin can be send to
    private lateinit var frienddialogviewAdapter: RecyclerView.Adapter<*>
    private lateinit var frienddialogviewManager: RecyclerView.LayoutManager
    private var userstore: User?=null
    private var color:String?=null
    private lateinit var dialog:Dialog
    private var selectedCoins = mutableListOf<Coin>()
    private var db = FirebaseFirestore.getInstance()
    private val TAG ="mywalletactivity"
    private var namesMapping = hashMapOf("SHIL" to "frost dragon","DOLR" to "acient dragon","QUID" to "blood dragon","PENY" to " fire dragon")
    private var helperFunctions= HelperFunctions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mywallet2)
        setSupportActionBar(this.toolbar)
        // get the current user info and the type of the coin
        userstore = intent.extras["useridentity"] as? User
        color = intent.extras["color"] as? String

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        // set navigation view header info
        nav_view.setNavigationItemSelectedListener(this)
        helperFunctions.updateHeader(userstore,nav_view)

        // get the coins to be shown in the recycler view
        val iterator= userstore!!.coins.iterator()
        while (iterator.hasNext()) {
            val coin = iterator.next()
            if(coin.currency ==color )
            {
                selectedCoins.add(0,coin)
            }
        }
        // initialise the recycler view of coins
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(selectedCoins, userstore!!, this)
        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view2).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        // set realtime update listener for the friend list and invitations
        val docRef = db.collection("users").document(userstore!!.email)
        docRef.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                // if no longer in this activity the listener exits
                Log.w(TAG, "listen:error", firebaseFirestoreException)
            }
            //update user object from snapshot
            userstore= documentSnapshot?.toObject(User::class.java)
            // update the views based on the kind of the change happened.
            when {

                userstore?.changestate==3 -> {
                    selectedCoins.clear()
                    viewAdapter.notifyDataSetChanged()
                    val coinIterator= userstore!!.coins.iterator()
                    while (coinIterator.hasNext()) {
                        val coin = coinIterator.next()
                        if(coin.currency ==color )
                        {
                            selectedCoins.add(0,coin)
                        }
                    }
                    viewAdapter.notifyItemRangeInserted(0,selectedCoins.size)
                    userstore?.changestate=0
                    helperFunctions.updateHeader(userstore,nav_view)
                }
            }
        }
    }

    /**
     * send the coin to the bank, update user information and view,
     * @param coin the selected coins
     */
    fun sendBank(coin:Coin){
        val coinindex = userstore!!.coins.map{acoin->acoin.id}.indexOf(coin.id)
        // if reached maximum saving allowance, and the coin is not a gift, do nothing
        if(userstore!!.bankedToday>=25 && coin.type==0)
        {
            Toast.makeText(this, "maximum banking limit reached",
                    Toast.LENGTH_SHORT).show()
        }
        //else remove the coin from the coin list and
        // calculate gold and coin sum accordingly
        else {
            if(coin.type==0)
            userstore!!.bankedToday+=1

            var rate = 0.0
            when (coin.currency) {
                "SHIL" -> {rate = userstore!!.shilrate
                    userstore!!.myshils-=coin.value}
                "DOLR" -> {rate = userstore!!.dolrrate
                    userstore!!.mydolrs-=coin.value}
                "QUID" -> {rate = userstore!!.quidrate
                    userstore!!.myquids-=coin.value}

                "PENY" ->{ rate = userstore!!.penyrate
                    userstore!!.mypenys-=coin.value}

            }
            val addition =coin.value * rate
            userstore!!.gold = userstore!!.gold + addition
            userstore!!.coins.removeAt(coinindex)
            update_selected_coins()
            helperFunctions.updateUser(userstore!!, "myadapter")
            helperFunctions.updateHeader(userstore,nav_view)
        }
    }
    /**
     * show the coin info dialog for each coin , which contains
     * the value, currency, collected date and indicator of wether it is
     * send from a friend.
     */
    fun showCoinInfoDialog(coin:Coin){
        //set up the alert dialog with dismiss button
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater : LayoutInflater = layoutInflater
        val view : View = inflater.inflate(R.layout.my_coininfo_dialog,null)
        builder.setView(view)
        builder.setPositiveButton("close") { dialog, which -> dialog!!.dismiss() }
        // get item views
        val coinValue= view.findViewById<TextView>(R.id.coin_value)
        val coinCurrency= view.findViewById<TextView>(R.id.coin_currency)
        val coinCollectedTime= view.findViewById<TextView>(R.id.coin_collected_time)
        val coinIsGiven= view.findViewById<TextView>(R.id.coin_gift)
        // set text using coin info.
        coinValue?.text= "the bone wegiht"+coin.value.toString()+"kg"
        coinCurrency?.text= "this is a "+namesMapping[coin.currency]+" bone"
        coinCollectedTime?.text= "the dragon bone is stored on "+coin.date
        coinIsGiven?.text= "the dragon bone is"+if(coin.type==1) "given by friend" else "collected from map"
        //build and show the dialog
        val dialog = builder.create()
        dialog.show()
    }
    /**
     *show the dialog for sending coin to a friend
     * @param coin the selected coin
     */
    fun showSendFriendDialog(coin:Coin){
        // selectedcoin: the index of the coin selected in the coins user possess
        userstore?.selectedcoin=userstore!!.coins.map { acoin->acoin.id }.indexOf(coin.id)
        // does user have friend?
        if(userstore!!.friends.size>0) {
            //set dialog with users friends in the recycler list
            dialog = Dialog(this)
            dialog.setContentView(R.layout.my_friend_dialog)
            val textView =  dialog.findViewById<TextView>(R.id.textView)!!
            textView.text = getString(R.string.sendprompt)

            frienddialogviewManager = LinearLayoutManager(this)
            frienddialogviewAdapter = FriendDialogAdapter(userstore!!.friends, userstore!!, this)
            frienddialogrecyclerView = dialog.findViewById<RecyclerView>(R.id.dialog_recycler_view)!!
            frienddialogrecyclerView.apply {
                setHasFixedSize(true)
                // use a linear layout manager
                layoutManager = frienddialogviewManager
                // specify an viewAdapter (see also next example)
                adapter = frienddialogviewAdapter

            }
            val buttonView =dialog.findViewById<Button>(R.id.dismiss_button)
            buttonView.setOnClickListener {
                    dialog.dismiss()
            }
            dialog.show()
        }
        else{
            Toast.makeText(this, "can't show dialog: add some friend first",
                    Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * send the coin to the selected user in the user dialog
     * @param  coinIndex the index of the coin in the user's coin list
     * @param friendIndex the index of the friend in the user's friend list
     */
    fun sendFriend(coinIndex:Int, friendIndex:Int){
        if(userstore!!.giftToday<30) {
            val friend = userstore!!.friends.get(friendIndex)
            val coin = userstore!!.coins.get(coinIndex)
            // remove the coin from user's coins
            userstore!!.coins.removeAt(coinIndex)
            userstore!!.giftToday = userstore!!.giftToday + 1
            //updates users count of possesse coins
            when (coin.currency) {
                "SHIL" -> {
                    userstore!!.myshils-=coin.value
                }
                "DOLR" -> {
                    userstore!!.mydolrs-=coin.value
                }
                "QUID" -> {
                    userstore!!.myquids-=coin.value
                }

                "PENY" ->{
                    userstore!!.mypenys-=coin.value
                }

            }
            Toast.makeText(this, "coin sent",
                    Toast.LENGTH_SHORT).show()
            //upload user 's current state
            helperFunctions.updateUser(userstore!!, "FriendDialogAdapter")
            // and send it to the friend
            helperFunctions.friendAddCoin(friend.email, coin, "FriendDialogAdapter",2,this)
            //close the dialog
            dialog.dismiss()
            // update the list of coins in the subwallet
            update_selected_coins()
            //update the value in the header of the navigation view
            helperFunctions.updateHeader(userstore,nav_view)
        }
        else{
            Toast.makeText(this, "can't send the gift: you have reached the upper limit today, plz try tmr",
                    Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * update the coins view after a coin has been sent to a friend
     */
    private fun update_selected_coins(){
        selectedCoins.clear()
        viewAdapter.notifyDataSetChanged()
        val iterator= userstore!!.coins.iterator()
        while (iterator.hasNext()) {
            val coin = iterator.next()
            if(coin.currency ==color )
            {
                selectedCoins.add(0,coin)
            }
        }
        viewAdapter.notifyItemRangeInserted(0,selectedCoins.size)
    }

    /**
     *  send coin has failed. process to add the coin back to user's account.
     */
    fun addPlayerCoin(coin :Coin){
        userstore!!.coins.add(0,coin)
        userstore!!.giftToday = userstore!!.giftToday - 1
        //updates users count of possesse coins
        when (coin.currency) {
            "SHIL" -> {
                userstore!!.myshils+=coin.value
            }
            "DOLR" -> {
                userstore!!.mydolrs+=coin.value
            }
            "QUID" -> {
                userstore!!.myquids+=coin.value
            }

            "PENY" ->{
                userstore!!.mypenys+=coin.value
            }

        }
        // update the list of coins in the subwallet
        update_selected_coins()
        //update the value in the header of the navigation view
        helperFunctions.updateHeader(userstore,nav_view)
    }
    //----ui  functions ----

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        // set listener for different activities
        // passing the userstore object
        when (item.itemId) {
            R.id.map -> {
                val intent = Intent(this, MapboxActivity2::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.myinventory -> {
                val intent = Intent(this, WalletSelectActivity::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.myprofile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.companions -> {
                val intent = Intent(this, MyFriendActivity::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.signout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
