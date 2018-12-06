package com.ilpcoursework.coinz.DAO

import java.io.Serializable

class friend:Serializable {
    var username :String=""
    var email: String=""
    constructor() {}
    constructor(username: String,email:String) {
        this.username=username
        this.email=email

    }
    public fun tostring():String{
        return "username: "+username+" ,email: "+email
    }

}