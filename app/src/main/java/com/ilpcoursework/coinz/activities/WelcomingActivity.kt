package com.ilpcoursework.coinz.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_welcoming.*
// the activity to show introduction to new user
class WelcomingActivity : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser?=null
    private var userstore: User?=null
    private val TAG="welcoming activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcoming)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth?.getCurrentUser()

    }
    override fun onStart() {
        //get userstore, the object to store user information from the internet
        super.onStart()
        val docRef = db.collection("users").document(user!!.email!!)
        downloadAttempt(2,docRef)
    }
    private fun downloadAttempt(count:Int,docRef:DocumentReference){
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            userstore= documentSnapshot.toObject(User::class.java)
            button.setOnClickListener { _ ->
                val intent = Intent(this, MapboxActivity2::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
        }.addOnFailureListener {
            if (count>0){
                downloadAttempt(count-1,docRef)
            }
            // retry continues to fail.
            else{
                Log.w(TAG, "download user data:failure internet down")
                Toast.makeText(this, "can't not access databse, plz check your internet connection",
                        Toast.LENGTH_LONG).show()
            }
        }
    }
}
