package com.ilpcoursework.coinz

import android.content.Context
import android.support.design.widget.NavigationView
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.activities.MywalletActivity

class HelperFunctions (context: Context){
    private var db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser?=null
    private var context:Context?=null
    private val internetdownPrompt = "can't not access databse, plz check your internet connection"
    init {
        mAuth = FirebaseAuth.getInstance()
        this.context=context
        user = mAuth?.currentUser
    }

    /**
     *  update the user document on the cloud with the local version
     *  @param userstore the local user object
     */
    fun updateUser(userstore : User, tag:String){

        db.collection("users")
                    .document(userstore.email).set(userstore)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " + userstore.email)

                    }.addOnFailureListener {
                        tryUpdateAgain(userstore,tag)
                    }

    }
    fun tryUpdateAgain(userstore : User, tag:String){

        db.collection("users")
                .document(userstore.email).set(userstore)
                .addOnSuccessListener {
                    Log.d(tag, "DocumentSnapshot added with ID: " + userstore.email)

                }.addOnFailureListener {
                    Toast.makeText(context, internetdownPrompt,
                            Toast.LENGTH_LONG).show()
                }

    }
    /**
     *  update the friends pending invitations after user send an invite
     *  @param friend the user's friend object, downloaded previously.
     */
    fun friendReceiveUserInvite( friend:User, tag:String,time:Int){
        db.collection("users").document(friend.email)
        // set changestate to indicate the operation down.

        friend.changestate=4
        db.collection("users")
                .document(friend.email).set(friend)
                .addOnSuccessListener {
                    Log.d(tag, "DocumentSnapshot added with ID: " +friend)
                }.addOnFailureListener {
                    if(time>0)
                    {
                        friendReceiveUserInvite( friend, tag,time-1)
                    }
                    else{
                        Toast.makeText(context, internetdownPrompt,
                                Toast.LENGTH_LONG).show()
                    }
                }
    }
    /**
     *  remove player from the the selected friend's friend list after player delete his friend
     *  @param friend friend 's email. used to download his document
     */
    fun friendRemoveUser(userstore: User, friend:String, tag:String,time:Int){
        val docRef = db.collection("users").document(friend)
        var frienduser : User?
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            frienduser= documentSnapshot.toObject(User::class.java)
            //find user from friend's friend list
            val index =frienduser!!.friends.map { friend -> friend.email }.indexOf(userstore.email)
            // and remove
            frienduser!!.friends.removeAt(index)
            // set changestate to indicate the operation down.
            frienduser?.changestate=1
            db.collection("users")
                    .document(friend).set(frienduser!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " +friend)

                    }.addOnFailureListener {
                        if(time>0)
                        {
                            friendRemoveUser( userstore,friend, tag,time-1)
                        }
                        else{
                            Toast.makeText(context, internetdownPrompt,
                                    Toast.LENGTH_LONG).show()
                        }
                    }
        }.addOnFailureListener {
            if(time>0)
            {
                friendRemoveUser( userstore,friend, tag,time-1)
            }
            else{
                Toast.makeText(context, internetdownPrompt,
                        Toast.LENGTH_LONG).show()
            }
        }
    }
    /**
     *  add player to his friend's friend list, after user accept the invitation
     *   @param friend friend 's email. used to download his document
     */
    fun friendAddUser(userstore: User, friend:String, tag:String,time:Int){
        val docRef = db.collection("users").document(friend)
        var frienduser : User?
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            frienduser= documentSnapshot.toObject(User::class.java)
            //friend add user to friend list
            frienduser?.friends?.add(0, com.ilpcoursework.coinz.DAO.friend(userstore.username, userstore.email))
            // set changestate to indicate the operation down.
            frienduser?.changestate=2

            db.collection("users")
                    .document(friend).set(frienduser!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " +friend)

                    }.addOnFailureListener {
                        if(time>0)
                        {
                            friendAddUser( userstore,friend, tag,time-1)
                        }
                        else{
                            Toast.makeText(context, internetdownPrompt,
                                    Toast.LENGTH_LONG).show()
                        }
                    }
        }.addOnFailureListener {
            if(time>0)
            {
                friendAddUser( userstore,friend, tag,time-1)
            }
            else{
                Toast.makeText(context, internetdownPrompt,
                        Toast.LENGTH_LONG).show()
            }
        }

    }
    /**
     *   friend add the coin the player send
     *   @param friendemail  friend 's email. used to download his document
     *   @param coin the coin to send
     */
    fun friendAddCoin(friendemail:String, coin: Coin, tag:String,time:Int,activity: MywalletActivity){
        val docRef = db.collection("users").document(friendemail)
        var frienduser: User?
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            frienduser= documentSnapshot.toObject(User::class.java)
            // indicate the coin is a gift, and add to the friend's coin collection
            coin.type=1
            frienduser?.coins?.add(0,coin)
            when (coin.currency) {
                "SHIL" -> { frienduser?.myshils =  frienduser?.myshils!!+coin.value }
                "DOLR" -> {frienduser?.mydolrs =  frienduser?.mydolrs!!+coin.value }
                "QUID" -> {frienduser?.myquids =  frienduser?.myquids!!+coin.value }
                "PENY" ->{ frienduser?.mypenys =  frienduser?.mypenys!!+coin.value }
            }
            // set changestate to indicate the operation down.
            frienduser?.changestate=3
            // update friend's state
            db.collection("users")
                    .document(friendemail).set(frienduser!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " +friendemail)

                    }.addOnFailureListener {
                        if(time>0)
                        {
                            friendAddCoin( friendemail,coin, tag,time-1,activity)
                        }
                        else{
                            Toast.makeText(context, internetdownPrompt,
                                    Toast.LENGTH_LONG).show()
                            //sending failed, try to return to the original state.
                            activity.addPlayerCoin(coin)
                        }
                    }
        }.addOnFailureListener {
            if(time>0)
            {
                friendAddCoin( friendemail,coin, tag,time-1,activity)
            }
            else{
                Toast.makeText(context, internetdownPrompt,
                        Toast.LENGTH_LONG).show()
                activity.addPlayerCoin(coin)
            }
        }
    }
    /**
     *  update the navigation header by calling update head value.
     */
    fun updateHeader(user:User?,nav_view:NavigationView){
        val headerView =nav_view.getHeaderView(0)
        val username=headerView.findViewById<View>(R.id.user_name)as TextView
        val userEmail=headerView.findViewById<View>(R.id.user_email)as TextView
        val goldView=headerView.findViewById<View>(R.id.gold_view)as TextView
        val dolrView=headerView.findViewById<View>(R.id.dolr_view)as TextView
        val penyView=headerView.findViewById<View>(R.id.peny_view)as TextView
        val quidView=headerView.findViewById<View>(R.id.quid_view)as TextView
        val shilView=headerView.findViewById<View>(R.id.shil_view)as TextView
        updateHeaderValue(user,username,userEmail ,goldView,dolrView,penyView,quidView,shilView)
    }
    /**
     * update the navigation header with the local user instance
     * @param user the local user instance
     */
    fun updateHeaderValue(user:User?,username:TextView,userEmail :TextView,goldView:TextView,dolrView:TextView,penyView:TextView,quidView:TextView,shilView:TextView){
        username.text = user?.username
        userEmail.text = user?.email
        goldView.text = user?.gold.toString().split(".")[0]
        dolrView.text = user?.mydolrs.toString().split(".")[0]
        penyView.text = user?.mypenys.toString().split(".")[0]
        quidView.text = user?.myquids.toString().split(".")[0]
        shilView.text = user?.myshils.toString().split(".")[0]
    }
}