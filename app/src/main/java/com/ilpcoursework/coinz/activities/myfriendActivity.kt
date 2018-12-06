package com.ilpcoursework.coinz.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.DAO.friend
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.adapters.friendAdapter
import com.ilpcoursework.coinz.adapters.pendingfriendAdapter
import com.ilpcoursework.coinz.helperfunctions
import kotlinx.android.synthetic.main.activity_myfriend.*
import kotlinx.android.synthetic.main.app_bar_myfriend.*
import kotlinx.android.synthetic.main.content_myfriend.*

class myfriendActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var friend_recyclerView: RecyclerView
    private lateinit var friend_viewAdapter: RecyclerView.Adapter<*>
    private lateinit var friend_viewManager: RecyclerView.LayoutManager
    private lateinit var pendingfriend_recyclerView: RecyclerView
    private lateinit var pendingfriend_viewAdapter: RecyclerView.Adapter<*>
    private lateinit var pendingfriend_viewManager: RecyclerView.LayoutManager
    private var userstore: User?=null
    private var db = FirebaseFirestore.getInstance();
    private val TAG ="myfriendactivity"
    private lateinit var friendlist :MutableList<friend>

    private lateinit var pendingfriendlist :MutableList<friend>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myfriend)
        setSupportActionBar(toolbar)
        userstore = intent.extras["useridentity"] as? User



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

        friend_viewManager = LinearLayoutManager(this)
         friendlist = userstore!!.friends
        friend_viewAdapter = friendAdapter(userstore!!.friends, this)
        friend_recyclerView = findViewById<RecyclerView>(R.id.friend_recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = friend_viewManager

            // specify an viewAdapter (see also next example)
            adapter = friend_viewAdapter

        }
        friend_recyclerView.getRecycledViewPool().clear();
        friend_viewAdapter.notifyDataSetChanged();
        pendingfriend_viewManager = LinearLayoutManager(this)
        pendingfriendlist = userstore!!.pendingfriends
        pendingfriend_viewAdapter = pendingfriendAdapter(pendingfriendlist, this)
        pendingfriend_recyclerView = findViewById<RecyclerView>(R.id.pendingfriend_recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager =  pendingfriend_viewManager

            // specify an viewAdapter (see also next example)
            adapter =  pendingfriend_viewAdapter

        }

        var docRef = db.collection("users").document(userstore!!.email)
        docRef.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w(TAG, "listen:error", firebaseFirestoreException)
            }
            userstore= documentSnapshot?.toObject(User::class.java)
            if(userstore?.changestate==1){
                friendlist.clear()
                friend_viewAdapter.notifyDataSetChanged();
                friendlist.addAll(userstore!!.friends)
                friend_viewAdapter.notifyItemRangeInserted(0,friendlist.size)
//                friend_viewAdapter.notifyItemInserted(0)
                userstore?.changestate=0
            }
            else if (userstore?.changestate==2){
                friendlist.clear()
                friend_viewAdapter.notifyDataSetChanged();
                friendlist.addAll(userstore!!.friends)
                friend_viewAdapter.notifyItemRangeInserted(0,friendlist.size)
                userstore?.changestate=0
            }
            else if (userstore?.changestate==4){
                pendingfriendlist.clear()
                pendingfriend_viewAdapter.notifyDataSetChanged();
                pendingfriendlist.addAll(userstore!!.pendingfriends)
                pendingfriend_viewAdapter.notifyItemRangeInserted(0,pendingfriendlist.size)

                //pendingfriend_viewAdapter.notifyItemInserted(0)
                userstore?.changestate=0
            }
        }
    }

    override fun onStart() {
        super.onStart()

        addfriend_button.setOnClickListener { view ->
            val usernameStr=username.text.toString()
            if(usernameStr!=userstore!!.username && !userstore!!.friends.map { friend -> friend.username }.contains(usernameStr)) {
                db.collection("users").whereEqualTo("username", usernameStr).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.getResult()!!.isEmpty()) {
                            Toast.makeText(this, "invalid username: no player found",
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.getResult()?.size() == 1) {

                            val friend = task.getResult()!!.documents.get(0).toObject(User::class.java)
                            if (!friend!!.pendingfriends.map { friend -> friend.email }.contains(userstore!!.email)) {
                                friend.pendingfriends.add(0, friend(userstore!!.username, userstore!!.email))
                                helperfunctions.friendReceiveUserInvite(friend, "friend invite")
                            }
                            Toast.makeText(this, "invitation sent",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "more than one user with username: " + usernameStr);
                            Toast.makeText(this, "invalid username: multiple player found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }


    }

    public fun deletefriend(position:Int) {
        val friend = userstore!!.friends.get(position)
        helperfunctions.Friendremoveuser(userstore!!, userstore!!.friends.get(position).email, "friendAdapter")
        userstore!!.friends.removeAt(position)
        helperfunctions.updateUser(userstore!!, "friendAdapter")
        //notify the adapter
        friendlist.clear()
        friend_viewAdapter.notifyDataSetChanged();
        friendlist.addAll(userstore!!.friends)
        friend_viewAdapter.notifyItemRangeInserted(0,friendlist.size)

    }
    public fun refusefriend(position:Int) {
        val friend = userstore!!.pendingfriends.get(position)
        userstore!!.pendingfriends.removeAt(position)
        helperfunctions.updateUser(userstore!!, "pendingfriendAdapter")
        //notify the adapter
        pendingfriendlist.clear()
        pendingfriend_viewAdapter.notifyDataSetChanged();
        pendingfriendlist.addAll(userstore!!.pendingfriends)
        pendingfriend_viewAdapter.notifyItemRangeInserted(0,pendingfriendlist.size)
    }
    public fun acceptfriend(position:Int) {
        val friend = userstore!!.pendingfriends.get(position)

        helperfunctions.Friendadduser(userstore!!, friend.email, "pendingfriendAdapter")
        userstore!!.pendingfriends.removeAt(position)
        userstore!!.friends.add(0,friend)
        helperfunctions.updateUser(userstore!!, "pendingfriendAdapter")
        //notify the adapter
        pendingfriendlist.clear()
        pendingfriend_viewAdapter.notifyDataSetChanged();
        pendingfriendlist.addAll(userstore!!.pendingfriends)
        pendingfriend_viewAdapter.notifyItemRangeInserted(0,pendingfriendlist.size)

        friendlist.clear()
        friend_viewAdapter.notifyDataSetChanged();
        friendlist.addAll(userstore!!.friends)
        friend_viewAdapter.notifyItemRangeInserted(0,friendlist.size)

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
