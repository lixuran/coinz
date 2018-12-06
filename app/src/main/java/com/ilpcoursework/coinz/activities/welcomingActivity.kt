package com.ilpcoursework.coinz.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_welcoming.*

class welcomingActivity : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance();
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser?=null
    private var userstore: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcoming)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth?.getCurrentUser();

    }
    override fun onStart() {

        super.onStart()
        val docRef = db.collection("users").document(user!!.email!!);
        docRef.get().addOnSuccessListener {
            documentSnapshot ->
            userstore= documentSnapshot.toObject(User::class.java)
            button.setOnClickListener { view ->
                val intent = Intent(this, mapboxActivity2::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
        }
    }
}
