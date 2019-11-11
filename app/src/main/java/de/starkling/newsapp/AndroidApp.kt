package de.starkling.newsapp

import android.app.Application
import de.starkling.newsapp.injections.components.DaggerApplicationComponent

/**
 * Created by Zohaib Akram on 2019-11-11
 * Copyright © 2019 Starkling. All rights reserved.
 */
class AndroidApp : Application() {
    val appComponent = DaggerApplicationComponent.create()
}