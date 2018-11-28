package com.ilpcoursework.coinz

import android.arch.lifecycle.Lifecycle
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.PointerDistancePair
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class MapboxActivity : AppCompatActivity(), OnMapReadyCallback,PermissionsListener,LocationEngineListener,MapboxMap.OnMapClickListener,DownloadCompleteListener{



    private lateinit var  fc:FeatureCollection
    private var featureList: List<Feature>? = null

    private val tag = "MapboxActivity"
    private var mapView: MapView? = null
    private var map: MapboxMap? = null

    private lateinit var originLocation: Location
    private lateinit var permissionsManager: PermissionsManager
    private var locationEngine: LocationEngine? =null
    private var locationLayerPlugin: LocationLayerPlugin? = null
    private val mywallet = Mywallet()

    private lateinit var downloader: DownloadFileTask
    private lateinit var curdate: LocalDateTime
    private var lastdate: LocalDateTime? =null
    private var coinrenderflag: Boolean? = null
    private var markerlist:MutableList<Marker> = mutableListOf();
    private var coinsindex = hashMapOf("SHIL" to 1,"DOLR" to 2,"QUID" to 3,"PENY" to 4 )
    private var coinsmapping = hashMapOf("SHIL" to R.drawable.blue_coin,"DOLR" to R.drawable.green_coin,"QUID" to R.drawable.yellow_coin,"PENY" to R.drawable.red_coin )
    private var icons:IntArray = intArrayOf(R.drawable.blue_coin, R.drawable.green_coin, R.drawable.yellow_coin, R.drawable.red_coin)
    private lateinit var downloadresult:String
    //create a wallet class here to store all coins  -- todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapbox)

        Mapbox.getInstance(this, getString(R.string.access_token_public))
        mapView = findViewById(R.id.mapboxMapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        //mapView?.getMapAsync{mapboxMap ->map = mapboxMap  }
    }
    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            //locationEngine?.requestLocationUpdates()
            try {
                locationEngine?.requestLocationUpdates()
            } catch (ignored: SecurityException) {
            //this try catch is from piazza might need redo

                locationEngine?.addLocationEngineListener(this)
            }
            locationLayerPlugin?.onStart()
        }

        mapView?.onStart()
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
        locationLayerPlugin?.onStop()
        mapView?.onStop()
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
            enableLocation()

            curdate = LocalDateTime.now()
            if(lastdate!=curdate || lastdate==null) {
                lastdate = curdate
                val format = SimpleDateFormat("yyyy/MM/dd")
                val url = "http://homepages.inf.ed.ac.uk/stg/coinz/" + curdate.year + "/" + curdate.monthValue + "/" + curdate.dayOfMonth + "/coinzmap.geojson"
                downloader = DownloadFileTask(this)
                downloader.execute(url)
            }

        }
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
       // Lifecycle lifecycle = getLifecycle();
       // lifecycle.addObserver(locationLayerPlugin!!);
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
            TODO("not implemented")//open a dialog with user
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
            var counter =0
            TODO("loop over all points and remove those that haven't been removed if close enough, add the coins to the wallet class with the date ")
            for (f in featureList.orEmpty()) {
                var g = f.geometry()
                var point = g as Point
                counter++
                if(distance(originLocation,point)<25){
                    map?.removeMarker(markerlist[counter])

                    mywallet.addtoWallet(f)

                }

            }
        }
    }
    private fun distance(location: Location?,p: Point):Double{
        val earthRadius = 6371000
        val dLat =Math.toRadians(location!!.latitude - p.latitude())
        val dLng  =Math.toRadians(location!!.longitude - p.longitude())
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(location!!.latitude)) * Math.cos(Math.toRadians(p.latitude())) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val dist = (earthRadius * c) as Double
        return dist
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

    override fun downloadComplete(result: String) {
        downloadresult = result

        fc = FeatureCollection.fromJson(downloadresult)
        featureList = fc.features()
        rendercoins(downloadresult)
    }

    private fun rendercoins(result: String?) {
        if (result == null) {

        } else {

            for (f in featureList.orEmpty()) {
                val g = f.geometry()
                val p = f.properties()?.asJsonObject
                val currency:String = p?.get("currency").toString()
                val point: Point = g as Point // mapping from ? to point might be unsafe?
                val iconFactory = IconFactory.getInstance(this);
                val icon = iconFactory.fromResource(R.drawable.blue_coin)


                // Add the custom icon marker to the map
                val marker=map?.addMarker(MarkerOptions()
                        .position(LatLng(point.latitude(), point.longitude()))
                        .title(p?.get("marker-symbol").toString())
                        .snippet(p?.get("currency").toString()+": "+p?.get("value").toString().substring(0,2))
                        // .icon(icon)
                        )
                //.icon(icon));
                markerlist.add(marker!!)
            }
        }
    }
}
