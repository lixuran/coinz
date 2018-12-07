package com.ilpcoursework.coinz

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User

object helperfunctions {
    private var db = FirebaseFirestore.getInstance();
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser?=null
    init {
        mAuth = FirebaseAuth.getInstance()

        user = mAuth?.getCurrentUser()
    }
    public fun updateUser(userstore : User, tag:String){
            db.collection("users")
                    .document(userstore.email).set(userstore)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " + userstore.email);

                    }
    }
    public fun friendReceiveUserInvite( friend:User, tag:String){
        val docRef = db.collection("users").document(friend.email);

        friend.changestate=4
        db.collection("users")
                .document(friend.email).set(friend)
                .addOnSuccessListener {
                    Log.d(tag, "DocumentSnapshot added with ID: " +friend)
                }
    }
    public fun Friendremoveuser(userstore: User, friend:String, tag:String){
        val docRef = db.collection("users").document(friend);
        var frienduser : User? =null
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            frienduser= documentSnapshot.toObject(User::class.java)
            val index =frienduser!!.friends.map { friend -> friend.email }.indexOf(userstore.email)
            frienduser!!.friends.removeAt(index)
            frienduser?.changestate=1
            db.collection("users")
                    .document(friend).set(frienduser!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " +friend);

                    }


        }



    }
    public fun Friendadduser(userstore: User, friend:String, tag:String){
        val docRef = db.collection("users").document(friend);
        var frienduser : User? =null
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            frienduser= documentSnapshot.toObject(User::class.java)
            frienduser?.friends?.add(0, com.ilpcoursework.coinz.DAO.friend(userstore.username, userstore.email))
            frienduser?.changestate=2

            db.collection("users")
                    .document(friend).set(frienduser!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " +friend);

                    }
        }

    }
    public fun Friendaddcoin(friendemail:String, coin: Coin, tag:String){
        val docRef = db.collection("users").document(friendemail);
        var frienduser : User? =null
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            frienduser= documentSnapshot.toObject(User::class.java)
            coin.type=1
            frienduser?.coins?.add(0,coin)
            when (coin.currency) {
                "SHIL" -> { frienduser?.myshils =  frienduser?.myshils!!+coin.value }
                "DOLR" -> {frienduser?.mydolrs =  frienduser?.mydolrs!!+coin.value }
                "QUID" -> {frienduser?.myquids =  frienduser?.myquids!!+coin.value }
                "PENY" ->{ frienduser?.mypenys =  frienduser?.mypenys!!+coin.value }

            }
            frienduser?.changestate=3

            db.collection("users")
                    .document(friendemail).set(frienduser!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " +friendemail);

                    }
        }



    }
}