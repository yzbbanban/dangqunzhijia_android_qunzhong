package com.haidie.dangqun.ui.main.activity

import android.content.Intent
import android.widget.TextView
import com.allenliu.versionchecklib.callback.OnCancelListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.main.SplashContract
import com.haidie.dangqun.mvp.model.bean.CheckVersionData
import com.haidie.dangqun.mvp.presenter.main.SplashPresenter
import com.haidie.dangqun.ui.main.view.BaseDialog
import com.haidie.dangqun.utils.AppUtils
import com.haidie.dangqun.utils.CompareVersion
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * Create by   Administrator
 *      on     2018/08/29 10:03
 * description
 */
class SplashActivity : BaseActivity(), SplashContract.View {

    private val mPresenter by lazy { SplashPresenter() }
    private var loginStatus: Boolean by Preference(Constants.LOGIN_STATUS, false)
    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun initData() {
//        //避免再次进入时重新启动
        if (!isTaskRoot) {
            finish()
            return
        }
    }
    override fun initView() {
        mPresenter.attachView(this)
        StatusBarUtil.immersive(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getCheckVersionData()  }
    private var builder: DownloadBuilder? = null
    override fun setCheckVersionData(checkVersionData: CheckVersionData) {
//        当前版本
        val versionCode = AppUtils.getVersionName(this)
//        线上版本
        val version = checkVersionData.version
        val compareVersion = CompareVersion.compareVersion(versionCode, version)
//        1.0.5
        if (compareVersion < 0){
            builder = AllenVersionChecker
                    .getInstance()
                    .downloadOnly(UIData.create()
                            .setDownloadUrl(UrlConstant.BASE_URL_HOST + checkVersionData.download)
                            .setContent(checkVersionData.desc))
            builder?.apply {
                notificationBuilder = createCustomNotification()
                customVersionDialogListener = customVersionDialog()
                downloadAPKPath = Constants.PATH_APK
                onCancelListener = OnCancelListener { goToMain() }
                executeMission(this@SplashActivity)
            }
        }else{
            goToMain()
        }
    }
    private fun createCustomNotification(): NotificationBuilder {
        return NotificationBuilder.create()
                .setRingtone(true)
                .setIcon(R.mipmap.ic_app)
                .setContentTitle(AppUtils.getAppName(this))
                .setContentText(getString(R.string.custom_content_text))
    }
    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     */
    private fun customVersionDialog(): CustomVersionDialogListener {
        return CustomVersionDialogListener { context, versionBundle ->
            val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.dialog_check_version)
            val textView = baseDialog.findViewById<TextView>(R.id.tv_msg)
            textView.text = versionBundle!!.content
            baseDialog
        }
    }

    override fun showError(msg: String, errorCode: Int) {  goToMain()  }
    private fun goToMain() {
        ivSplash.postDelayed({
            val clazz: Class<*> = if (loginStatus) {
                MainActivity::class.java
            } else  LoginActivity::class.java
            startActivity(Intent(this, clazz))
            finish()
        },1000)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}