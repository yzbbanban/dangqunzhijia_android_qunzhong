package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.allenliu.versionchecklib.callback.OnCancelListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener
import com.allenliu.versionchecklib.v2.ui.VersionService.builder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.AboutUsContract
import com.haidie.dangqun.mvp.model.bean.CheckVersionData
import com.haidie.dangqun.mvp.presenter.mine.AboutUsPresenter
import com.haidie.dangqun.ui.main.view.BaseDialog
import com.haidie.dangqun.utils.AppUtils
import com.haidie.dangqun.utils.CompareVersion
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by admin2
 *  on 2018/08/18  11:29
 * description  关于我们
 */
class AboutUsActivity : BaseActivity(),AboutUsContract.View {

    private val mPresenter by lazy { AboutUsPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_about_us
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "关于党群之家"
        val versionName = AppUtils.getVersionName(this)
        tv_version_name.text = String.format(resources.getString(R.string.version_content_text),versionName)
        tvHost.text = "${UrlConstant.BASE_URL_HOST} 版权所有"
        tv_check_for_updates.setOnClickListener {
//            调用版本检查接口
            mPresenter.getCheckVersionData()
        }
        //跳转到投诉建议页面
        tv_complaint_suggestions.setOnClickListener { startActivity(Intent(this@AboutUsActivity,ComplaintSuggestionsActivity::class.java)) }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
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
                onCancelListener = OnCancelListener {}
                executeMission(this@AboutUsActivity)
            }
        }else{
            showShort("当前已是最新版本")
        }
    }
    private fun customVersionDialog(): CustomVersionDialogListener {
        return CustomVersionDialogListener { context, versionBundle ->
            val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.dialog_check_version)
            val textView = baseDialog.findViewById<TextView>(R.id.tv_msg)
            textView.text = versionBundle!!.content
            baseDialog
        }
    }
    private fun createCustomNotification(): NotificationBuilder {
        return NotificationBuilder.create()
                .setRingtone(true)
                .setIcon(R.mipmap.ic_app)
                .setContentTitle(AppUtils.getAppName(this))
                .setContentText(getString(R.string.custom_content_text))
    }

    override fun showError(msg: String, errorCode: Int) { showShort(msg) }
    override fun showLoading() {}
    override fun dismissLoading() {}
}