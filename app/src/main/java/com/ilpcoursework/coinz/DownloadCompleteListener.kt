package com.ilpcoursework.coinz
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point
import com.google.gson.JsonObject
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions

interface DownloadCompleteListener {
    fun downloadComplete(result: String)
}
//object DownloadCompleteRunner : DownloadCompleteListener {
//    lateinit var result: String
//    private var marker:Marker?=null;
//
//    override fun downloadComplete(result: String) {
//        this.result = result
//        rendercoins(this.result)
//    }
//
//    private fun rendercoins(result: String) {
//        if (result == null) {
//
//        } else {
//            var fc = FeatureCollection.fromJson(result)
//            var featureList = fc.features()
//
//
//            for (f in featureList.orEmpty()) {
//                var g = f.geometry()
//                var p = f.properties()
//                var point: Point = g as Point // mapping from ? to point might be unsafe?
//                marker = map.addMarker()
//            }
//        }
//    }
//}