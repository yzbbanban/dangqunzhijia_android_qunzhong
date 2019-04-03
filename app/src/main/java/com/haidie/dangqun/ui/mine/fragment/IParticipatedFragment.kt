package com.haidie.dangqun.ui.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.mine.IParticipatedContract
import com.haidie.dangqun.mvp.model.bean.IParticipatedData
import com.haidie.dangqun.mvp.presenter.mine.IParticipatedPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.LifeBulletinDetailActivity
import com.haidie.dangqun.ui.mine.adapter.IParticipatedAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/14 15:19
 * description  我参加的
 */
class IParticipatedFragment : BaseFragment(),IParticipatedContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false
    private var mId: Int = -1
    private val mPresenter by lazy { IParticipatedPresenter() }
    private lateinit var mData: MutableList<IParticipatedData.ListBean>
    private lateinit var mAdapter: IParticipatedAdapter
    companion object {
        fun getInstance(param1: Int, param2: String?): IParticipatedFragment {
            val fragment = IParticipatedFragment()
            val args = Bundle()
            args.putInt(Constants.ARG_PARAM1, param1)
            args.putString(Constants.ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initView() {
        mPresenter.attachView(this)
        common_toolbar.visibility = View.GONE
        mId = arguments!!.getInt(Constants.ARG_PARAM1)
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multiple_status_view
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener {
            page = 1
            isRefresh = true
            lazyLoad()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener {
            page++
            isRefresh = false
            lazyLoad()
            it.finishLoadMore(1000)
        }
    }
    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = IParticipatedAdapter(R.layout.layout_i_participated_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
            //跳转到生活公告详情页面
            val intent = Intent(activity, LifeBulletinDetailActivity::class.java)
            intent.putExtra(Constants.ID,"${mAdapter.data[position].aid}")
            intent.putExtra(Constants.TYPE,Constants.ACTIVITY)
            startActivity(intent)
            activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun lazyLoad() { mPresenter.getIParticipatedData(uid,page,Constants.SIZE,mId,token) }
    private var isFirst: Boolean = true
    override fun setIParticipatedData(iParticipatedData: IParticipatedData) {
        mData = iParticipatedData.list
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