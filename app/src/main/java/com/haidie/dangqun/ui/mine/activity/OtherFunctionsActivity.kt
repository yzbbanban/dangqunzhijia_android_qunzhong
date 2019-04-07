package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.MineContract
import com.haidie.dangqun.mvp.model.bean.MineData
import com.haidie.dangqun.mvp.presenter.mine.MinePresenter
import com.haidie.dangqun.ui.main.activity.LoginActivity
import com.haidie.dangqun.ui.mine.adapter.OtherFunctionsAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import com.just.agentweb.AgentWebConfig
import com.tencent.android.tpush.XGPushManager
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/23 10:09
 * description  其他功能
 */
class OtherFunctionsActivity : BaseActivity(), MineContract.View {
    private val mPresenter by lazy { MinePresenter() }
    private var mData = mutableListOf<String>()
    private val title = arrayOf("关于我们", "退出登录", "修改密码")
    private var otherFunctionsAdapter: OtherFunctionsAdapter? = null
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var loginAccount by Preference(Constants.ACCOUNT, Constants.EMPTY_STRING)
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initData() {
        mData.addAll(title)
        otherFunctionsAdapter = OtherFunctionsAdapter(R.layout.mine_item, mData)
        recycler_view?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(this))
            it.adapter = otherFunctionsAdapter
        }
        otherFunctionsAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            when (position) {
                //跳转到关于我们页面
                0 -> toActivity(AboutUsActivity::class.java)
//                退出登录
                1 -> mPresenter.getLogoutData(uid, token)
                //跳转到修改密码页面
                2 -> toActivity(ChangePasswordActivity::class.java)
            }
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "其他功能"
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {}
    override fun setMineData(mineData: MineData) {}
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }

    override fun logoutSuccess() {
        try {
            //解绑指定账号（3.2.2以及3.2.2之后的版本使用，无注册回调）
            XGPushManager.delAccount(this, loginAccount)
            //清空缓存
            AgentWebConfig.clearDiskCache(this)
            AgentWebConfig.removeAllCookies()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } catch (e: Exception) {

        }
    }

    override fun showRefreshEvent() {}
    override fun showLoading() {}
    override fun dismissLoading() {}
}