package com.fidato.headytestapp

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import java.lang.ref.WeakReference

class HeadyTestApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        wApp!!.clear()
        wApp = WeakReference(this@HeadyTestApp)

    }

    companion object {
        private var wApp: WeakReference<HeadyTestApp>? = WeakReference<HeadyTestApp>(null)!!
        val instance: HeadyTestApp get() = wApp?.get()!!

        val context: Context
            get() {
                val app = if (null != wApp) wApp!!.get() else HeadyTestApp()
                return if (app != null) app.applicationContext else HeadyTestApp().applicationContext
            }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}