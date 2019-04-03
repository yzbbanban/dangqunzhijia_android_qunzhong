package com.haidie.dangqun.ui.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.mine.IReleasedContract
import com.haidie.dangqun.mvp.model.bean.IReleasedData
import com.haidie.dangqun.mvp.presenter.mine.IReleasedPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.LifeBulletinDetailActivity
import com.haidie.dangqun.ui.mine.adapter.IReleasedAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/14 15:19
 * description  我发布的
 */
class IReleasedFragment : BaseFragment(),IReleasedContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false
    private var mId: Int = -1
    private val mPresenter by lazy { IReleasedPresenter() }
    private lateinit var mData: MutableList<IReleasedData.ListBean>
    private lateinit var mAdapter: IReleasedAdapter

    companion object {
        fun getInstance(param1: Int, param2: String?): IReleasedFragment {
            val fragment = IReleasedFragment()
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
        mAdapter = IReleasedAdapter(R.layout.layout_i_participated_item, mData)
        mAdapter.setOnItemClickListener{
            _, _, position ->
            //跳转到生活公告详情页面
            val intent = Intent(activity, LifeBulletinDetailActivity::class.java)
            intent.putExtra(Constants.ID,"${mAdapter.data[position].id}")
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
    //    避免首次进入时一起加载
    private var isLoad: Boolean = false
    private var isFirst: Boolean = true
    override fun lazyLoad() {
        if (isReload) {
            isFirst = true
        }
        if (isLoad) {
            mPresenter.getIReleasedData(uid,page,Constants.SIZE,token)
        }
    }
    fun setBoolean(load : Boolean){
        isLoad = load
        if (isLoad && isFirst) {
            lazyLoad()
        }
    }
    override fun setIReleasedData(iReleasedData: IReleasedData) {
        isLoad = true
        mData = iReleasedData.list
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
                isFirst = false
            }
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        isFirst = false
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
    override fun showRefreshEvent() { smart_layout.autoRefresh() }
}