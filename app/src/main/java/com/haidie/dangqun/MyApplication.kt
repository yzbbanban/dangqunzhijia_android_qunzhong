package com.haidie.dangqun

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import com.haidie.dangqun.utils.DisplayManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlin.properties.Delegates

/**
 * Created by admin2
 *  on 2018/08/09  10:45
 * description
 */
class MyApplication : Application() {
    companion object {
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                //设置下拉刷新主题颜色
                layout.setPrimaryColorsId(R.color.background_color)
                MaterialHeader(context).setShowBezierWave(true)
            }
        }

        private const val TAG = "MyApplication"
        var mId = 0

        var context: Context by Delegates.notNull()
            private set
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initConfig()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }


    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(Constants.OK_HTTP)
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG  //如果是Debug  模式 ；打印日志
            }
        })
    }

    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Logger.d(TAG, "onCreated:" + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Logger.d(TAG, "onStart:" + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity?) {}
        override fun onActivityPaused(activity: Activity?) {}
        override fun onActivityStopped(activity: Activity?) {}
        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
        override fun onActivityDestroyed(activity: Activity) {
            Logger.d(TAG, "onDestroyed:" + activity.componentName.className)
        }
    }
}