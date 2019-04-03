package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.WorkOrderEvaluationContract
import com.haidie.dangqun.mvp.event.WorkOrderManagementDetailEditStatus
import com.haidie.dangqun.mvp.presenter.home.WorkOrderEvaluationPresenter
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RatingBar
import kotlinx.android.synthetic.main.activity_work_order_evaluation.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/08 15:30
 * description  工单评价
 */
class WorkOrderEvaluationActivity : BaseActivity(),WorkOrderEvaluationContract.View {

    private val mPresenter by lazy { WorkOrderEvaluationPresenter() }
    private var mId: Int = 0
    private val level = arrayOf("一般", "还可以", "满意", "比较满意", "非常满意")
    private var mRank = 5
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_work_order_evaluation
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        mId = intent.getIntExtra(Constants.ID, -1)
        iv_back.setImageResource(R.drawable.ic_back_left_white_24dp)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }

        rating_bar.setOnRatingChangeListener(object : RatingBar.OnRatingChangeListener{
            override fun onRatingChange(ratingCount: Float) {
                mRank = ratingCount.toInt()
                tv_evaluation_level.text = level[mRank - 1]
            }
        })
        tv_submit_evaluation.setOnClickListener {
            CircularAnim.hide(tv_submit_evaluation)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
            //改工单状态为4
//                    http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/editStatus
//                    http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/toComment
            val status = 4
            mPresenter.getToCommentData(uid,mId, mRank,et_evaluation_content.text.toString(), token,status)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    override fun setToCommentData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("操作成功")
            //通知工单管理页面刷新
            RxBus.getDefault().post(WorkOrderManagementDetailEditStatus())
            //返回上一个页面
            onBackPressed()
        } else {
            showShort(msg)
            showSubmit()
        }
    }
    private fun showSubmit() {
        CircularAnim.show(tv_submit_evaluation).go()
        progress_bar.visibility = View.GONE
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}