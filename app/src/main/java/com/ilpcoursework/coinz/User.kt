package com.ilpcoursework.coinz

import java.io.Serializable


class User :Serializable{
    var coins= mutableListOf<Coin> ()
    var username: String=""
    var email: String=""
    var gold:Double=0.0
    var myshils:Double=0.0
    var mydolrs:Double=0.0
    var myquids:Double=0.0
    var mypenys:Double=0.0
    var collectedtoday:Int=0
    var lastdate:String?=null
    var result:String?=null
    var shilrate:Double=0.0
    var dolrrate:Double=0.0
    var quidrate:Double=0.0
    var penyrate:Double=0.0
    var bankedtoday:Int =0
    var collectedcoins = mutableListOf(0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0)

    public fun update_settings(){
        collectedtoday=0
        collectedcoins=mutableListOf(0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0,0, 0, 0,0,0)
        result =null
        bankedtoday = 0
    }

//    fun getname(): String {
//        return username
//    }
//    fun getresult(): String {
//        if(result !=null) {
//            return result!!
//        }
//        else
//            return ""
//    }
//    fun getdate(): String {
//        if(lastdate !=null) {
//            return lastdate!!
//        }
//        else
//            return ""    }
//    fun getshilrate(): Double {
//        return shilrate
//    }
//    fun getdolrrate(): Double {
//        return dolrrate
//    }
//    fun getpenyrate(): Double {
//        return penyrate
//    }
//    fun getquidrate(): Double {
//        return quidrate
//    }
//    fun getemail(): String {
//        return email
//    }
//    fun getgold(): Double {
//        return gold
//    }
//    fun getshils(): Double {
//        return myshils
//    }
//    fun getdolrs(): Double {
//        return mydolrs
//    }
//    fun getpenys(): Double {
//        return mypenys
//    }
//    fun getquids(): Double {
//        return myquids
//    }
//    fun getName(): String {
//        return username
//    }

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
        bankedtoday=0
    }
}

