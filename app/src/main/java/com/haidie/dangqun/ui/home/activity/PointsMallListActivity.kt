package com.haidie.dangqun.ui.home.activity

import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.PointsMallListContract
import com.haidie.dangqun.mvp.model.bean.PointsMallListData
import com.haidie.dangqun.mvp.presenter.home.PointsMallListPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.PointsMallListAdapter
import com.haidie.dangqun.ui.home.view.PointsMallListDividerItemDecoration
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/11 09:13
 * description  积分商城列表
 */
class PointsMallListActivity : BaseActivity(),PointsMallListContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private val type = 1  //商品分类,默认传入值是1,表示商品类型属于积分商品,0表示普通商品
    private var isRefresh = false
    private lateinit var mData: MutableList<PointsMallListData.ListBean>
    private lateinit var mAdapter: PointsMallListAdapter
    private var mTvScore: TextView? = null
    private var mIvAvatar: ImageView? = null
    private lateinit var mHeaderView: View
    private val mPresenter by lazy { PointsMallListPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "积分商城"
        tv_submit.visibility = View.VISIBLE
        tv_submit.text = "积分规则"

        setRefresh()
        initRecyclerView()
        mHeaderView = getHeaderView()
        mLayoutStatusView = multiple_status_view
        //跳转到积分规则页面
        tv_submit.setOnClickListener { toActivity(IntegralRuleActivity::class.java) }
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener{
            page = 1
            isRefresh = true
            start()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener{
            page++
            isRefresh = false
            start()
            it.finishLoadMore(1000)
        }
    }
    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = PointsMallListAdapter(R.layout.layout_points_mall_list_item, mData)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.tv_exchange -> {
                    //弹出提示确认
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("是否执行兑换操作")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确认") { _, _ ->
                                //调用积分商品兑换接口
                                mPresenter.getExchangeData(uid,mAdapter.data[position].id,token)
                            }
                    builder.create().show()
                }
            }
        }
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.addItemDecoration(PointsMallListDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getPointsMallListData(uid,type,page,Constants.SIZE, token) }
    private var isFirst: Boolean = true
    override fun setPointsMallListData(pointsMallListData: PointsMallListData) {
        mData = pointsMallListData.list
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
                initHeader(pointsMallListData)
                isFirst = false
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                    if (isFirst) {
                        initHeader(pointsMallListData)
                    }
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
    private fun initHeader(pointsMallListData: PointsMallListData) {
        val avatar = pointsMallListData.avatar
        val score = pointsMallListData.score
        val headerLayout = mAdapter.headerLayout
        if (headerLayout == null) {
            mAdapter.addHeaderView(mHeaderView)
        }
        mTvScore?.text = "$score"
        if (avatar.isNotEmpty()) {
            ImageLoader.loadCircle(this, avatar, mIvAvatar!!)
        } else {  mIvAvatar?.setImageResource(R.drawable.icon_default) }
    }
    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.points_mall_list_header, recycler_view.parent as ViewGroup, false)
        mTvScore = view.findViewById(R.id.tv_score)
        mIvAvatar = view.findViewById(R.id.iv_avatar)
        return view
    }
    override fun setExchangeData(isSuccess: Boolean,msg : String) {
        showShort(if (isSuccess) "兑换成功" else msg)
        if (isSuccess){ smart_layout.autoRefresh() }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> if (isFirst) mLayoutStatusView?.showNoNetwork()
            else -> if (isFirst) mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh  && isFirst) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
}