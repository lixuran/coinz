package com.ilpcoursework.coinz

import android.os.Bundle
import android.provider.BaseColumns
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_myfriend.*

class myfriendActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myfriend)
        setSupportActionBar(toolbar)

        send_message_button.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(BaseColumns._ID, FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_id, FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_value, FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_currency)

        // Filter results WHERE "title" = 'My Title'
        // val selection = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} = ?"
        //val selectionArgs = arrayOf("My Title")

        // How you want the results sorted in the resulting Cursor
        //val sortOrder = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"

        val cursor = db.query(
                FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        )
        val Coins = mutableListOf<Coin>()
//        with(cursor) {
//            while (moveToNext()) {
//                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
//                val itemvalue = getDouble(getColumnIndexOrThrow(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_value))
//                val itemcur = getString(getColumnIndexOrThrow(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_currency))
//
//                Coins.add(Coin(itemId,itemvalue,itemcur))
//            }
//        }
//        viewManager = LinearLayoutManager(this)
//        viewAdapter = MyAdapter(Coins)
//        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view2).apply {
//            // use this setting to improve performance if you know that changes
//            // in content do not change the layout size of the RecyclerView
//            setHasFixedSize(true)
//
//            // use a linear layout manager
//            layoutManager = viewManager
//
//            // specify an viewAdapter (see also next example)
//            adapter = viewAdapter
//
//        }
    }

}
