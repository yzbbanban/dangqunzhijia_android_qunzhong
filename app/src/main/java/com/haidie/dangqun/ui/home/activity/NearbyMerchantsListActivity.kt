package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.webkit.WebView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.androidinterface.AndroidNearbyMerchantsListInterface
import com.haidie.dangqun.utils.LogHelper
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.aop.CheckOnClick
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Create by   Administrator
 *      on     2018/10/16 08:40
 * description  附近商家列表
 */
class NearbyMerchantsListActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mWebView: WebView? = null
    private var url: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var addressStr: String? = null
    private var mLocationClient: LocationClient? = null
    companion object {
        fun start(context: FragmentActivity, url: String){
            val intent = Intent(context, NearbyMerchantsListActivity::class.java)
            intent.putExtra(Constants.URL_KEY, url)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_web_view
    override fun onDestroy() {
        super.onDestroy()
        //释放资源
        mWebView?.let {
            it.destroy()
            null
        }
    }

    private var mOption: LocationClientOption? = null
    override fun initData() {
        mOption = LocationClientOption()
        mOption?.let {
            it.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            it.setCoorType("bd09ll")
            it.setScanSpan(0)
            it.setIsNeedAddress(true)
            it.setNeedDeviceDirect(false)
            it.setIsNeedLocationDescribe(true)
            it.setIsNeedLocationPoiList(true)
            it.isOpenGps = true
        }

        url = intent.getStringExtra(Constants.URL_KEY)
        mLocationClient = LocationClient(applicationContext)
        mLocationClient?.let {
            it.registerLocationListener(mListener)
            it.locOption = mOption
            it.start()
        }
    }
    override fun onStop() {
        mLocationClient?.let {
            it.unRegisterLocationListener(mListener)
            it.stop()
        }
        super.onStop()
    }
    private val mListener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            if (null != location && location.locType != BDLocation.TypeServerError) {
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()
                addressStr = location.addrStr.toString()
                val sb = StringBuffer(256)
                sb.append("time : ")
                sb.append(location.time)
                sb.append("\nlocType : ")// 定位类型
                sb.append(location.locType)
                sb.append("\nlocType description : ")// *****对应的定位类型说明*****
                sb.append(location.locTypeDescription)
                sb.append("\nlatitude : ")// 纬度
                sb.append(location.latitude)
                sb.append("\nlongitude : ")// 经度
                sb.append(location.longitude)

                when {
                    location.locType == BDLocation.TypeGpsLocation -> {// GPS定位结果
                        sb.append("\nspeed : ")
                        sb.append(location.speed)// 速度 单位：km/h
                        sb.append("\nsatellite : ")
                        sb.append(location.satelliteNumber)// 卫星数目
                        sb.append("\nheight : ")
                        sb.append(location.altitude)// 海拔高度 单位：米
                        sb.append("\ngps status : ")
                        sb.append(location.gpsAccuracyStatus)// *****gps质量判断*****
                        sb.append("\ndescribe : ")
                        sb.append("gps定位成功")
                    }
                    location.locType == BDLocation.TypeNetWorkLocation -> {// 网络定位结果
                        // 运营商信息
                        if (location.hasAltitude()) {// *****如果有海拔高度*****
                            sb.append("\nheight : ")
                            sb.append(location.altitude)// 单位：米
                        }
                        sb.append("\noperators : ")// 运营商信息
                        sb.append(location.operators)
                        sb.append("\ndescribe : ")
                        sb.append("网络定位成功")
                    }

                    location.locType == BDLocation.TypeCriteriaException -> {
                        sb.append("\ndescribe : ")
                        sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                    }
                }
                LogHelper.d("=======\n$sb")
                mLocationClient?.stop()
                mLayoutStatusView?.showContent()
                val mSettings  = mWebView?.settings
                syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.UID}=$uid")
                syncCookie(UrlConstant.BASE_URL_HOST + url,"${Constants.TOKEN}=$token")
                syncCookie(UrlConstant.BASE_URL_HOST + url,"latitude=${location.latitude}")
                syncCookie(UrlConstant.BASE_URL_HOST + url,"longitude=${location.longitude}")

                webView.loadUrl(UrlConstant.BASE_URL_HOST + url)
                webView.addJavascriptInterface(AndroidNearbyMerchantsListInterface(this@NearbyMerchantsListActivity), Constants.ANDROID)
                initWebViewSettings(mSettings!!)
            }else{
                mLocationClient?.requestLocation()
            }
        }
    }

    override fun initView() {
        mLayoutStatusView = multipleStatusView
        mWebView = webView
        mLayoutStatusView?.showLoading()
    }

    override fun start() {}
    @CheckOnClick
    fun toNearbyMerchantsDetail(url: String){
//        跳转到附近商家详情页面
        NearbyMerchantsDetailActivity.start(this,url,latitude!!,longitude!!,addressStr!!)
    }
}