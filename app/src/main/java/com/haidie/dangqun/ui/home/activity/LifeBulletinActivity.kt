package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.LifeBulletinContract
import com.haidie.dangqun.mvp.model.bean.ArticleListData
import com.haidie.dangqun.mvp.presenter.home.LifeBulletinPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.LifeBulletinAdapter
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_life_bulletin.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/08 17:20
 * description  生活公告
 */
class LifeBulletinActivity : BaseActivity(),LifeBulletinContract.View {
    private val mPresenter by lazy { LifeBulletinPresenter() }
    private var mId: String? = null
    private var isRefresh = false
    private var page: Int = 1
    private lateinit var mData: MutableList<ArticleListData.ArticleListItemData>
    override fun getLayoutId(): Int = R.layout.activity_life_bulletin

    override fun initData() {
        val intent = intent
        mId = intent.getStringExtra(Constants.ID)
    }

    private lateinit var mAdapter: LifeBulletinAdapter

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "生活公告"
        mLayoutStatusView = multiple_status_view
        mData = mutableListOf()
        mAdapter = LifeBulletinAdapter(R.layout.layout_life_bulletin_item, mData)
        mAdapter.setOnItemClickListener{
            _, _, position ->
//            跳转到生活公告详情页面
            val intent = Intent(this@LifeBulletinActivity, LifeBulletinDetailActivity::class.java)
            intent.putExtra(Constants.ID,"${mAdapter.data[position].id}")
            intent.putExtra(Constants.TYPE,Constants.ARTICLE)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)

        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
        smart_layout.setOnRefreshListener {
            isRefresh = true
            page = 1
            start()
            it.finishRefresh(1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getArticleListData(mId!!) }
    private var isFirst: Boolean = true
    override fun setArticleListData(articleListData: ArticleListData) {
        mData = articleListData.list
        when{
            isRefresh -> {
                if (mData.isEmpty()) {
                    showShort( "暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    smart_layout.isEnableLoadMore = false
                    smart_layout.isEnableRefresh = false
                    return
                }
                smart_layout.isEnableRefresh = true
                mAdapter.replaceData(mData)
                smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                isFirst = false
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                    isFirst = false
                } else {
                    if (page > 1) page --
                    if (isFirst) {
                        mLayoutStatusView?.showEmpty()
                        smart_layout.isEnableRefresh = false
                        smart_layout.isEnableLoadMore = false
                        showShort( "暂无数据内容")
                    }else{
                        showShort(resources.getString(R.string.load_more_no_data))
                    }
                }
            }
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
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
}