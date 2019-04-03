package com.haidie.dangqun.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zxing.ZXingView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.utils.LogHelper
import kotlinx.android.synthetic.main.activity_scan.*

/**
 * Create by   Administrator
 *      on     2018/11/01 13:55
 * description  二维码扫描页面
 */
class ScanActivity : BaseActivity(), QRCodeView.Delegate {

    private lateinit var mZXingView: ZXingView
    override fun getLayoutId(): Int = R.layout.activity_scan

    override fun initData() {}

    override fun initView() {
        mZXingView = zxingview
        mZXingView.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        mZXingView.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        // mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect() // 显示扫描框，并且延迟0.1秒后开始识别
    }

    override fun onStop() {
        mZXingView.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        mZXingView.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }
    override fun start() {}

    override fun onScanQRCodeSuccess(result: String?) {
        if (!result?.contains("www.dqzj.com")!! && !result.contains(UrlConstant.BASE_URL_HOST.split("//")[1])){
            showShort("二维码错误")
            return
        }
        val split = result.split("?")
        val ids = split[1].split("=".toRegex())
        val intent = Intent(this@ScanActivity, MyVolunteerActivitiesDetailActivity::class.java)
        intent.putExtra(Constants.ID, ids[1].toInt())
        intent.putExtra(Constants.IS_SIGN_UP,false)
        intent.putExtra(Constants.IS_SIGN_IN_OR_OUT,true)
        startActivity(intent)
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        finish()
        vibrate()

        mZXingView.startSpot() // 延迟0.1秒后开始识别
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText = mZXingView.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                mZXingView.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        showShort("打开相机出错")
        LogHelper.d("=================\n打开相机出错")
    }
}