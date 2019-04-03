package com.haidie.dangqun.ui.main.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.main.MainContract
import com.haidie.dangqun.mvp.event.ReloadLifeEvent
import com.haidie.dangqun.mvp.presenter.main.MainPresenter
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.business.fragment.BusinessFragment
import com.haidie.dangqun.ui.home.fragment.HomeFragment
import com.haidie.dangqun.ui.life.fragment.LifeFragment
import com.haidie.dangqun.ui.main.view.RuntimeRationale
import com.haidie.dangqun.ui.mine.activity.MyHelpActivity
import com.haidie.dangqun.ui.mine.activity.MyReportActivity
import com.haidie.dangqun.ui.mine.fragment.MineFragment
import com.haidie.dangqun.ui.release.activity.ReleaseArticleActivity
import com.haidie.dangqun.ui.release.activity.ReleaseProductActivity
import com.haidie.dangqun.utils.ActivityCollector
import com.haidie.dangqun.utils.LogHelper
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.ReleaseDialog
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushManager
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*

class  MainActivity : BaseActivity(),MainContract.View {

    private val mTitle = arrayOf("首页", "商圈", "生活", "我的")
    private val mTag = arrayOf("home", "business", "life", "mine")
    //未选中图标
    private val mIconUnselectedIds = intArrayOf(R.drawable.ic_home_unselected, R.drawable.ic_business_unselected,
            R.drawable.ic_life_unselected, R.drawable.ic_mine_unselected)
    //选中图标
    private val mIconSelectIds = intArrayOf(R.drawable.ic_home_select, R.drawable.ic_business_select,
            R.drawable.ic_life_select, R.drawable.ic_mine_select)
    private val mPresenter by lazy { MainPresenter() }
    private var mHomeFragment: HomeFragment? = null
    private var mBusinessFragment: BusinessFragment? = null
    private var mLifeFragment: LifeFragment? = null
    private var mMineFragment: MineFragment? = null
    private var currentFragment : BaseFragment? = null
    private var mIndex = 0
    private var textTypeface: Typeface? = null
    private var releaseDialog: ReleaseDialog? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var loginAccount by Preference(Constants.ACCOUNT,Constants.EMPTY_STRING)
    override fun onCreate(savedInstanceState: Bundle?) {
//        启动并注册APP，同时绑定账号,推荐有帐号体系的APP使用
        initXGRegister()
        AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_FINE_LOCATION,Permission.READ_EXTERNAL_STORAGE)
                .rationale(RuntimeRationale())
                .onDenied { permissions ->
                    showSettingDialog(this, permissions)
                }
                .start()
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(Constants.CURRENT_TAB_INDEX)
        }
        super.onCreate(savedInstanceState)
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, Constants.FONTS_REGULAR)
        tv_home.typeface = textTypeface
        tv_business_circle.typeface = textTypeface
        tv_release.typeface = textTypeface
        tv_life.typeface = textTypeface
        tv_mine.typeface = textTypeface
        switchFragment(mIndex)
        mPresenter.attachView(this)
    }
    override fun initXG() {
        initXGRegister()
        RxBus.getDefault().post(ReloadLifeEvent())
    }
    private fun initXGRegister() {
        XGPushManager.appendAccount(this, loginAccount, object : XGIOperateCallback {
            override fun onSuccess(data: Any?, p1: Int) {
                mPresenter.getXGData(uid, data as String, token)
            }
            override fun onFail(p0: Any?, errCode: Int, msg: String?) {
                initXGRegister()
            }
        })
    }
    private fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting) { _, _ ->
                    setPermission()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
    }
    private fun setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .start()
    }
    override fun onResume() {
        super.onResume()
        val click = XGPushManager.onActivityStarted(this)
        if (click != null) { // 判断是否来自信鸽的打开方式
            LogHelper.d("====================\n通知被点击:" + click.toString())
        }
    }
    override fun onPause() {
        super.onPause()
        XGPushManager.onActivityStoped(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        releaseDialog?.dismiss()
        var tab = intent!!.getIntExtra(Constants.TAB, -1)
        val reportOrHelp = intent.getIntExtra(Constants.IS_REPORT_OR_HELP,-1)
        if (tab == -1) {
            tab = 0
        }
        switchFragment(tab)
        if (tab == 3) {
            when (reportOrHelp) {
                1 -> startActivity(Intent(this, MyReportActivity::class.java))
                2 -> startActivity(Intent(this, MyHelpActivity::class.java))
            }
        }
    }
    /**
     * 切换Fragment
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 // 首页
            -> mHomeFragment?.let {
                transaction.show(it)
                currentFragment = it
            } ?: HomeFragment.getInstance(mTitle[position]).let {
                mHomeFragment = it
                transaction.add(R.id.fl_container, it, mTag[position])
                currentFragment = it
            }
            1  //商圈
            -> mBusinessFragment?.let {
                transaction.show(it)
                currentFragment = it
            } ?: BusinessFragment.getInstance(mTitle[position]).let {
                mBusinessFragment = it
                transaction.add(R.id.fl_container, it, mTag[position])
                currentFragment = it
            }
            2  //生活
            -> mLifeFragment?.let {
                transaction.show(it)
                currentFragment = it
            } ?: LifeFragment.getInstance(mTitle[position]).let {
                mLifeFragment = it
                transaction.add(R.id.fl_container, it, mTag[position])
                currentFragment = it
            }
            3 //我的
            -> mMineFragment?.let {
                transaction.show(it)
                currentFragment = it
            } ?: MineFragment.getInstance(mTitle[position]).let {
                mMineFragment = it
                transaction.add(R.id.fl_container, it, mTag[position])
                currentFragment = it
            }
        }
        mIndex = position
        transaction.commitAllowingStateLoss()

        showNormal(position)
    }
    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mBusinessFragment?.let { transaction.hide(it) }
        mLifeFragment?.let { transaction.hide(it) }
        mMineFragment?.let { transaction.hide(it) }
    }
    override fun initData() {
        rl_release.setOnClickListener { _ ->
            if (releaseDialog == null) {
                releaseDialog = ReleaseDialog(this@MainActivity,R.style.main_release_dialog_style)
                releaseDialog!!.setProductClickListener(View.OnClickListener{ toActivity(ReleaseProductActivity::class.java)  })
                releaseDialog!!.setArticleClickListener(View.OnClickListener { toActivity(ReleaseArticleActivity::class.java)  })
            }
            releaseDialog!!.show()
        }
        rl_home.setOnClickListener {  switchFragment(0) }
        rl_business_circle.setOnClickListener {  switchFragment(1)  }
        rl_life.setOnClickListener {  switchFragment(2) }
        rl_mine.setOnClickListener { switchFragment(3)  }
    }
    private fun showNormal(tab : Int) {
        iv_home.setImageResource(mIconUnselectedIds[0])
        iv_business_circle.setImageResource(mIconUnselectedIds[1])
        iv_life.setImageResource(mIconUnselectedIds[2])
        iv_mine.setImageResource(mIconUnselectedIds[3])
        when (tab) {
            0 ->  iv_home.setImageResource(mIconSelectIds[0])
            1 ->  iv_business_circle.setImageResource(mIconSelectIds[1])
            2 ->  iv_life.setImageResource(mIconSelectIds[2])
            3 ->  iv_mine.setImageResource(mIconSelectIds[3])
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        outState.putInt(Constants.CURRENT_TAB_INDEX, mIndex)
    }
    override fun initView() {}
    override fun start() {}
    private var clickTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - clickTime > 2000) {
                showShort("再按一次退出程序")
                clickTime = System.currentTimeMillis()
            } else {
                ActivityCollector.instance.exitApp()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun switchToBusiness() {  switchFragment(1) }
    override fun showLoading() {}
    override fun dismissLoading() {}
}
