package com.ilpcoursework.coinz.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.adapters.MyAdapter
import com.ilpcoursework.coinz.adapters.frienddialogAdapter
import com.ilpcoursework.coinz.helperfunctions
import kotlinx.android.synthetic.main.activity_mywallet2.*
import kotlinx.android.synthetic.main.app_bar_mywallet2.*



class MywalletActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var frienddialogrecyclerView: RecyclerView
    lateinit var frienddialogviewAdapter: RecyclerView.Adapter<*>
    lateinit var frienddialogviewManager: RecyclerView.LayoutManager
    private var userstore: User?=null
    private var color:String?=null
    private lateinit var dialog:Dialog
    private var selectedcoins = mutableListOf<Coin>()
    private var db = FirebaseFirestore.getInstance();
    private val TAG ="mywallet"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mywallet2)
        setSupportActionBar(this.toolbar)
        userstore = intent.extras["useridentity"] as? User
        color = intent.extras["color"] as? String



        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val headerview =nav_view.getHeaderView(0)
        val user_name=headerview.findViewById<View>(R.id.user_name)as TextView
        val user_email=headerview.findViewById<View>(R.id.user_email)as TextView
        val gold_view=headerview.findViewById<View>(R.id.gold_view)as TextView
        val dolr_view=headerview.findViewById<View>(R.id.dolr_view)as TextView
        val peny_view=headerview.findViewById<View>(R.id.peny_view)as TextView
        val quid_view=headerview.findViewById<View>(R.id.quid_view)as TextView
        val shil_view=headerview.findViewById<View>(R.id.shil_view)as TextView

        user_name.text = userstore?.username
        user_email.text = userstore?.email
        gold_view.text = userstore?.gold.toString().split(".")[0]
        dolr_view.text = userstore?.mydolrs.toString().split(".")[0]
        peny_view.text = userstore?.mypenys.toString().split(".")[0]
        quid_view.text = userstore?.myquids.toString().split(".")[0]
        shil_view.text = userstore?.myshils.toString().split(".")[0]


        val iterator= userstore!!.coins.iterator()
        while (iterator.hasNext()) {
            val coin = iterator.next()
            if(coin.currency ==color )
            {
                selectedcoins.add(0,coin)
            }
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(selectedcoins, userstore!!, this)
        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view2).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

    }
    public fun sendtobank(position:Int){
        if(userstore!!.bankedtoday>=25)
        {
            Toast.makeText(this, "maximum banking limit reached",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            userstore!!.bankedtoday+=1
            val cointosent = selectedcoins.get(position)
            var rate: Double = 0.0
            when (cointosent.currency) {
                "SHIL" -> {rate = userstore!!.shilrate
                    userstore!!.myshils-=cointosent.value}
                "DOLR" -> {rate = userstore!!.dolrrate
                    userstore!!.mydolrs-=cointosent.value}
                "QUID" -> {rate = userstore!!.quidrate
                    userstore!!.myquids-=cointosent.value}

                "PENY" ->{ rate = userstore!!.penyrate
                    userstore!!.mypenys-=cointosent.value}

            }
            val addition =cointosent.value * rate
            userstore!!.gold = userstore!!.gold + addition
            userstore!!.coins.remove(cointosent)
            update_selected_coins()
            helperfunctions.updateUser(userstore!!, "myadapter")
        }
    }
    public fun showdialog(coin:Coin){
        userstore?.selectedcoin=userstore!!.coins.indexOf(coin)
        if(userstore!!.friends.size>0) {


            dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_layout)

            frienddialogviewManager = LinearLayoutManager(this)
            frienddialogviewAdapter = frienddialogAdapter(userstore!!.friends, userstore!!, this)
            frienddialogrecyclerView = dialog.findViewById<RecyclerView>(R.id.dialog_recycler_view)!!
            frienddialogrecyclerView.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = frienddialogviewManager

                // specify an viewAdapter (see also next example)
                adapter = frienddialogviewAdapter

            }
            dialog.show()
        }
        else{
            Toast.makeText(this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public fun sendfriend(coinindex:Int,friendindex:Int){
        val friend = userstore!!.friends.get(friendindex)
        val coin = userstore!!.coins.get(coinindex)
        userstore!!.coins.remove(coin)
        Toast.makeText(this, "coin sent",
                Toast.LENGTH_SHORT).show();
        helperfunctions.updateUser(userstore!!, "frienddialogAdapter")
        helperfunctions.Friendaddcoin(friend.email, coin, "frienddialogAdapter")
        //close the dialog
        dialog.dismiss()
        update_selected_coins()

    }

    private fun update_selected_coins(){
        selectedcoins.clear()
        viewAdapter.notifyDataSetChanged();
        val iterator= userstore!!.coins.iterator()
        while (iterator.hasNext()) {
            val coin = iterator.next()
            if(coin.currency ==color )
            {
                selectedcoins.add(0,coin)
            }
        }
        viewAdapter.notifyItemRangeInserted(0,selectedcoins.size)
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.map -> {
                // Handle the camera action
                val intent = Intent(this, mapboxActivity2::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.myinventory -> {
                val intent = Intent(this, walletselectActivity::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.myprofile -> {
                val intent = Intent(this, profileActivity::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.companions -> {
                val intent = Intent(this, myfriendActivity::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.signout -> {
                FirebaseAuth.getInstance().signOut();
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
