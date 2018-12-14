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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.DAO.friend
import com.ilpcoursework.coinz.HelperFunctions
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import com.ilpcoursework.coinz.adapters.FriendAdapter
import com.ilpcoursework.coinz.adapters.PendingFriendAdapter
import kotlinx.android.synthetic.main.activity_myfriend.*
import kotlinx.android.synthetic.main.app_bar_myfriend.*
import kotlinx.android.synthetic.main.content_myfriend.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
/**
 * the activity to add and remove friend
 */
class MyFriendActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var friendViewAdapter: RecyclerView.Adapter<*>
    private lateinit var friendViewManager: RecyclerView.LayoutManager
    private lateinit var pendingfriendRecyclerView: RecyclerView
    private lateinit var pendingfriendViewAdapter: RecyclerView.Adapter<*>
    private lateinit var pendingfriendViewManager: RecyclerView.LayoutManager
    private var userstore: User?=null
    private var db = FirebaseFirestore.getInstance()
    private val TAG ="myfriendactivity"
    private lateinit var friendlist :MutableList<friend>
    private var helperFunctions= HelperFunctions(this)

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

        //set navigation header information from user object
        nav_view.setNavigationItemSelectedListener(this)
        helperFunctions.updateHeader(userstore,nav_view)


        //initialise the recycler view for friend invitations and friend list
        friendViewManager = LinearLayoutManager(this)
         friendlist = userstore!!.friends
        friendViewAdapter = FriendAdapter(userstore!!.friends, this)
        friendRecyclerView = findViewById<RecyclerView>(R.id.friend_recyclerView).apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = friendViewManager
            // specify an viewAdapter (see also next example)
            adapter = friendViewAdapter

        }

        pendingfriendViewManager = LinearLayoutManager(this)
        pendingfriendlist = userstore!!.pendingfriends
        pendingfriendViewAdapter = PendingFriendAdapter(pendingfriendlist, this)
        pendingfriendRecyclerView = findViewById<RecyclerView>(R.id.pendingfriend_recyclerView).apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager =  pendingfriendViewManager
            // specify an viewAdapter (see also next example)
            adapter =  pendingfriendViewAdapter

        }

        // set realtime update listener to receive changes in the friend list and invitations
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
                userstore?.changestate==1 -> {
                    //clear all items in the friend list,which is used to fill the adapter,
                    // and reset it to the updated list
                    friendlist.clear()
                    friendViewAdapter.notifyDataSetChanged()
                    friendlist.addAll(userstore!!.friends)
                    friendViewAdapter.notifyItemRangeInserted(0,friendlist.size)
                    //friendViewAdapter.notifyItemInserted(0)
                    //after change is made reset for next view update
                    userstore?.changestate=0
                }
                userstore?.changestate==2 -> {
                    friendlist.clear()
                    friendViewAdapter.notifyDataSetChanged()
                    friendlist.addAll(userstore!!.friends)
                    friendViewAdapter.notifyItemRangeInserted(0,friendlist.size)
                    userstore?.changestate=0
                }
                userstore?.changestate==4 -> {
                    pendingfriendlist.clear()
                    pendingfriendViewAdapter.notifyDataSetChanged()
                    pendingfriendlist.addAll(userstore!!.pendingfriends)
                    pendingfriendViewAdapter.notifyItemRangeInserted(0,pendingfriendlist.size)

                    //pendingfriendViewAdapter.notifyItemInserted(0)
                    userstore?.changestate=0
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //set the listener for user adding friend
        addfriend_button.setOnClickListener { _ ->
            val usernameStr=username.text.toString()
            //if player request for adding a friend who s in the friend list
            if(userstore!!.friends.map { friend -> friend.username }.contains(usernameStr)) {
                Toast.makeText(this, "invalid username: already in friend list",
                        Toast.LENGTH_SHORT).show()
            }
            // if the player has already been sent an invite from the person who he want to send invite to ,
            // notify that such action can't be done
            else if(userstore!!.pendingfriends.map { friend -> friend.username }.contains(usernameStr)) {
                Toast.makeText(this, "player has already sent you an invitation, accept it to become friends",
                        Toast.LENGTH_SHORT).show()
            }
            // if the player try to add himself
            else if(usernameStr==userstore!!.username ){
                Toast.makeText(this, "invalid username:player can't add himself ",
                        Toast.LENGTH_SHORT).show()
            }
            else {
                // get request user info
                db.collection("users").whereEqualTo("username", usernameStr).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // check how many result are found
                        if (task.getResult()!!.isEmpty) {
                            Toast.makeText(this, "invalid username: no player found",
                                    Toast.LENGTH_SHORT).show()
                        } else if (task.getResult()?.size() == 1) {
                            // send a invitation to the target player if the target player has not received an invite from
                            // the current player , or has current player as his friend.
                            val friend = task.getResult()!!.documents.get(0).toObject(User::class.java)
                            if (!friend!!.pendingfriends.map { afriend -> afriend.email }.contains(userstore!!.email)) {
                                if(!friend.friends.map { afriend -> afriend.email }.contains(userstore!!.email)){
                                    friend.pendingfriends.add(0, friend(userstore!!.username, userstore!!.email))
                                    helperFunctions.friendReceiveUserInvite(friend, "friend invite",2)
                                }
                            }
                            Toast.makeText(this, "invitation sent",
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            //there shouldn't be two user with same username but just in case, set a warning and a toast
                            Log.w(TAG, "more than one user with username: $usernameStr")
                            Toast.makeText(this, "invalid username: multiple player found",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    /**
     * delete the friend indicated by position in the friend list when called upon
     * @param position the position in the friend list of the friend to be deleted
     */
    fun deleteFriend(position:Int) {
        //delete friend from friend list and delete user from the friend's friend list
        if(userstore!!.friends.size>position) {
            helperFunctions.friendRemoveUser(userstore!!, userstore!!.friends.get(position).email, "FriendAdapter", 2)
            userstore!!.friends.removeAt(position)
        }
        helperFunctions.updateUser(userstore!!, "FriendAdapter")
        //notify the adapter
        friendlist.clear()
        friendViewAdapter.notifyDataSetChanged()
        friendlist.addAll(userstore!!.friends)
        friendViewAdapter.notifyItemRangeInserted(0,friendlist.size)
    }

    /**
     * reject the friend invitation indicated by position in the pending friend list when called upon
     * @param position the position in the pending friend list of the invitation to be rejected
     */
    fun refuseFriend(position:Int) {
        //remove from invitations
        if(userstore!!.pendingfriends.size>position)
        userstore!!.pendingfriends.removeAt(position)
        helperFunctions.updateUser(userstore!!, "PendingFriendAdapter")
        //notify the adapter
        pendingfriendlist.clear()
        pendingfriendViewAdapter.notifyDataSetChanged()
        pendingfriendlist.addAll(userstore!!.pendingfriends)
        pendingfriendViewAdapter.notifyItemRangeInserted(0,pendingfriendlist.size)
    }

    /**
     * accept the friend invitation indicated by position in the pending friend list when called upon
     * @param position the position in the pending friend list of the invitation to be accepted
     */
    fun acceptFriend(position:Int) {
        val friend = userstore!!.pendingfriends.get(position)

            //add user to the friend's friend list
            helperFunctions.friendAddUser(userstore!!, friend.email, "PendingFriendAdapter", 2)
            if (userstore!!.pendingfriends.size > position) {
                // add friend to user's friend list and remove from invitations
                userstore!!.pendingfriends.removeAt(position)
                //if the friend is not already in the user's friend list
                if(!userstore!!.friends.map { afriend -> afriend.email }.contains(friend.email)) {
                    userstore!!.friends.add(0, friend)
                }
            }
            helperFunctions.updateUser(userstore!!, "PendingFriendAdapter")
            //notify the adapter
            pendingfriendlist.clear()
            pendingfriendViewAdapter.notifyDataSetChanged()
            pendingfriendlist.addAll(userstore!!.pendingfriends)
            pendingfriendViewAdapter.notifyItemRangeInserted(0, pendingfriendlist.size)

            friendlist.clear()
            friendViewAdapter.notifyDataSetChanged()
            friendlist.addAll(userstore!!.friends)
            friendViewAdapter.notifyItemRangeInserted(0, friendlist.size)

    }

    //---- ui funcitons ----

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
            //set listener for changing to other pages
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
