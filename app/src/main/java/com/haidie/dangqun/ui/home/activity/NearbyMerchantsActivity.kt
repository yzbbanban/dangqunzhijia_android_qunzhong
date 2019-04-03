package com.haidie.dangqun.ui.home.activity

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.androidinterface.AndroidNearbyMerchantsInterface
import com.haidie.dangqun.utils.LogHelper
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.aop.CheckOnClick
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Create by   Administrator
 *      on     2018/10/12 15:59
 * description  附近商家
 */
class NearbyMerchantsActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var latitude: String? = null
    private var longitude: String? = null
    private var addressStr: String? = null
    private var mWebView: WebView? = null
    private var mOption: LocationClientOption? = null
    private var mLocationClient: LocationClient? = null
    override fun getLayoutId(): Int = R.layout.activity_web_view
    override fun onDestroy() {
        super.onDestroy()
        //释放资源
        mWebView?.also {
            it.destroy()
            mWebView = null
        }
    }
    override fun initData() {
        mOption = LocationClientOption()
        mOption?.apply {
            locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            setCoorType("bd09ll")
            setScanSpan(0)
            setIsNeedAddress(true)
            setNeedDeviceDirect(false)
            setIsNeedLocationDescribe(true)
            setIsNeedLocationPoiList(true)
            isOpenGps = true
        }
        mLocationClient = LocationClient(applicationContext)
        mLocationClient?.apply {
            registerLocationListener(mListener)
            locOption = mOption
            start()
        }
    }
    override fun onStop() {
        mLocationClient?.let {
            it.unRegisterLocationListener(mListener)
            it.stop()
        }
        super.onStop()
    }
    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private val mListener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            if (null != location && location.locType != BDLocation.TypeServerError) {
                if (location.locType != BDLocation.TypeGpsLocation && location.locType != BDLocation.TypeOffLineLocation &&
                        location.locType != BDLocation.TypeNetWorkLocation){
                    mLocationClient?.restart()
                    return
                }
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

                sb.append("\nprovince : ")// 获取省份
                sb.append(location.province)

                sb.append("\ncity : ")// 城市
                sb.append(location.city)
                sb.append("\naddressStr : ")
                sb.append(location.addrStr)

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
                LogHelper.d("=======$sb")
                mLocationClient?.stop()
                mLayoutStatusView?.showContent()
                var mSettings : WebSettings? = null
                mWebView?.let {
                    it.webViewClient = mWebViewClient
                    mSettings = it.settings
                }
                syncCookie(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX,"${Constants.UID}=$uid")
                syncCookie(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX,"${Constants.TOKEN}=$token")
                syncCookie(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX,"province=${encode(location.province)}")
                syncCookie(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX,"city=${encode(location.city)}")
                syncCookie(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX,"latitude=${location.latitude}")
                syncCookie(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX,"longitude=${location.longitude}")

                webView.loadUrl(UrlConstant.BASE_URL_NEARBY_MERCHANTS_INDEX)
                webView.addJavascriptInterface(AndroidNearbyMerchantsInterface(this@NearbyMerchantsActivity), Constants.ANDROID)
                initWebViewSettings(mSettings!!)
            }else{
                mLocationClient?.restart()
            }
        }
    }

    override fun initView() {
        mLayoutStatusView = multipleStatusView
        mWebView = webView
        mLayoutStatusView?.showLoading()
    }
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mLayoutStatusView?.showContent()
        }
    }
    override fun start() {}

    @CheckOnClick
    fun toNearbyMerchantsList(url: String){
//        跳转到附近商家列表页面
        NearbyMerchantsListActivity.start(this,url)
    }
    @CheckOnClick
    fun toNearbyMerchantsDetail(url: String){
//        跳转到附近商家详情页面
        NearbyMerchantsDetailActivity.start(this,url,latitude!!,longitude!!,addressStr!!)
    }
}