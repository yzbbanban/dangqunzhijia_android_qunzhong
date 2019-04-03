package com.haidie.dangqun.ui.home.activity

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.LinearLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.FaultRepairContract
import com.haidie.dangqun.mvp.presenter.home.FaultRepairPresenter
import com.haidie.dangqun.ui.home.androidinterface.AndroidFaultRepairInterface
import com.haidie.dangqun.ui.main.view.RuntimeRationale
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.aop.CheckOnClick
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_frame_layout.*

/**
 * Create by   Administrator
 *      on     2018/09/21 16:53
 * description  障碍报修
 */
class FaultRepairActivity : BaseActivity(),FaultRepairContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var phone by Preference(Constants.ACCOUNT, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAgentWeb: AgentWeb? = null
    private val mPresenter by lazy { FaultRepairPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_frame_layout
    override fun onPause() {
        mAgentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }
    override fun onResume() {
        mAgentWeb!!.webLifeCycle.onResume()
        super.onResume()
    }
    override fun onDestroy() {
        mAgentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_FAULT_REPAIR,"${Constants.UID}=$uid")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_FAULT_REPAIR,"${Constants.TOKEN}=$token")
        AgentWebConfig.syncCookie(UrlConstant.BASE_URL_FAULT_REPAIR,"${Constants.PHONE}=$phone")
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(frame_layout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this,R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, Constants.NEGATIVE_ONE)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_FAULT_REPAIR)
        //注入对象
        mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidFaultRepairInterface(this))
    }
    override fun start() {}
    fun releaseWorkOrder(url: String){
        ReleaseWorkOrderActivity.startActivity(this, url)
    }
    @CheckOnClick
    fun toEditReport(url: String){
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                .rationale(RuntimeRationale())
                .onGranted { EditReportActivity.startActivity(this, url) }
                .onDenied { permissions ->  showSettingDialog(this, permissions)  }
                .start()
//        EditReportActivity.startActivity(this, url)
    }
    private fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting) { _, _ -> setPermission() }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
    }
    private fun setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .start()
    }
    override fun showRefreshEvent() {
        mAgentWeb!!.jsAccessEntrace.quickCallJs(Constants.RELOAD)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}