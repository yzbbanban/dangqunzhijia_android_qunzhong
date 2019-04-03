package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.glide.GlideApp
import com.haidie.dangqun.mvp.contract.home.ArticleActivityVoteDetailContract
import com.haidie.dangqun.mvp.model.bean.VoteDetailData
import com.haidie.dangqun.mvp.presenter.home.ArticleActivityVoteDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.VoteDetailListViewAdapter
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_article_activity_vote_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/12 14:10
 * description  文章、活动、投票详情
 */
class ArticleActivityVoteDetailActivity : BaseActivity(),ArticleActivityVoteDetailContract.View {

    private val mPresenter by lazy { ArticleActivityVoteDetailPresenter() }
    private var mId: Int? = null
    private var isRefresh = false
    private lateinit var mInfo: VoteDetailData.Info
    private  var mList: MutableList<VoteDetailData.VoteDetailListItemData>? = null
    private lateinit var mAdapter: VoteDetailListViewAdapter
    private var mVInfoId: String? = null
    private var uid: Int by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_article_activity_vote_detail
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
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getVoteDetailData(mId!!.toString())
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

    @SuppressLint("SetTextI18n")
    override fun setVoteDetailData(voteDetailData: VoteDetailData) {
        mInfo = voteDetailData.info
        tv_article_title.text = mInfo.title
        tv_create_time.text = mInfo.create_time
        ll_start_time.visibility = View.VISIBLE
        ll_end_time.visibility = View.VISIBLE
        tv_start_time_text.text = "投票开始时间:"
        tv_start_time.text = mInfo.start_time
        tv_end_time_text.text = "投票结束时间:"
        tv_end_time.text = mInfo.end_time
        tv_view_text.text = "浏览量:"
        tv_view.text = "${mInfo.view}"
        val url = Constants.HTML_BODY + mInfo.content + Constants.BODY_HTML
        web_view.loadDataWithBaseURL(null, url, Constants.TEXT_HTML, Constants.UTF_8, null)
        if (mInfo.pic.isNotEmpty()) {
            iv_pic.visibility = View.VISIBLE
            GlideApp.with(this).load(mInfo.pic)
                    .error(R.drawable.icon_default)
                    .into(iv_pic)
        }
        mList = voteDetailData.list
        if (mList != null) {
            val num = mInfo.num
            tv_num.text = "总共投票：$num"
            tv_vote.text = "投票"
            mAdapter = VoteDetailListViewAdapter(this, mList!!, num)
            lv_vote.adapter = mAdapter
            mAdapter.setSingleChoiceOrMultipleChoice(mInfo.type == 0)
            tv_single_or_multiple.text = if (mInfo.type == 0) "单选投票" else "多选投票"
            frame_layout_vote.visibility = View.VISIBLE
        }

        tv_vote.setOnClickListener {
            CircularAnim.hide(tv_vote)
                    .endRadius((progress_bar_vote.height / 2).toFloat())
                    .go({ progress_bar_vote.visibility = View.VISIBLE })
            mVInfoId = ""
            val isSingle = mAdapter.getChoiceMode()
            if (isSingle) {
                val selectPosition = mAdapter.getSelectPosition()
                if (-1 == selectPosition) {
                    showShort("未选择")
                    showTvVote()
                    return@setOnClickListener
                }
                val voteDetailListItemData = mList!![selectPosition]
                val id = voteDetailListItemData.id
                mVInfoId += id
            } else {
                val sparseBooleanArray = mAdapter.getMap()
                val size = sparseBooleanArray.size()
                for (i in 0 until size) {
                    val b = sparseBooleanArray.get(sparseBooleanArray.keyAt(i))
                    if (b) {
                        val voteDetailListItemData = mList!![sparseBooleanArray.keyAt(i)]
                        val id = voteDetailListItemData.id
                        mVInfoId +=   "$id,"
                    }
                }
                if (size == 0 || mVInfoId!!.isEmpty()) {
                    showShort("未选择")
                    showTvVote()
                    return@setOnClickListener
                }
                if (mVInfoId!!.endsWith(",")) {
                    mVInfoId = mVInfoId!!.substring(0, mVInfoId!!.length - 1)
                }
            }
            //调用投票接口
//           http://192.168.3.3/dangqun_backend_mayun/public/api/vote/addVoteIn
            mPresenter.getAddVoteInData(mInfo.id,mVInfoId!!,uid)
        }
    }
    override fun setAddVoteInData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("投票成功")
            //返回到上级页面
            onBackPressed()
        }else{
            showShort(msg)
            showTvVote()
        }
    }
    private fun showTvVote() {
        CircularAnim.show(tv_vote).go()
        progress_bar_vote.visibility = View.GONE
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
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
}