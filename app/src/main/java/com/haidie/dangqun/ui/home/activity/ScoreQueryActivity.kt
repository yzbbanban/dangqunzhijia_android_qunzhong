package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.ScoreQueryContract
import com.haidie.dangqun.mvp.model.bean.ScoreQueryData
import com.haidie.dangqun.mvp.presenter.home.ScoreQueryPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_score_query.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/29 08:45
 * description  成绩查询
 */
class ScoreQueryActivity : BaseActivity(),ScoreQueryContract.View {
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { ScoreQueryPresenter() }
    private var mPvOptions: OptionsPickerView<String>? = null
    private var mPvOptionsYear: OptionsPickerView<String>? = null
    private var mPvOptionsType: OptionsPickerView<String>? = null
    private var mType = -1
    override fun getLayoutId(): Int = R.layout.activity_score_query

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "成绩查询"
    }
    override fun initView() {
        mPresenter.attachView(this)
        val data = ArrayList<String>()
        data.add("1")
        data.add("2")
        data.add("3")
        data.add("4")
        data.add("5")
        data.add("6")
        mPvOptions = OptionsPickerBuilder(this@ScoreQueryActivity, OnOptionsSelectListener { options1, _, _, _ ->
            tvClass.text = data[options1]
        }).build()
        mPvOptions?.setPicker(data)
        val dataYear = ArrayList<String>()
        dataYear.add("一")
        dataYear.add("二")
        dataYear.add("三")
        dataYear.add("四")
        dataYear.add("五")
        dataYear.add("六")
        mPvOptionsYear = OptionsPickerBuilder(this@ScoreQueryActivity, OnOptionsSelectListener { options1, _, _, _ ->
            tvYear.text = dataYear[options1]
        }).build()
        mPvOptionsYear?.setPicker(dataYear)
        val dataType = ArrayList<String>()
        dataType.add("第一学期")
        dataType.add("第二学期")
        mPvOptionsType = OptionsPickerBuilder(this@ScoreQueryActivity, OnOptionsSelectListener { options1, _, _, _ ->
            tvType.text = dataType[options1]
            mType = options1
        }).build()
        mPvOptionsType?.setPicker(dataType)
        tvClass.setOnClickListener {
            closeKeyboard(etUsername,this)
            if (!mPvOptions!!.isShowing){
                mPvOptions?.show()
            }
        }
        tvYear.setOnClickListener {
            closeKeyboard(etUsername,this)
            if (!mPvOptionsYear!!.isShowing){
                mPvOptionsYear?.show()
            }
        }
        tvType.setOnClickListener {
            closeKeyboard(etUsername,this)
            if (!mPvOptionsType!!.isShowing){
                mPvOptionsType?.show()
            }
        }
        tvQuery.setOnClickListener {
            closeKeyboard(etUsername,this)
            val username = etUsername.text.toString()
            if (username.isEmpty()) {
                showShort("请输入姓名")
                return@setOnClickListener
            }
            val clazz = tvClass.text.toString()
            if (clazz.isEmpty()) {
                showShort("请选择班级")
                return@setOnClickListener
            }
            val year = tvYear.text.toString()
            if (year.isEmpty()) {
                showShort("请选择年级")
                return@setOnClickListener
            }
            if (mType == -1) {
                showShort("请选择学期")
                return@setOnClickListener
            }
            mPresenter.getScoreQueryData(uid,token,username,year,clazz,mType)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    override fun setScoreQueryData(scoreQueryData: ScoreQueryData) {
        val username = scoreQueryData.username
        val chinese = scoreQueryData.chinese
        val math = scoreQueryData.math
        val english = scoreQueryData.english
        ScoreQueryDetailActivity.start(this,username,chinese, math, english)
    }
    override fun showError(msg: String, errorCode: Int) {
        if (errorCode == ApiErrorCode.SUCCESS) {
            showShort("暂无数据")
            return
        }
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}