package com.haidie.dangqun.ui.mine.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VolunteerActivitiesDetailContract
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesDetailData
import com.haidie.dangqun.mvp.presenter.home.VolunteerActivitiesDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.VolunteersParticipateActivitiesActivity
import com.haidie.dangqun.ui.main.view.RuntimeRationale
import com.haidie.dangqun.utils.*
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_my_volunteer_activities_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*


/**
 * Create by   Administrator
 *      on     2018/10/22 16:15
 * description 我的志愿活动-详情
 */
class MyVolunteerActivitiesDetailActivity : BaseActivity(), VolunteerActivitiesDetailContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var isRefresh = false
    private var isSignUp = false
    private var isSignInOrOut = false
    private var mId: Int? = null
    private var activityType: Int? = null
    private val mPresenter by lazy { VolunteerActivitiesDetailPresenter() }
    private var volunteer_id: String? = null
    override fun getLayoutId(): Int = R.layout.activity_my_volunteer_activities_detail

    override fun initData() {

        isSignUp = intent.getBooleanExtra(Constants.IS_SIGN_UP, Constants.DEFAULT_FALSE)
        isSignInOrOut = intent.getBooleanExtra(Constants.IS_SIGN_IN_OR_OUT, Constants.DEFAULT_FALSE)
        volunteer_id = intent.getStringExtra(Constants.ACTIVITY_PERSONAL_UID)
        activityType = intent.getIntExtra(Constants.ACTIVITY_TYPE, 0)
        if (activityType == 0) {
            mId = intent.getIntExtra(Constants.ID, Constants.NEGATIVE_ONE)
            MyApplication.mId = this.mId!!
        }
        if (0 != MyApplication.mId) {
            mId = MyApplication.mId
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "活动详情"

        if (activityType != 0) {
            println("====>uid" + uid + "token" + token + " volunteer_id" + volunteer_id + " mId" + MyApplication.mId)

            mPresenter.getSignInOutData(uid, token, "" + volunteer_id, "" + MyApplication.mId)

        }

        initWebView()
        mLayoutStatusView = multipleStatusView
        llVolunteerActivities.setOnClickListener {
            // 跳转到志愿者参加活动人员列表页面
            val intent = Intent(this@MyVolunteerActivitiesDetailActivity, VolunteersParticipateActivitiesActivity::class.java)
            intent.putExtra(Constants.ID, mId)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        tvSignUp.setOnClickListener {
            if (tvSignUp.text == "我要报名") {
                AlertDialog.Builder(this)
                        .setTitle("是否确认报名")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认") { _, _ ->
                            // 调用报名接口
                            mPresenter.getAddActivityData(uid, mId!!, token)
                        }.create().show()
            }
        }

        btnActivitySignIn.setOnClickListener {

            //开启相机回调
            startCamera()
            finish()

        }

//        btnActivitySignOut.setOnClickListener {
//
//            //开启相机回调
//            startCamera()
//            finish()
//
//        }

        if (isSignInOrOut) {
            rlSignInOrOut.visibility = View.VISIBLE
        }
        tvSignIn.setOnClickListener {
            //调用志愿者活动签到
            mPresenter.getVolunteerActivitiesSignInData(uid, mId!!, token)
        }

    }

    private fun startCamera() {
        var intent = Intent(this@MyVolunteerActivitiesDetailActivity, ScanActivity::class.java)
        intent.putExtra(Constants.IS_SIGN_UP, isSignUp)
        intent.putExtra(Constants.IS_SIGN_IN_OR_OUT, isSignInOrOut)
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .rationale(RuntimeRationale())
                //跳转到二维码页面
                .onGranted { startActivity(intent) }
                .onDenied { permissions -> showSettingDialog(this@MyVolunteerActivitiesDetailActivity, permissions) }
                .start()
    }


    override fun setVolunteerActivitiesSignInData(isSuccess: Boolean, msg: String, errorCode: Int) {
        println("====>isSuccess " + isSuccess + " msg " + msg + " errorCode " + errorCode)
        if (isSuccess) {
            showShort(msg)
        } else {
//            if (code == 202){}
//            $retData['message']="您还没报名该活动,尚不能签到";
            if (errorCode == 202) {
                AlertDialog.Builder(this)
                        .setTitle("$msg\n是否确认报名")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认") { _, _ ->
                            // 调用报名接口
                            mPresenter.getAddActivityData(uid, mId!!, token)
                        }.create().show()
            } else {
                showShort(if (msg.isEmpty()) "签到失败" else msg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {
        mPresenter.getVolunteerActivitiesDetailData(uid, mId!!, token)
    }

    private fun initWebView() {
        val webSettings = webView.settings

        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        webSettings.textZoom = 250
        webSettings.setSupportZoom(false)  //支持缩放，默认为true
        //不显示缩放按钮
        webSettings.displayZoomControls = false

        //设置自适应屏幕，两者合用（下面这两个方法合用）
        webSettings.useWideViewPort = true        //将图片调整到适合WebView的大小
        webSettings.loadWithOverviewMode = true   //缩放至屏幕的大小
        //自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.webViewClient = WebViewClient()
    }

    @SuppressLint("SetTextI18n")
    override fun setVolunteerActivitiesDetailData(volunteerActivitiesDetailData: VolunteerActivitiesDetailData) {
        val isJoin = volunteerActivitiesDetailData.is_join
        flowLayout.removeAllViews()
        if (isSignUp) {
            tvSignUp.visibility = View.VISIBLE
            tvSignUp.text = if (isJoin == null) "我要报名" else "已报名"
        } else {
            tvSignUp.visibility = View.GONE
        }
        val pic = volunteerActivitiesDetailData.pic
        ImageLoader.load(this, pic, ivPic)
        val title = volunteerActivitiesDetailData.title
        tvTitle.text = title
        val url = Constants.HTML_BODY + volunteerActivitiesDetailData.content + Constants.BODY_HTML
        webView.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)

        val group = volunteerActivitiesDetailData.group
        if (group != null) {
            flowLayout.visibility = View.VISIBLE
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            for (index in 0 until group.size) {
                val textView = TextView(this)
                textView.setLineSpacing(1.2f, 1.2f)
                textView.setTextColor(ContextCompat.getColor(this, R.color.white))
                textView.setBackgroundResource(R.drawable.text_view_yellow_bg)
                textView.setPadding(DisplayManager.dip2px(10f)!!,
                        DisplayManager.dip2px(5f)!!,
                        DisplayManager.dip2px(10f)!!,
                        DisplayManager.dip2px(5f)!!)
                params.setMargins(DisplayManager.dip2px(10f)!!, DisplayManager.dip2px(5f)!!,
                        0, DisplayManager.dip2px(5f)!!)
                textView.layoutParams = params
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                textView.text = group[index]
                flowLayout.addView(textView)
            }
        }
        val points = volunteerActivitiesDetailData.points
        tvPoints.text = "${points}分"
        val num = volunteerActivitiesDetailData.num
        tvNum.text = "已报名${num}人"
        val needNum = volunteerActivitiesDetailData.need_num
        tvNeedNum.text = "/需${needNum}人"
        val startTime = volunteerActivitiesDetailData.start_time
        val endTime = volunteerActivitiesDetailData.end_time
        tvTime.text = DateUtils.getTimeToChina(startTime) + "-" + DateUtils.getTimeToChina(endTime)
        val nowTime = DateUtils.getNowTime()
        val isSignOut = volunteerActivitiesDetailData.is_signout
        val isSignIn = volunteerActivitiesDetailData.is_signin
//        if (DateUtils.isDateOneBigger(nowTime, endTime)) {
//            isSignOut 签退状态,0未签退,1已签退
        if (isSignIn == 1 && isSignOut == 0) {
            tvSignOut.visibility = View.VISIBLE
        } else {
            tvSignOut.visibility = View.GONE
            llSign.visibility = View.VISIBLE
        }
//        }else{
        // is_signin=0 ,表示未签到,  =1,表示已签到
        when (isSignIn) {
            0 -> tvSignIn.visibility = View.VISIBLE
            else -> tvSignIn.visibility = View.GONE
        }
//        }
        tvSignOut.setOnClickListener {
            if (DateUtils.isDateOneBigger(endTime, nowTime)) {
                showShort("活动未结束，暂时无法签退")
                return@setOnClickListener
            }
            //跳转到志愿者活动签退页面
            val intent = Intent(this, VolunteerActivitiesSignOutActivity::class.java)
            intent.putExtra(Constants.ID, mId)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        val area = volunteerActivitiesDetailData.area
        val address = volunteerActivitiesDetailData.address
        tvAreaAddress.text = if (area == null) address else area + address
        val author = volunteerActivitiesDetailData.author
        tvAuthor.text = author
        val phone = volunteerActivitiesDetailData.phone
        tvPhone.text = phone
        val remark = volunteerActivitiesDetailData.remark
        tvRemark.text = remark
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }

    override fun setAddActivityData(isSuccess: Boolean, msg: String, errorCode: Int) {
        when {
            isSuccess -> {
//            报名成功并且刷新数据
                showShort("报名成功")
                start()
            }
            //   code": 202,   "message": "当前您还没有绑定志愿者身份，点击确定前往",
            errorCode == 202 -> {
                AlertDialog.Builder(this)
                        .setTitle(msg)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定") { _, _ ->
                            //跳转到志愿者绑定页面
                            toActivity(VolunteerBindingActivity::class.java)
                        }.create().show()
            }
            else -> showShort(msg)
        }
    }

    override fun setSignInData(isSuccess: Boolean, msg: String) {}
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
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
        AndPermission.with(this@MyVolunteerActivitiesDetailActivity)
                .runtime()
                .setting()
                .start()
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}