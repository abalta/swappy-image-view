package com.abdullahbalta.swappyimageview

import android.app.Application
import com.evernote.android.state.StateSaver



class SwappyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true)
    }
}