package com.ilpcoursework.coinz.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.HelperFunctions
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.app_bar_profile.*
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
// the activity to show the user's current profile
class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var userstore: User?=null
    private var helperFunctions= HelperFunctions(this)
    private var db = FirebaseFirestore.getInstance()
    private var TAG="profile activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        userstore = intent.extras["useridentity"] as? User

        //initialise the slidebar header using information from the userstore
        nav_view.setNavigationItemSelectedListener(this)
        helperFunctions.updateHeader(userstore,nav_view)

        val penytext =findViewById<View>(R.id.peny) as TextView
        val dolrtext =findViewById<View>(R.id.dolr) as TextView
        val quidtext =findViewById<View>(R.id.quid) as TextView
        val shiltext =findViewById<View>(R.id.shil) as TextView
        val goldtext =findViewById<View>(R.id.gold) as TextView
        penytext.text =getString(R.string.penytext, userstore?.mypenys.toString().split(".")[0])
        dolrtext.text =getString(R.string.dolrtext, userstore?.mydolrs.toString().split(".")[0])
        quidtext.text =getString(R.string.quidtext, userstore?.myquids.toString().split(".")[0])
        shiltext.text =getString(R.string.shiltext, userstore?.myshils.toString().split(".")[0])
        goldtext.text =getString(R.string.goldtext, userstore?.gold.toString().split(".")[0])


        //set listeners
        val contract_button =findViewById<View>(R.id.contract_button) as Button
        contract_button.setOnClickListener{ _->
            showDialog()

        }
        val signout_button =findViewById<View>(R.id.signout_button) as Button
        signout_button.setOnClickListener { _ ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("InflateParams")
    private fun showDialog(){
        // show the dialog of the detail rules, also known as the yarl's contract
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater : LayoutInflater = layoutInflater
        val view : View = inflater.inflate(R.layout.my_help_dialog,null)
        builder.setView(view)
        builder.setPositiveButton("close") { dialog, which -> dialog!!.dismiss() }
        val dialog = builder.create()
        dialog.show()
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
                val intent = Intent(this, MapboxActivity2::class.java)
                intent.putExtra("useridentity", userstore)
                startActivity(intent)
            }
            R.id.myinventory -> {
                val intent = Intent(this, walletselectActivity::class.java)
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
                FirebaseAuth.getInstance().signOut();
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
