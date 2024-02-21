package eu.tutorials.myshoppinglistapp

import android.app.Application

class ShoppListApp : Application(){
    override fun onCreate(){
        super.onCreate()
        Graph.provide(this)
    }
}