package com.pudding.getapplist

import android.graphics.drawable.Drawable
import com.google.gson.annotations.Expose

/**
 * This created by Error on 2020/05/28,16:34.
 */
class AppInfo{
    @Expose
    var appName=""
    @Expose
    var packageName=""
    @Expose
    var versionName=""
    @Expose
    var appIconName=""
    var appIcon: Drawable?=null
}