package com.haidie.dangqun.ui.home.activity

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.LifeBulletinDetailContract
import com.haidie.dangqun.mvp.model.bean.ActivityDetailData
import com.haidie.dangqun.mvp.model.bean.ArticleDetailData
import com.haidie.dangqun.mvp.presenter.home.LifeBulletinDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_life_bulletin_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/10 08:55
 * description  生活公告详情
 */
class LifeBulletinDetailActivity : BaseActivity(), LifeBulletinDetailContract.View {

    private val mPresenter by lazy { LifeBulletinDetailPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mId: String? = null
    private var mType: String? = null

    override fun getLayoutId(): Int = R.layout.activity_life_bulletin_detail
    override fun initData() {
        mId = intent.getStringExtra(Constants.ID)
        mType = intent.getStringExtra(Constants.TYPE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "详细信息"
        mLayoutStatusView = multiple_status_view
        initWebView()
    }
    private fun initWebView() {
        val webSettings = web_view.settings
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
        web_view.webViewClient = WebViewClient()
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        if (Constants.ARTICLE == mType) {
            mPresenter.getArticleDetailData(mId!!)
        }else if (Constants.ACTIVITY == mType){
            mPresenter.getActivityDetailData(mId!!,uid, token)
        }
    }

    override fun setArticleDetailData(articleDetailDataList: List<ArticleDetailData>) {
        val articleDetailData = articleDetailDataList[0]
        tv_article_title.text = articleDetailData.title
        tv_create_time.text = articleDetailData.create_time
        ll_view.visibility = View.GONE
        val url = Constants.HTML_BODY + articleDetailData.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
    }

    override fun setActivityDetailData(activityDetailData: ActivityDetailData) {
        tv_article_title.text = activityDetailData.title
        tv_author.text = activityDetailData.author
        tv_create_time.text = activityDetailData.create_time
        tv_view.text = "${activityDetailData.view}"
        ll_start_time.visibility = View.VISIBLE
        ll_end_time.visibility = View.VISIBLE
        ll_status.visibility = View.VISIBLE
        ll_area.visibility = View.VISIBLE
        ll_address.visibility = View.VISIBLE
        tv_start_time.text = activityDetailData.start_time
        tv_end_time.text = activityDetailData.end_time
        ll_author.visibility = View.VISIBLE
//        活动状态,0报名中,1已结束
        tv_status.text = if (activityDetailData.status == 0) "报名中" else "已结束"
        tv_area.text = activityDetailData.area
        tv_address.text = activityDetailData.address
        val url = Constants.HTML_BODY + activityDetailData.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)

        frame_layout.visibility = View.VISIBLE
        tv_add_activity.setOnClickListener {
            CircularAnim.hide(tv_add_activity)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go { progress_bar.visibility = View.VISIBLE }
            val endTime = activityDetailData.end_time
            val nowTime = DateUtils.getNowTime()
            if (DateUtils.isDateOneBigger(nowTime, endTime)) {
                showAddActivity()
                showShort("活动已结束")
                return@setOnClickListener
            }
            mPresenter.getAddActivityData(uid, mId!!.toInt(), token)
        }
    }
    override fun setAddActivityData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("报名成功")
            //返回到上级页面
            onBackPressed()
        } else {
            showShort(msg)
            showAddActivity()
        }
    }
    private fun showAddActivity() {
        CircularAnim.show(tv_add_activity).go()
        progress_bar.visibility = View.GONE
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}