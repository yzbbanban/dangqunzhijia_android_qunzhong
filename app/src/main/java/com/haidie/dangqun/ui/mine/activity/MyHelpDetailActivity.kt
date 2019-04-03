package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.view.View
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
import com.haidie.dangqun.utils.DisplayManager
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_my_help_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/10/22 11:00
 * description  我的求助-详情
 */
class MyHelpDetailActivity : BaseActivity(), OnlineHelpDetailsContract.View {

    private var mId: Int? = null
    private val page: Int = 1
    private var isRefresh = false
    private var isHelp: Boolean = false
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var avatar by Preference(Constants.AVATAR, Constants.EMPTY_STRING)
    private var username by Preference(Constants.USERNAME, Constants.EMPTY_STRING)
    private val mPresenter by lazy { OnlineHelpDetailsPresenter() }
    companion object {
        fun startActivity(context: FragmentActivity,id : Int,isHelp : Boolean) {
            val intent = Intent(context, MyHelpDetailActivity::class.java)
            intent.putExtra(Constants.ID,id)
            intent.putExtra(Constants.IS_HELP,isHelp)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_my_help_detail

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "详情"
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
        isHelp = intent.getBooleanExtra(Constants.IS_HELP,Constants.DEFAULT_FALSE)
        mLayoutStatusView = multipleStatusView
    }

    override fun initView() {  mPresenter.attachView(this) }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getOnlineHelpDetailsData(uid,mId!!,token,page,Constants.SIZE)
    }
    override fun setOnlineHelpDetailsData(onlineHelpDetailsData: OnlineHelpDetailsData) {
        if (isHelp) {
            ll_help.visibility = View.VISIBLE
        }
        ImageLoader.loadCircle(this, UrlConstant.BASE_URL_HOST + avatar,ivAvatar)
        tvLoginUsername.text = username
        tvCreateTime.text = onlineHelpDetailsData.create_time
        tvTitle.text = onlineHelpDetailsData.title
        tvContent.text = onlineHelpDetailsData.content
        val localMedia = ArrayList<LocalMedia>()
        val pic1 = onlineHelpDetailsData.pic1
        if (pic1 != null && pic1.isNotEmpty()) {
            val url = UrlConstant.BASE_URL_HOST + pic1
            ImageLoader.load(this, pic1, ivPic1)
            initImages(localMedia, url, ivPic1, 0)
        }
        val pic2 = onlineHelpDetailsData.pic2
        if (pic2 != null && pic2.isNotEmpty()) {
            val url = UrlConstant.BASE_URL_HOST + pic2
            ImageLoader.load(this, pic2, ivPic2)
            initImages(localMedia, url, ivPic2, 1)
        }
        val pic3 = onlineHelpDetailsData.pic3
        if (pic3 != null && pic3.isNotEmpty()) {
            val url = UrlConstant.BASE_URL_HOST + pic3
            ImageLoader.load(this, pic3, ivPic3)
            initImages(localMedia, url, ivPic3, 2)
        }
        if (pic1 != null || pic2 != null || pic3 != null) {
            llPic.visibility = View.VISIBLE
        }
        val screenWidth = DisplayManager.getScreenWidth()
        ivPic1.maxWidth = screenWidth!!
        val pic = onlineHelpDetailsData.pic1
        if (pic != null) {
            ImageLoader.load(this, pic, ivPic1)
        }
        tvUsername.text = onlineHelpDetailsData.username
        tvPhone.text = onlineHelpDetailsData.phone
        when (onlineHelpDetailsData.gender) {
            0 -> {
                rbFemale.isChecked = true
                rbMale.isEnabled = false
            }
            1 -> {
                rbMale.isChecked = true
                rbFemale.isEnabled = false
            }
        }
        tvIdentity.text = onlineHelpDetailsData.identity
        tvAddress.text = onlineHelpDetailsData.address
        tvTroubleType.text = when(onlineHelpDetailsData.troubletype){
//困难类型0.其他, 1.因病,2.因残,3.因学,4.因灾,5.因技术,6.缺劳动力,7.缺资金
            0 -> "其他"
            1 -> "因病"
            2 -> "因残"
            3 -> "因学"
            4 -> "因灾"
            5 -> "因技术"
            6 -> "缺劳动力"
            else -> "缺资金"
        }
        tvStatus.text = when(onlineHelpDetailsData.status){
//            帮扶状态,0未帮扶,1帮扶中,2已帮扶
            0 -> "未帮扶"
            1 -> "帮扶中"
            else -> "已帮扶"
        }
        tvUpdateTime.text = onlineHelpDetailsData.update_time
        tvHelpDesc.text = onlineHelpDetailsData.help_desc
    }
    private fun initImages(localMedia: ArrayList<LocalMedia>, url: String, iv: ImageView, position: Int) {
        val localMedia1 = LocalMedia()
        localMedia1.path = url
        localMedia.add(localMedia1)
        iv.setOnClickListener {
            PictureSelector.create(this@MyHelpDetailActivity)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(position, localMedia)
//            避免预览时闪现桌面背景
            overridePendingTransition(0, 0)
        }
    }
    override fun setOnlineHelpHistoryReplayData(onlineHelpHistoryReplayData: OnlineHelpHistoryReplayData) {
    }
    override fun setOnlineHelpToReplayData(isSuccess: Boolean, msg: String) {
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
}