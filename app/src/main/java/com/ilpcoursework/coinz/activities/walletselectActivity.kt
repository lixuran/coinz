package com.ilpcoursework.coinz.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_walletselect.*
import kotlinx.android.synthetic.main.app_bar_walletselect.*
import kotlinx.android.synthetic.main.content_walletselect.*

class walletselectActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var userstore: User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walletselect)
        setSupportActionBar(toolbar)
        userstore = getIntent().extras["useridentity"] as? User
        val bluetext: TextView = findViewById(R.id.bluerate) as TextView
        bluetext.text = userstore!!.shilrate.toString().split(".")[0] + "gold per kg"
        val redtext: TextView = findViewById(R.id.redrate) as TextView
        redtext.text = userstore!!.penyrate.toString().split(".")[0] + "gold per kg"
        val greentext: TextView = findViewById(R.id.greenrate) as TextView
        greentext.text = userstore!!.dolrrate.toString().split(".")[0] + "gold per kg"
        val yellowtext: TextView = findViewById(R.id.yellowrate) as TextView
        yellowtext.text = userstore!!.quidrate.toString().split(".")[0] + "gold per kg"
        blue_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity2::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "SHIL")
            startActivity(intent)
        }
        red_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity2::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "PENY")
            startActivity(intent)
        }
        yellow_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity2::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "QUID")
            startActivity(intent)
        }
        green_button.setOnClickListener { view ->
            val intent = Intent(this, MywalletActivity2::class.java)
            intent.putExtra("useridentity", userstore)
            intent.putExtra("color", "DOLR")
            startActivity(intent)
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val headerview = nav_view.getHeaderView(0)
        val user_name = headerview.findViewById<View>(R.id.user_name) as TextView
        val user_email = headerview.findViewById<View>(R.id.user_email) as TextView
        val gold_view = headerview.findViewById<View>(R.id.gold_view) as TextView
        val dolr_view = headerview.findViewById<View>(R.id.dolr_view) as TextView
        val peny_view = headerview.findViewById<View>(R.id.peny_view) as TextView
        val quid_view = headerview.findViewById<View>(R.id.quid_view) as TextView
        val shil_view = headerview.findViewById<View>(R.id.shil_view) as TextView

        user_name.text = userstore?.username
        user_email.text = userstore?.email
        gold_view.text = userstore?.gold.toString().split(".")[0]
        dolr_view.text = userstore?.mydolrs.toString().split(".")[0]
        peny_view.text = userstore?.mypenys.toString().split(".")[0]
        quid_view.text = userstore?.myquids.toString().split(".")[0]
        shil_view.text = userstore?.myshils.toString().split(".")[0]
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
