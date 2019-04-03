package com.haidie.dangqun.ui.home.activity

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.ImageView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.OnlineHelpDetailsContract
import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.mvp.model.bean.OnlineHelpHistoryReplayData
import com.haidie.dangqun.mvp.presenter.home.OnlineHelpDetailsPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.OnlineHelpHistoryReplyListViewAdapter
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_online_help_details.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/12 18:12
 * description  网上求助详情
 */
class OnlineHelpDetailsActivity : BaseActivity(),OnlineHelpDetailsContract.View {

    private var mId: Int? = null
    private var isRefresh = false
    private val page: Int = 1
    private val type: Int = 3    //填3,表示网上求助
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var username: String by Preference(Constants.USERNAME, Constants.EMPTY_STRING)
    private val mPresenter by lazy { OnlineHelpDetailsPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_online_help_details
    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
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

        tv_send.setOnClickListener {
            //调用网上求助留言接口,成功后关闭软键盘并且隐藏输入框刷新当前页面数据
            val content = et_edit_content.text.toString()
            if (content.isEmpty()) {
                showShort("请输入留言内容")
                return@setOnClickListener
            }
            mPresenter.getOnlineHelpToReplayData(uid,token,mId!!.toInt(),type,content)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {
        mPresenter.getOnlineHelpDetailsData(uid,mId!!,token,page,Constants.SIZE)
    }
    override fun setOnlineHelpDetailsData(onlineHelpDetailsData: OnlineHelpDetailsData) {
        tv_detail_title.text = onlineHelpDetailsData.title
        //上报类型 0,其他,1.儿童,2.老人,3.妇女4.特困群众 ,5.军烈属,6.建档立卡户,7.残疾人
        val type = onlineHelpDetailsData.type
        tv_type.text = when(type){
            0 -> "其他"
            1 -> "儿童"
            2 -> "老人"
            3 -> "妇女"
            4 -> "特困群众"
            5 -> "军烈属"
            6 -> "建档立卡户"
            else -> "残疾人"
        }
        val troubleType = onlineHelpDetailsData.troubletype
         //困难类型0.其他, 1.因病,2.因残,3.因学,4.因灾,5.因技术,6.缺劳动力,7.缺资金
        tv_trouble_type.text = when(troubleType){
            0 -> "其他"
            1 -> "因病"
            2 -> "因残"
            3 -> "因学"
            4 -> "因灾"
            5 -> "因技术"
            6 -> "缺劳动力"
            else -> "残疾人"
        }
        tv_username.text = onlineHelpDetailsData.username
//        帮扶状态,0未帮扶,1帮扶中,2已帮扶
        val status = onlineHelpDetailsData.status
        tv_status.text = when(status){
            0 -> "未帮扶"
            1 -> "帮扶中"
            else -> "已帮扶"
        }
        tv_phone.text = onlineHelpDetailsData.phone
//        性别0,表示女,1表示男
        tv_gender.text = if (onlineHelpDetailsData.gender == 1) "男" else "女"
        val content = Constants.HTML_BODY + onlineHelpDetailsData.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, content, Constants.TEXT_HTML, Constants.UTF_8, null)

        val localMedia = ArrayList<LocalMedia>()
        val pic = onlineHelpDetailsData.pic1
        if (pic != null) {
            val url = UrlConstant.BASE_URL_HOST + pic
            ImageLoader.load(this, pic, iv_pic1)
            initImages(localMedia, url, iv_pic1, 0)
        }
        val pic2 = onlineHelpDetailsData.pic2
        if (pic2 != null) {
            val url = UrlConstant.BASE_URL_HOST + pic2
            ImageLoader.load(this, pic2, iv_pic2)
            initImages(localMedia, url, iv_pic2, 1)
        }
        val pic3 = onlineHelpDetailsData.pic3
        if (pic3 != null) {
            val url = UrlConstant.BASE_URL_HOST + pic3
            ImageLoader.load(this, pic3, iv_pic3)
            initImages(localMedia, url, iv_pic3, 2)
        }
        if (pic != null || pic2 != null || pic3 != null) {
            ll_pic.visibility = View.VISIBLE
        }
        tv_create_time.text = onlineHelpDetailsData.create_time
        tv_update_time.text = onlineHelpDetailsData.update_time
        tv_help_desc.text = onlineHelpDetailsData.help_desc
    }
    override fun setOnlineHelpHistoryReplayData(onlineHelpHistoryReplayData: OnlineHelpHistoryReplayData) {
        val list = onlineHelpHistoryReplayData.list
        if (list.isEmpty())  return
        val adapter = OnlineHelpHistoryReplyListViewAdapter(this, list)
        adapter.setLoginUsername(username)
        lv_reply.adapter = adapter
        nsv.smoothScrollTo(0, 0)
        lv_reply.isFocusable = false
        tv_detail_title.isFocusable = true
        tv_detail_title.isFocusableInTouchMode = true
        tv_detail_title.requestFocus()
    }
    override fun setOnlineHelpToReplayData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("留言成功")
            et_edit_content.text.clear()
            //软键盘
            closeKeyboard(et_edit_content,this)
            start()
        } else {
            showShort(msg)
        }
    }
    private fun initImages(localMedia: ArrayList<LocalMedia>, url: String, iv: ImageView, position: Int) {
        val localMedia1 = LocalMedia()
        localMedia1.path = url
        localMedia.add(localMedia1)
        iv.setOnClickListener {
            PictureSelector.create(this@OnlineHelpDetailsActivity)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(position, localMedia)
        }
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
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
}