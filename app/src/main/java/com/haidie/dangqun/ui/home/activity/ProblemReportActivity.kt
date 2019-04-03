package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.androidinterface.AndroidBackAndReleaseInterface
import com.haidie.dangqun.ui.main.activity.MainActivity
import com.haidie.dangqun.utils.LogHelper
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_web_view.*
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/10/24 10:09
 * description  问题上报
 */
class ProblemReportActivity : BaseActivity() {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var addressStr: String? = null
    private var mWebView: WebView? = null
    private var mUploadCallBackAbove: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val takePhotoResultCode = 1
    private val fileChooserResultCode = 2
    private var mLocationClient: LocationClient? = null
    override fun getLayoutId(): Int = R.layout.activity_web_view
    private var mOption: LocationClientOption? = null
    override fun initData() {
        mOption = LocationClientOption()
        mOption?.apply {
//            locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            locationMode = LocationClientOption.LocationMode.Battery_Saving  //网络定位
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
                addressStr = location.addrStr.toString()
                val sb = StringBuffer(256)
                sb.append("time : ")
                /**
                 * 时间也可以使用systemClock.elapsedRealTime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
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
                LogHelper.d("=======\n$sb")
                mLocationClient?.stop()
                mLayoutStatusView?.showContent()
                var mSettings : WebSettings? = null
                mWebView?.let {
                    it.webChromeClient = mWebChromeClient
                    mSettings = it.settings
                }
                syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORT,"${Constants.UID}=$uid")
                syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORT,"${Constants.TOKEN}=$token")
                syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORT,"${Constants.ADDRESS_STR}=${encode(addressStr!!)}")

                webView.loadUrl(UrlConstant.BASE_URL_PROBLEM_REPORT)
                webView.addJavascriptInterface(AndroidBackAndReleaseInterface(this@ProblemReportActivity), Constants.ANDROID)
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

    private var mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        //For Android 5.0
        override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
            mUploadCallBackAbove = filePathCallback
            take()
            return true
        }
        //       android 4.0 - android 4.3  安卓4.4.4也用的这个方法
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            mUploadMessage = uploadMsg
            take()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileChooserResultCode) {
            val result = if (data == null || resultCode != RESULT_OK) null else data.data
            when {
                mUploadCallBackAbove != null -> onActivityResultAbove(requestCode, resultCode, data)
                mUploadMessage != null -> {
                    mUploadMessage!!.onReceiveValue(result)
                    mUploadMessage = null
                }
            }
        }
    }
    private fun onActivityResultAbove(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != fileChooserResultCode || mUploadCallBackAbove == null) {
            return
        }
        var results: Array<Uri>? = null
        if (resultCode == RESULT_OK) {
            if (data != null) {
                val dataString = data.dataString
                val clipData = data.clipData
                if (clipData != null) {
                    results = arrayOf()
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mUploadCallBackAbove!!.let {
            it.onReceiveValue(results)
            null
        }
    }
    private fun take() {
        val albumIntent = Intent(Intent.ACTION_PICK, null)
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants.IMAGE)
        startActivityForResult(albumIntent, fileChooserResultCode)
        // 指定拍照存储位置的方式调起相机
//        val filePath = "${Environment.getExternalStorageDirectory()}${File.separator}${Environment.DIRECTORY_PICTURES}${File.separator}"
//        val fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg"
//        val imageUri = Uri.fromFile( File(filePath + fileName))
//        val captureIntent =  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//        val photo =  Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        val chooserIntent = Intent.createChooser(photo, "拍照或选择图片")
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))
//        startActivityForResult(chooserIntent, fileChooserResultCode)
//        openPick()
//        openCapture()
    }
    /**
     * 打开相册
     */
    private fun openPick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "image/*"
            startActivityForResult(Intent.createChooser(i, "选择图片"), fileChooserResultCode)
        } else {
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, fileChooserResultCode)
        }
    }
    private val s: String
        get() {
            return "${Environment.getExternalStorageDirectory().path}/h5/${System.currentTimeMillis()}.jpg"
        }
    private var cameraUri: Uri? = null
    /**
     * 打开照相机拍照
     */
    private fun openCapture() {
        val takeIntent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imagePaths = s
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        val vFile =  File(imagePaths)
        if (!vFile.exists()) {
            vFile.parentFile.mkdirs()
        } else {
            if (vFile.exists()) {
                vFile.delete()
            }
        }
        cameraUri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
                FileProvider.getUriForFile(this, "$packageName.provider",vFile)//通过FileProvider创建一个content类型的Uri
            else -> Uri.fromFile(vFile)
        }
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        startActivityForResult(takeIntent, takePhotoResultCode)
    }
    override fun start() {}
    fun handleJSEvent(code : Int){
        when (code) {
        // code = 1 该页面取消，返回上一级页面
            1 -> finish()
        //  code = 2 发布失败，停留在本页面
            2 -> {
                showShort("发布失败")
            }
        }
    }
    fun releaseSuccess(){
        showShort("发布成功")
        val intent = Intent(this@ProblemReportActivity, MainActivity::class.java)
        intent.putExtra(Constants.TAB,3)
        intent.putExtra(Constants.IS_REPORT_OR_HELP,1)
        startActivity(intent)
    }
}