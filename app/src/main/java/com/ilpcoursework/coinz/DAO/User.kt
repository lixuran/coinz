package com.ilpcoursework.coinz.DAO

import java.io.Serializable
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class User :Serializable{
    var coins= mutableListOf<Coin> ()
    var username: String=""
    var email: String=""
    var gold:Double=0.0
    var myshils:Double=0.0
    var mydolrs:Double=0.0
    var myquids:Double=0.0
    var mypenys:Double=0.0
    var collectedtoday:Int=0 // how many coins have been collected today
    var lastdate:String?=null
    var result:String?=null
    var shilrate:Double=0.0
    var dolrrate:Double=0.0
    var quidrate:Double=0.0
    var penyrate:Double=0.0
    var bankedToday:Int =0// how many coins have been send to bank today
    var collectedcoins = mutableListOf(0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0)// which coin has been collected
    var friends  = mutableListOf<friend>()
    var pendingfriends  = mutableListOf<friend>()
    var changestate:Int =0
    var selectedcoin :Int =0
    var giftToday :Int =0 // how many coins have been sent as gift today
    public fun update_settings(){
        collectedtoday=0
        collectedcoins=mutableListOf(0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0)
        result =null
        bankedToday = 0
        giftToday=0
        for (coin in coins){
            if(timebetween(coin.date)>=14){
                coins.remove(coin)
                when (coin.currency) {
                    "SHIL" -> { myshils =  myshils-coin.value }
                    "DOLR" -> {mydolrs =  mydolrs-coin.value }
                    "QUID" -> {myquids =  myquids-coin.value }
                    "PENY" ->{ mypenys =  mypenys-coin.value }

                }
            }
        }
    }
    private fun timebetween(date :String):Int{

        val lastdates = date.split("/").map { item->item.toInt() }
        val date1= LocalDate.of(lastdates[0],lastdates[1],lastdates[2])
        val date2 = LocalDate.now()
        val days = date1.until(date2, ChronoUnit.DAYS)
        return days.toInt()
    }

    constructor() {}
    constructor(username: String, email: String) {
        this.username=username
        this.email=email
        gold=0.0
        myshils=0.0
        mydolrs=0.0
        myquids=0.0
        mypenys=0.0
        collectedtoday=0
        bankedToday=0
        giftToday=0
    }
}

