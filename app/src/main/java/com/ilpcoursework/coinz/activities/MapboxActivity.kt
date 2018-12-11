package com.ilpcoursework.coinz.activities
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.*
import com.ilpcoursework.coinz.DAO.Coin
import com.ilpcoursework.coinz.DAO.User
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
import kotlinx.android.synthetic.main.activity_mapbox.*
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class MapboxActivity : AppCompatActivity(), OnMapReadyCallback,PermissionsListener,LocationEngineListener,MapboxMap.OnMapClickListener, DownloadCompleteListener {

//, NavigationView.OnNavigationItemSelectedListener

    private lateinit var  fc:FeatureCollection
    private var featureList: List<Feature>? = null

    private val tag = "MapboxActivity"
    private var mapView: MapView? = null
    private var map: MapboxMap? = null

    private lateinit var originLocation: Location
    private lateinit var permissionsManager: PermissionsManager
    private var locationEngine: LocationEngine? =null
    private var locationLayerPlugin: LocationLayerPlugin? = null

    private lateinit var downloader: DownloadFileTask
    val dbHelper = FeedReaderDbHelper(this)

    private val preferencesFile = "MyPrefsFile" // for storing preferences
    private var lastdate: String =""


    private var db = FirebaseFirestore.getInstance();
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser?=null
    private var userstore: User?=null

    private var coinsindex = hashMapOf("SHIL" to 1,"DOLR" to 2,"QUID" to 3,"PENY" to 4 )
    private var coinsmapping = hashMapOf("SHIL" to R.drawable.bluedragon,"DOLR" to R.drawable.greendragon,"QUID" to R.drawable.yellowdragon,"PENY" to R.drawable.reddragon)
    private var icons:IntArray = intArrayOf(R.drawable.bluedragon, R.drawable.greendragon, R.drawable.yellowdragon, R.drawable.reddragon)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapbox)
      //  setSupportActionBar(toolbar)

        Mapbox.getInstance(this, getString(R.string.access_token_public))
        mapView = findViewById(R.id.mapboxMapView)
        mapView?.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth?.getCurrentUser();

        userstore = getIntent().extras["useridentity"] as? User
        mapView?.getMapAsync(this)


    }


    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        try {
            locationEngine?.requestLocationUpdates()
        } catch (ignored: SecurityException) {
            //this try catch is from piazza might need redo
        locationEngine?.addLocationEngineListener(this)
        }

        signout_button.setOnClickListener { view ->

            FirebaseAuth.getInstance().signOut();
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        mywallet_button.setOnClickListener { view ->
            val intent = Intent(this, WalletSelectActivity::class.java)
            intent.putExtra("useridentity", userstore)
            startActivity(intent)
        }
        myfriend_button.setOnClickListener { view ->
            val intent = Intent(this, MyFriendActivity::class.java)
            intent.putExtra("useridentity", userstore)
            startActivity(intent)
        }
        myprofile_button.setOnClickListener { view ->
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("useridentity", userstore)
            startActivity(intent)
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
    private fun updateUser(){
        if(userstore!=null) {
            db.collection("users")
                    .document(user!!.email!!).set(userstore!!)
                    .addOnSuccessListener({
                        Log.d(tag, "DocumentSnapshot added with ID: " + user!!.email!!);
                    })
                    .addOnFailureListener(this) {
                        Log.w(tag, "Error adding document", it);
                    }
        }
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

    //from slides
    override fun onMapReady(mapboxMap: MapboxMap?) {
        if (mapboxMap == null) {
            Log.d(tag, "[onMapReady] mapboxMap is null")
        } else {
            map = mapboxMap
            // Set user interface options
            map?.uiSettings?.isCompassEnabled = true
            map?.uiSettings?.isZoomControlsEnabled = true
            // Make location information available


            val curdatestring =getcurdate()
            val url = "http://homepages.inf.ed.ac.uk/stg/coinz/" +curdatestring + "/coinzmap.geojson"

            if(userstore?.lastdate!=curdatestring || userstore?.lastdate==null) {
                userstore?.lastdate = curdatestring
                userstore?.gold =userstore!!.gold* updategold(userstore!!.lastdate!!)
                userstore?.update_settings()
                updateUser()
                downloader = DownloadFileTask(this)
                downloader.execute(url)
            }

            enableLocation()


        }
    }
    override fun downloadComplete(result: String) {


        save_rate_preferrences(result)
        rendercoins(null)
    }
    private fun updategold(lastdate:String):Double{
        val lastdates = lastdate.split("/").map { item->item.toInt() }


        val date1= LocalDate.of(lastdates[0],lastdates[1],lastdates[2])
        val date2 = LocalDate.now()
        val days = date1.until(date2, ChronoUnit.DAYS)
        return 1.001.pow(days.toInt())
    }
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

    @SuppressWarnings("MissingPermission")
    private fun initialiseLocationLayer() {
//       Lifecycle lifecycle = getLifecycle();
//       lifecycle.addObserver(locationLayerPlugin!!);
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


    //for permission
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
            //TODO("not implemented")//open a dialog with user
        }
    }
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Log.d(tag, "Permissions: $permissionsToExplain")
    }
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
                val distance =LatLng(originLocation.latitude,originLocation.longitude) .distanceTo(LatLng(point.latitude(),point.longitude()))
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
            rendercoins(location)
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
    override fun onMapClick(point: LatLng) {
        TODO("not implemented")
        //click on coins show coin info (perferably as a panel(or sidepanel )) this is implemented but can be improved
        //click on house show house info (as another activity)
        //click on wallet show personal info?? is this included?

    }


    private fun rendercoins(location: Location?) {
        var index = 0
        fc = FeatureCollection.fromJson(userstore!!.result!!)
        featureList = fc.features()

        for (f in featureList.orEmpty()) {
            if(userstore!!.collectedcoins[index]==1) {
                index += 1
                continue
            }
            index += 1
                val g = f.geometry()
                val p = f.properties()?.asJsonObject
                val currency:String = p?.get("currency").toString().substring(1,5)
                val point: Point = g as Point // mapping from ? to point might be unsafe?
                var distance :Double=0.0
                if(location!= null ){  distance =LatLng(location!!.latitude,location.longitude) .distanceTo(LatLng(point.latitude(),point.longitude()))}
                else {
                    distance= 0.0
                }
                if(distance <250){
                val iconFactory = IconFactory.getInstance(this);
                val icon = iconFactory.fromResource(coinsmapping.get(currency)!!)


                // Add the custom icon marker to the map
                val marker=map?.addMarker(MarkerOptions()
                        .position(LatLng(point.latitude(), point.longitude()))
                        .title(p?.get("marker-symbol").toString())
                        .snippet(p?.get("currency").toString().substring(1,5)+": "+p?.get("value").toString().substring(1,4))
                        .icon(icon)
                        )
                //.icon(icon));
                }
            }
        }
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

}
