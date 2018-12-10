package com.ilpcoursework.coinz.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.HelperFunctions
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_walletselect.*
import kotlinx.android.synthetic.main.app_bar_walletselect.*
import kotlinx.android.synthetic.main.content_walletselect.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class walletselectActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var userstore: User?=null
    private var helperFunctions= HelperFunctions(this)
    private var db = FirebaseFirestore.getInstance()
    private val TAG ="wallectselect"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walletselect)
        setSupportActionBar(toolbar)
        userstore = intent.extras["useridentity"] as? User
        val bluetext: TextView = findViewById(R.id.bluerate) as TextView
        val redtext: TextView = findViewById(R.id.redrate) as TextView
        val greentext: TextView = findViewById(R.id.greenrate) as TextView
        val yellowtext: TextView = findViewById(R.id.yellowrate) as TextView

        yellowtext.text =getString(R.string.quidrate, userstore?.quidrate.toString().split(".")[0])
        bluetext.text =getString(R.string.shilrate, userstore?.shilrate.toString().split(".")[0])
        redtext.text =getString(R.string.penyrate, userstore?.penyrate.toString().split(".")[0])
        greentext.text =getString(R.string.dolrrate, userstore?.dolrrate.toString().split(".")[0])
        // set the listeners for the buttons to open wallet for each type of coins
        // the type is passed through the intent
        blue_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "SHIL")
            startActivity(intent)
        }
        red_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "PENY")
            startActivity(intent)
        }
        yellow_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "QUID")
            startActivity(intent)
        }
        green_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "DOLR")
            startActivity(intent)
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //initialise the slidebar header using information from the userstore
        nav_view.setNavigationItemSelectedListener(this)
        helperFunctions.updateHeader(userstore,nav_view)

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
        // pass on user information through intent using userstore
        when (item.itemId) {
            R.id.map -> {
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
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
