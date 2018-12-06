package com.ilpcoursework.coinz.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
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
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.app_bar_profile.*

class profileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var userstore: User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        userstore = getIntent().extras["useridentity"] as? User

        nav_view.setNavigationItemSelectedListener(this)
        val penytext =findViewById<View>(R.id.peny) as TextView
        val dolrtext =findViewById<View>(R.id.dolr) as TextView
        val quidtext =findViewById<View>(R.id.quid) as TextView
        val shiltext =findViewById<View>(R.id.shil) as TextView
        val goldtext =findViewById<View>(R.id.gold) as TextView
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
        penytext.text ="fire dragon bone: "+userstore?.mypenys.toString().split(".")[0]
        dolrtext.text ="ancient dragon bone: "+userstore?.mydolrs.toString().split(".")[0]
        quidtext.text ="blood dragon bone: "+userstore?.myquids.toString().split(".")[0]
        shiltext.text ="frost dragon bone: "+userstore?.myshils.toString().split(".")[0]
        goldtext.text ="gold: "+userstore?.gold.toString().split("")[0]



        val contract_button =findViewById<View>(R.id.contract_button) as Button
        contract_button.setOnClickListener{view->
            showDialog()

        }
        val signout_button =findViewById<View>(R.id.signout_button) as Button
        signout_button.setOnClickListener { view ->
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("InflateParams")
    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater : LayoutInflater = layoutInflater
        val view : View = inflater.inflate(R.layout.row_dialog,null)
        builder.setView(view)
        builder.setPositiveButton("close",object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which:Int){
                dialog!!.dismiss()
            }
        })
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
