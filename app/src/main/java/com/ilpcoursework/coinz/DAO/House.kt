package com.ilpcoursework.coinz.DAO

import com.google.type.LatLng

class House {
    var name :String=""
    var currency:String=""
    var price : Double=0.0
    var profit: Double=0.0
    var latlng:LatLng= LatLng.getDefaultInstance()
    constructor() {}
    constructor(name:String,currency: String,price:Double,profit:Double,latlng:LatLng) {
        this.name=name
        this.currency=currency
        this.price=price
        this.profit=profit
        this.latlng=latlng

    }
}