package com.ilpcoursework.coinz

import java.io.Serializable

class Coin :Serializable{
    var id = ""
    var value = 0.0
    var currency=""
    var date =""
    public fun getcur(): String{
        return this.currency
    }
    public fun tostring(): String {
        return "value: "+value.toString()+ "collected at: "+date
    }
    constructor() {}
    constructor(id: String,value: Double,currency: String,date:String) {
        this.id=id
        this.value=value
        this.currency=currency
        this.date=date
    }
}