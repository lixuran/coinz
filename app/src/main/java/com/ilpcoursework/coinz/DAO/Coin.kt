package com.ilpcoursework.coinz.DAO

import java.io.Serializable

class Coin :Serializable{
    var id = ""
    var value = 0.0
    var currency=""
    var date =""
    var type :Int = 0 // 0 stand for non gift coin and one stand for coins received as a gift

    fun tostring(): String {
        return "value: "+value.toString()+ "collected at: "+date +"more info>>"
    }
    constructor() {}
    constructor(id: String,value: Double,currency: String,date:String) {
        this.id=id
        this.value=value
        this.currency=currency
        this.date=date
        this.type=0
    }
}