package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/11 09:20
 * description  积分规则
 */
class IntegralRuleActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_integral_rule

    override fun initData() {
    }

    override fun initView() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "积分规则"
    }

    override fun start() {
    }
}