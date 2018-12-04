package com.ilpcoursework.coinz

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mywallet2.*
import kotlinx.android.synthetic.main.app_bar_mywallet2.*

class MywalletActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var userstore: User?=null
    private var color:String?=null
    private var selectedcoins = mutableListOf<Coin>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mywallet2)
        setSupportActionBar(toolbar)
        userstore = getIntent().extras["useridentity"] as? User
        color = getIntent().extras["color"] as? String



        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

//        val dbHelper = FeedReaderDbHelper(this)
//        val db = dbHelper.readableDatabase
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        val projection = arrayOf(BaseColumns._ID, FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_id, FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_value, FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_currency)
//
//        // Filter results WHERE "title" = 'My Title'
//        // val selection = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} = ?"
//        //val selectionArgs = arrayOf("My Title")
//
//        // How you want the results sorted in the resulting Cursor
//        //val sortOrder = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"
//
//        val cursor = db.query(
//                FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                null,              // The columns for the WHERE clause
//                null,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                null               // The sort order
//        )
//       val Coins = mutableListOf<Coin>()
//        with(cursor) {
//            while (moveToNext()) {
//                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
//                val itemvalue = getString(getColumnIndexOrThrow(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_value)).substring(1,5)
//                val itemcur = getString(getColumnIndexOrThrow(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_currency))
//
//                Coins.add(Coin(itemId,itemvalue.toDouble(),itemcur))
//            }
//        }
        val iterator= userstore!!.coins.iterator()
        while (iterator.hasNext()) {
            val coin = iterator.next()
            if(coin.currency ==color )
            {
                selectedcoins.add(0,coin)
            }
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(selectedcoins,userstore!!,this)
        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view2).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
    public fun updateuser(usertostore:User,index:Int){
        this.userstore=usertostore
        viewAdapter.notifyItemRemoved(index)

    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mywallet_activity2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
