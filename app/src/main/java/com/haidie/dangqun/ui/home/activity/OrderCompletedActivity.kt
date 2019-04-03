package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.OrderCompleteContract
import com.haidie.dangqun.mvp.presenter.home.OrderCompletePresenter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RatingBar
import kotlinx.android.synthetic.main.activity_order_completed.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/10 14:07
 * description  订单完成
 */
class OrderCompletedActivity : BaseActivity(),OrderCompleteContract.View {

    private val mPresenter by lazy { OrderCompletePresenter() }
    private var mId: Int? = null
    private var mRank = 5
    private val level = arrayOf("一般", "还可以", "满意", "比较满意", "非常满意")
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_order_completed

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
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
//                    http://192.168.3.3/dangqun_backend_mayun/public/api/service/toOrderComment
            mPresenter.getSubmitEvaluationData(uid, mId!!, mRank, et_evaluation_content.text.toString(), token)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showSubmit() {
        CircularAnim.show(tv_submit_evaluation).go()
        progress_bar.visibility = View.GONE
    }
    override fun start() {}
    override fun setSubmitEvaluationData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("评价成功")
            //返回到上级页面
            onBackPressed()
        }else{
            showShort(msg)
            showSubmit()
        }
    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}