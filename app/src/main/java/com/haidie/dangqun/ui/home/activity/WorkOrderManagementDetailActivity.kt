package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.WorkOrderManagementDetailContract
import com.haidie.dangqun.mvp.event.WorkOrderManagementDetailEditStatus
import com.haidie.dangqun.mvp.model.bean.HistoryReplayData
import com.haidie.dangqun.mvp.model.bean.OrderInfoData
import com.haidie.dangqun.mvp.presenter.home.WorkOrderManagementDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.home.adapter.HistoryReplyListViewAdapter
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_work_order_management_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/08 11:07
 * description  工单详情页
 */
class WorkOrderManagementDetailActivity : BaseActivity(),WorkOrderManagementDetailContract.View {

    private val mPresenter by lazy { WorkOrderManagementDetailPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var username by Preference(Constants.USERNAME, Constants.EMPTY_STRING)
    private var mId: Int? = null
    private var mGroupId: Int? = null
    private var isRefresh = false
    override fun getLayoutId(): Int = R.layout.activity_work_order_management_detail

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID, Constants.NEGATIVE_ONE)
        mGroupId = intent.getIntExtra(Constants.GROUP_ID, Constants.NEGATIVE_ONE)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        mLayoutStatusView = multiple_status_view

        tv_send.setOnClickListener {
            //调用回复接口,成功后关闭软键盘并且隐藏输入框刷新当前页面数据
            val content = et_edit_content.text.toString()
            if (content.isEmpty()) {
                showShort("请输入回复内容")
                return@setOnClickListener
            }
//                    http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/toReplay
            mPresenter.getToReplayData(uid,mId!!.toInt(),tv_detail_title.text.toString(),content,token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() { mPresenter.getOrderInfoData(uid,mId!!,token,Constants.PAGE,Constants.SIZE) }
    override fun setOrderInfoData(orderInfoData: OrderInfoData) {
        tv_edit_status.visibility = View.GONE
        tv_transfer_order.visibility = View.GONE
        tv_detail_title.text = orderInfoData.title
        tv_type.text = orderInfoData.type
        tv_username.text = orderInfoData.username
        tv_status_value.text = orderInfoData.status_value
        val status = orderInfoData.status
        var editStatus = ""
        if (mGroupId == 1) {
            when (status) {
                0, 1, 2, 4 -> {
                    editStatus = "关闭工单"
                    tv_edit_status.visibility = View.VISIBLE
                }
                3 -> {
                    editStatus = "去评价"
                    tv_edit_status.visibility = View.VISIBLE
                }
            }
        } else {
            when (status) {
                1 -> {
                    editStatus = "去处理"
                    tv_edit_status.visibility = View.VISIBLE
                }
                2 -> {
                    editStatus = "已处理"
                    if (orderInfoData.is_change == null) {
                        tv_transfer_order.text = "转单"
                        tv_transfer_order.visibility = View.VISIBLE
                    }
                    tv_edit_status.visibility = View.VISIBLE
                }
                3, 4 -> {
                    editStatus = "关闭工单"
                    tv_edit_status.visibility = View.VISIBLE
                }
            }
        }
        tv_edit_status.text = editStatus
        tv_edit_status.setOnClickListener {
            if (mGroupId == 1 && status == 3) {
                //跳转到评价页面
                val intent = Intent(this@WorkOrderManagementDetailActivity, WorkOrderEvaluationActivity::class.java)
                intent.putExtra(Constants.ID, mId)
                startActivity(intent)
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
            } else {
                val editStatusText = tv_edit_status.text.toString()
                //                        http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/editStatus
                var statusValue = 5
                when (editStatusText) {
                    "关闭工单" -> //改工单状态为5
                        statusValue = 5
                    "去处理" -> //改工单状态为2
                        statusValue = 2
                    "已处理" -> //改工单状态为3
                        statusValue = 3
                }
                mPresenter.getEditStatusData(uid,mId!!,statusValue,token)
            }
        }
        tv_content.text = orderInfoData.content
        val localMedia = ArrayList<LocalMedia>()
        val pic = orderInfoData.pic1
        if (pic != null) {
            val url = UrlConstant.BASE_URL_HOST + pic
            ImageLoader.load(this, pic, iv_pic1)
            initImages(localMedia, url, iv_pic1, 0)
        }
        val pic2 = orderInfoData.pic2
        if (pic2 != null) {
            val url = UrlConstant.BASE_URL_HOST + pic2
            ImageLoader.load(this, pic2, iv_pic2)
            initImages(localMedia, url, iv_pic2, 1)
        }
        val pic3 = orderInfoData.pic3
        if (pic3 != null) {
            val url = UrlConstant.BASE_URL_HOST + pic3
            ImageLoader.load(this, pic3, iv_pic3)
            initImages(localMedia, url, iv_pic3, 2)
        }
        if (pic != null || pic2 != null || pic3 != null) {
            ll_pic.visibility = View.VISIBLE
        }

        tv_worker.text = orderInfoData.worker ?: ""
        tv_worker_group.text = orderInfoData.worker_group ?: ""
        tv_create_time.text = orderInfoData.create_time
        tv_remark.text = orderInfoData.remark ?: ""
    }

    private fun initImages(localMedia: ArrayList<LocalMedia>, url: String, iv: ImageView, position: Int) {
        val localMedia1 = LocalMedia()
        localMedia1.path = url
        localMedia.add(localMedia1)
        iv.setOnClickListener {
            PictureSelector.create(this@WorkOrderManagementDetailActivity)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(position, localMedia)
        }
    }

    override fun setHistoryReplayData(historyReplayData: HistoryReplayData) {
        val list = historyReplayData.list
        if (list.isEmpty()) {
            return
        }
        val adapter = HistoryReplyListViewAdapter(this, list)
        adapter.setLoginUsername(username)
        lv_reply.adapter = adapter
        nsv.smoothScrollTo(0, 0)
        lv_reply.isFocusable = false
        tv_detail_title.isFocusable = true
        tv_detail_title.isFocusableInTouchMode = true
        tv_detail_title.requestFocus()
    }

    override fun setToReplayData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("回复成功")
            et_edit_content.text.clear()
            //软键盘
            closeKeyboard(et_edit_content,this)
            start()
        } else {
            showShort(msg)
        }
    }
    override fun setEditStatusData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("操作成功")
            //刷新当前页面数据,通知工单管理页面刷新
            RxBus.getDefault().post(WorkOrderManagementDetailEditStatus())
        } else {
            showShort(msg)
        }
    }
    override fun showRefreshEvent() { start() }
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