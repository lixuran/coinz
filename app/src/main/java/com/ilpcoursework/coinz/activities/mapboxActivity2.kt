package com.ilpcoursework.coinz.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.DownloadCompleteListener
import com.ilpcoursework.coinz.DownloadFileTask
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import kotlinx.android.synthetic.main.activity_mapbox2.*
import kotlinx.android.synthetic.main.app_bar_mapbox2.*
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class mapboxActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback, PermissionsListener, LocationEngineListener,MapboxMap.OnMapClickListener, DownloadCompleteListener {
    private lateinit var  fc: FeatureCollection
    private var featureList: List<Feature>? = null

    private val tag = "MapboxActivity"
    private var mapView: MapView? = null
    private var map: MapboxMap? = null

    private lateinit var originLocation: Location
    private lateinit var permissionsManager: PermissionsManager
    private var locationEngine: LocationEngine? =null
    private var locationLayerPlugin: LocationLayerPlugin? = null

    private lateinit var downloader: DownloadFileTask

    private var db = FirebaseFirestore.getInstance();
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser?=null
    private var userstore: User?=null

    private var coinsmapping = hashMapOf("SHIL" to R.drawable.bluedragon,"DOLR" to R.drawable.greendragon,"QUID" to R.drawable.yellowdragon,"PENY" to R.drawable.reddragon)
    private var icons:IntArray = intArrayOf(R.drawable.bluedragon, R.drawable.greendragon, R.drawable.yellowdragon, R.drawable.reddragon)

    //---- overide lifecycle funcitons-----

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapbox2)
        setSupportActionBar(toolbar)
        // from signup or welcoming activity get downloaded user information
        userstore = intent.extras["useridentity"] as? User

        //set navigation slidebar
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        //set slidebar header info
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

        //initialise mapbox
        Mapbox.getInstance(this, getString(R.string.access_token_public))
        mapView = findViewById(R.id.mapboxMapView)
        mapView?.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth?.getCurrentUser()
        mapView?.getMapAsync(this)

    }

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        //start location engine
        try {
            locationEngine?.requestLocationUpdates()
        } catch (ignored: SecurityException) {
            //this try catch is from piazza might need redo
            locationEngine?.addLocationEngineListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()

    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        locationEngine?.removeLocationEngineListener(this);//this is from piazza
        locationEngine?.removeLocationUpdates()
        //locationLayerPlugin?.onStop()
        mapView?.onStop()
        // All objects are from android.context.Context
        updateUser()


    }
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        locationEngine?.deactivate()
    }
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (outState != null) {
            mapView?.onSaveInstanceState(outState)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    //-----ui functions----

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
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //----- implement map and location funcitons----

    /**
     *  when map is ready , start to load the coins and enable
     *  the location functionality.
     */
    override fun onMapReady(mapboxMap: MapboxMap?) {
        if (mapboxMap == null) {
            Log.d(tag, "[onMapReady] mapboxMap is null")
        } else {
            map = mapboxMap
            // Set user interface options
            map?.uiSettings?.isCompassEnabled = true
            map?.uiSettings?.isZoomControlsEnabled = true

            val curdatestring =getcurdate()
            val url = "http://homepages.inf.ed.ac.uk/stg/coinz/$curdatestring/coinzmap.geojson"
            //check if the date has changed since last login
            if(userstore?.lastdate!=curdatestring || userstore?.lastdate==null) {
                userstore?.lastdate = curdatestring
                //if date changed , update user gold with interest
                userstore?.gold =userstore!!.gold* updategold(userstore!!.lastdate!!)
                //reset all date related information in user
                userstore?.update_settings()
                updateUser()
                downloader = DownloadFileTask(this)
                downloader.execute(url)
            }
            else{
                renderCoins(null)

            }
            // Make location information available
            // call this function only when map is ready to make sure the location shows properly
            enableLocation()
        }
    }

    /**
     * on downlaod complete, save todays rate to the user object, and render coins to give player
     * information on the boarder of the playable area
     * @param result the string that contains geojson style coin information
     */
    override fun downloadComplete(result: String) {
        save_rate_preferrences(result)
        renderCoins(null)
    }


    /**
     * enable tracking location if have permission to accesss location
     */
    private fun enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            Log.d(tag, "Permissions are granted")
            initialiseLocationEngine()
            initialiseLocationLayer()
        } else {
            Log.d(tag, "Permissions are not granted")
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    /**
     * set location engine parameter including update speed and task priority
     */
    @SuppressWarnings("MissingPermission")
    private fun initialiseLocationEngine() {
        locationEngine = LocationEngineProvider(this)
                .obtainBestLocationEngineAvailable()
        locationEngine?.apply {
            interval = 5000 // preferably every 5 seconds
            fastestInterval = 1000 // at most every second
            priority = LocationEnginePriority.HIGH_ACCURACY
            activate()
        }
        locationEngine?.requestLocationUpdates()
        val lastLocation = locationEngine?.lastLocation
        if (lastLocation != null) {
            originLocation = lastLocation
            setCameraPosition(lastLocation)

        } else {
            locationEngine?.addLocationEngineListener(this)
        }
    }

    // initialise location layer to tract user position
    @SuppressWarnings("MissingPermission")
    private fun initialiseLocationLayer() {
        if (mapView == null) {
            Log.d(tag, "mapView is null")
        } else {
            if (map == null) {
                Log.d(tag, "map is null")
            } else {
            }
            locationLayerPlugin = LocationLayerPlugin(mapView!!,
                    map!!, locationEngine)
            locationLayerPlugin?.apply {
                setLocationLayerEnabled(true)
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.NORMAL
            }
        }
    }
    //inherited from listener
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Log.d(tag, "Permissions: $permissionsToExplain")
    }

    /**
     *   on location change , automatically collect those coins that
     *   are within collectable distance, and render the remaining coins.
     *   @param location the updated location
     */
    override fun onLocationChanged(location: Location?) {
        if (location == null) {
            Log.d(tag, "[onLocationChanged] location is null")
        } else {
            originLocation = location
            setCameraPosition(originLocation)
            var counter =-1
            fc = FeatureCollection.fromJson(userstore!!.result!!)
            featureList = fc.features()
            for (f in featureList.orEmpty()) {
                val g = f.geometry()
                val point = g as Point
                counter++
                //
                val distance = LatLng(originLocation.latitude,originLocation.longitude) .distanceTo(LatLng(point.latitude(),point.longitude()))
                if(distance<100 && userstore!!.collectedcoins[counter]==0){
                    userstore!!.collectedcoins.set(counter,1)
                    userstore!!.collectedtoday= userstore!!.collectedtoday +1
                    val id =  f.properties()?.get("id").toString()
                    val value =f.properties()?.get("value").toString().substring(1,5)
                    val currency = f.properties()?.get("currency").toString().substring(1,5)
                    userstore!!.coins.add(Coin(id, value.toDouble(), currency, userstore?.lastdate!!))
                    when(currency) {
                        "SHIL" ->  userstore!!.myshils += value.toDouble()
                        "DOLR" ->  userstore!!.mydolrs += value.toDouble()
                        "QUID" ->  userstore!!.myquids += value.toDouble()
                        "PENY" ->  userstore!!.mypenys += value.toDouble()
                    }
                }

            }
            updateUser()
            map?.clear()
            renderCoins(location)
        }
    }

    private fun setCameraPosition(location: Location) {
        val latlng = LatLng(location.latitude, location.longitude)
        map?.animateCamera(CameraUpdateFactory.newLatLng(latlng))
    }
    // inherited for location listener
    override fun onConnected() {
        Log.d(tag, "[onConnected] requesting location updates")
        locationEngine?.requestLocationUpdates()
    }
    //request for permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onPermissionResult(granted: Boolean) {
        Log.d(tag, "[onPermissionResult] granted == $granted")
        if(granted)
        {
            enableLocation()
        }
        else
        {
            Log.e(tag, "[onPermissionResult] granted == $granted")
            Toast.makeText(this, "location permission not granted.",
                    Toast.LENGTH_SHORT).show();
        }
    }

   override fun onMapClick(point: LatLng) {
//        TODO("not implemented")
//        click on coins show coin info (perferably as a panel(or sidepanel )) this is implemented but can be improved
//        click on house show house info (as another activity)
//        click on wallet show personal info?? is this included?
//        or instead of automatically collect all coins user have to collect them manually.
    }

    //helper functions below
    /**
     *@param lastdate the last time when user login
     *@return the ratio to multiple the current gold user possess
     */
    private fun updategold(lastdate:String):Double{
        // calculate the time between last login and current login
        val lastdates = lastdate.split("/").map { item->item.toInt() }
        val date1= LocalDate.of(lastdates[0],lastdates[1],lastdates[2])
        val date2 = LocalDate.now()
        val days = date1.until(date2, ChronoUnit.DAYS)
        return 1.001.pow(days.toInt())
    }

    /**
     * render the coins on the map, based on the location information.
     * only coins that are within viewable distance and haven't been colloceted yet would be
     * shown on the map
     * @param location the updated location
     */
    private fun renderCoins(location: Location?) {
        var index = 0
        fc = FeatureCollection.fromJson(userstore!!.result!!)
        featureList = fc.features()

        for (f in featureList.orEmpty()) {
            //for each feature get the location of the coin
            if(userstore!!.collectedcoins[index]==1) {
                index += 1
                continue
            }
            index += 1
            val g = f.geometry()
            val p = f.properties()?.asJsonObject
            val currency:String = p?.get("currency").toString().substring(1,5)
            val point: Point = g as Point
            // calculate distance between current location and coin position
            // if location is unknown then assume user can see the coin
            var distance: Double =0.0
            if(location!= null ){  distance = LatLng(location.latitude,location.longitude) .distanceTo(LatLng(point.latitude(),point.longitude()))}
            else {
                distance= 0.0
            }
            if(distance <250){
                val iconFactory = IconFactory.getInstance(this);
                val icon = iconFactory.fromResource(coinsmapping.get(currency)!!)

                // Add the custom icon marker to the map
                map?.addMarker(MarkerOptions()
                        .position(LatLng(point.latitude(), point.longitude()))
                        .title(p?.get("marker-symbol").toString())
                        .snippet(p?.get("currency").toString().substring(1,5)+": "+p?.get("value").toString().substring(1,4))
                        .icon(icon)
                )
            }
        }
    }

    /**
     *   save currency exchange rate from the downloaded string
     */
    private fun save_rate_preferrences(result:String){
        val jsonObj = JSONObject(result)
        val rates = jsonObj.getJSONObject("rates")
        val shil = rates.getDouble("SHIL")
        val dolr = rates.getDouble("DOLR")
        val quid = rates.getDouble("QUID")
        val peny = rates.getDouble("PENY")
        userstore?.result=result
        userstore?.shilrate=shil
        userstore?.dolrrate=dolr
        userstore?.quidrate=quid
        userstore?.penyrate=peny
        updateUser()

    }

    private fun getcurdate ():String{
        val curdate = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return format.format(curdate)
    }

    /**
     *  upload the user object to cloud firestore to synchrinise.
     */
    private fun updateUser(){
        if(userstore!=null) {
            db.collection("users")
                    .document(user!!.email!!).set(userstore!!)
                    .addOnSuccessListener {
                        Log.d(tag, "DocumentSnapshot added with ID: " + user!!.email!!);
                    }
                    .addOnFailureListener(this) {
                        Log.w(tag, "Error adding document", it);
                    }
        }
    }
}
