package com.ilpcoursework.coinz.DAO

import com.google.type.LatLng

class House {
    var name :String=""
    var price : Double=0.0
    var profit: Double=0.0
    var latlng:LatLng= LatLng.getDefaultInstance()
    constructor() {}
    constructor(name:String,price:Double,profit:Double,latlng:LatLng) {
        this.name=name
        this.price=price
        this.profit=profit
        this.latlng=latlng

    }
    override fun toString():String{
        return "house "+name+" costs "+price.toString()
    }

}