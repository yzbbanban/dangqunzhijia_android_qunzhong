package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import kotlinx.android.synthetic.main.activity_score_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/29 13:20
 * description  查询结果
 */
class ScoreQueryDetailActivity : BaseActivity() {
    companion object {
        fun start(context: FragmentActivity, username: String, chinese: String, math: String, english: String){
            val intent = Intent(context, ScoreQueryDetailActivity::class.java)
            intent.putExtra(Constants.USERNAME, username)
            intent.putExtra(Constants.CHINESE, chinese)
            intent.putExtra(Constants.MATH, math)
            intent.putExtra(Constants.ENGLISH, english)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_score_detail

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "成绩查询"
    }
    override fun initView() {
        val username = intent.getStringExtra(Constants.USERNAME)
        val chinese = intent.getStringExtra(Constants.CHINESE)
        val math = intent.getStringExtra(Constants.MATH)
        val english = intent.getStringExtra(Constants.ENGLISH)
        tvUsername.text = username
        tvChinese.text = chinese
        tvMath.text = math
        tvEnglish.text = english
    }
    override fun start() {}
}