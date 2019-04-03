package com.haidie.dangqun.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.api.UrlConstant
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.home.HomeContract
import com.haidie.dangqun.mvp.model.bean.HomeBannerData
import com.haidie.dangqun.mvp.model.bean.HomeNewsData
import com.haidie.dangqun.mvp.presenter.home.HomePresenter
import com.haidie.dangqun.ui.home.activity.*
import com.haidie.dangqun.ui.home.adapter.HomeAdapter
import com.haidie.dangqun.ui.home.adapter.RecyclerViewFunctionsAdapter
import com.haidie.dangqun.ui.home.view.ViewFunctionsDividerItemDecoration
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity
import com.haidie.dangqun.ui.main.view.RuntimeRationale
import com.haidie.dangqun.utils.GlideImageLoader
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.StatusBarUtil
import com.haidie.dangqun.view.HomeRecyclerViewDividerItemDecoration
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_banner_view.view.*


/**
 * Created by admin2
 *  on 2018/08/10  10:03
 * description  首页
 */
class HomeFragment : BaseFragment(), HomeContract.View {

    private val mPresenter by lazy { HomePresenter() }
    private var mData = mutableListOf<HomeNewsData.ListBean>()
    private var homeAdapter: HomeAdapter? = null
    private var recyclerViewFunctionsAdapter: RecyclerViewFunctionsAdapter? = null
    private val texts = arrayOf("活动公告", "家政服务", "生活缴费", "志愿活动",
            "三务公开", "生活公告", "普法宣传", "全部功能")
    private val icons = arrayOf(R.drawable.activity_bulletin, R.drawable.property_housekeeping,
            R.drawable.living_payment, R.drawable.icon_voluntary_activities,
            R.drawable.three_public_affairs, R.drawable.property_announcement,
            R.drawable.laws_and_regulations, R.drawable.all_functions)
    private var dataList = mutableListOf<Map<String, Any>>()
    private val mHeaderView by lazy { getHeaderView() }
    private var mTitle: String? = null
    private var isRefresh = false
    private var mBanner: Banner? = null
    private var mRecyclerViewFunctions : RecyclerView? = null
    private var mFaultRepair : LinearLayout? = null
    private var mOnlineHelp : LinearLayout? = null
    private var mCommodityTrading : LinearLayout? = null
    private var mNearbyMerchants : LinearLayout? = null
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        mPresenter.attachView(this)
        //状态栏透明
        StatusBarUtil.immersive(activity.window)
        smart_layout?.apply {
            //内容跟随偏移
            setEnableHeaderTranslationContent(true)
            setOnRefreshListener {
                isRefresh = true
                lazyLoad()
            }
        }

        mBanner = mHeaderView.banner

        homeAdapter = HomeAdapter(R.layout.home_item, mData)
        recyclerViewFunctionsAdapter = RecyclerViewFunctionsAdapter(R.layout.recycler_view_functions_item, dataList)
        mRecyclerView?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(HomeRecyclerViewDividerItemDecoration(activity))
            it.adapter = homeAdapter
        }

        homeAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener{
            _, _, position ->
            // 跳转到详情页面
//            index/toutiao/detail/user_id/${user_id}/article_id/${val.id}?w=0      后台发布设置w为1
            val url = "index/toutiao/detail/user_id/$uid/article_id/${homeAdapter!!.data[position].id}?w=1"
            LifeDetailActivity.startActivity(activity, url)
        }
        val arrayList = ArrayList<Any>()
//        设置默认轮播图
        arrayList.add(R.drawable.banner_1)
        arrayList.add(R.drawable.main_bg)
        //设置图片集合
        mBanner!!.setImages(arrayList)
        initBanner()
        setHomeAdapterData()
        mLayoutStatusView = multipleStatusView

        mRecyclerViewFunctions?.let {
            it.setHasFixedSize(true)
            it.layoutManager = GridLayoutManager(activity,4)
            it.addItemDecoration(ViewFunctionsDividerItemDecoration(activity))
            it.adapter = recyclerViewFunctionsAdapter
        }
        recyclerViewFunctionsAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            when (position) {
                    //跳转到活动公告页面
                0 ->   toActivity(EventAnnouncementsActivity::class.java)
                    //跳转到家政服务页面
                1 ->   toActivity(HousekeepingActivity::class.java)
                    //跳转到生活缴费页面
                2->  toActivity(LifePaymentActivity::class.java)
                3-> //志愿活动
                    //跳转到活动公告页面
                    toActivity(ActivityBulletinActivity::class.java)
                    //跳转到三务公开列表页面
                4->  toActivity(ThreeAffairsListActivity::class.java)
                5 -> {
                    //跳转到生活公告页面
                    val intent = Intent(activity, GovernmentArticleListActivity::class.java)
                    intent.putExtra(Constants.ID,"29")
                    intent.putExtra(Constants.TEXT,texts[5])
                    intent.putExtra(Constants.TYPE,2)
                    startActivity(intent)
                    activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                6->  {
                    // 普法宣传
                    val intent = Intent(activity, GovernmentArticleListActivity::class.java)
                    intent.putExtra(Constants.ID,"94")
                    intent.putExtra(Constants.TEXT,texts[6])
                    intent.putExtra(Constants.TYPE,2)
                    startActivity(intent)
                    activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
                }
                7->  toActivity(AllFunctionsActivity::class.java)
            }
        }
        //跳转到商品交易页面
        mCommodityTrading?.setOnClickListener { toActivity(CommodityTradingActivity::class.java) }
        mNearbyMerchants?.setOnClickListener { _ ->
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.ACCESS_FINE_LOCATION,Permission.READ_EXTERNAL_STORAGE)
                    .rationale(RuntimeRationale())
                    //跳转到附近商家页面
                    .onGranted { toActivity(NearbyMerchantsActivity::class.java) }
                    .onDenied { permissions ->  showSettingDialog(activity, permissions)  }
                    .start()
        }
        mFaultRepair?.setOnClickListener { _ ->
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.ACCESS_FINE_LOCATION,Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                    .rationale(RuntimeRationale())
                    //跳转到问题上报页面
                    .onGranted { toActivity(ProblemReportActivity::class.java) }
                    .onDenied { permissions ->  showSettingDialog(activity, permissions)  }
                    .start()
        }
        //跳转到网上求助表单提交
//        https://developer.android.com/about/versions/oreo/android-8.0-changes  权限
//        例如，假设某个应用在其清单中列出 READ_EXTERNAL_STORAGE 和 WRITE_EXTERNAL_STORAGE。
// 应用请求 READ_EXTERNAL_STORAGE，并且用户授予了该权限。如果该应用针对的是 API 级别 24 或更低级别，
// 系统还会同时授予 WRITE_EXTERNAL_STORAGE，因为该权限也属于同一 STORAGE 权限组并且也在清单中注册过。
// 如果该应用针对的是 Android 8.0，则系统此时仅会授予 READ_EXTERNAL_STORAGE；不过，
// 如果该应用后来又请求 WRITE_EXTERNAL_STORAGE，则系统会立即授予该权限，而不会提示用户。
//        避免拍照时8.0系统手机写入图片文件权限问题
        mOnlineHelp?.setOnClickListener { _ ->
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .rationale(RuntimeRationale())
                    //跳转到网上求助表单提交页面
                    .onGranted {
                        toActivity(OnlineHelpFormSubmissionActivity::class.java)
                    }
                    .onDenied { permissions ->  showSettingDialog(activity, permissions)  }
                    .start()
        }
    }

    private fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting) { _, _ -> setPermission() }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
    }
    private fun setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .start()
    }
    override fun setHomeBannerData(homeBannerData: HomeBannerData) {
        val list = homeBannerData.list
        val arrayList = arrayListOf<String>()
        list.forEach {
            arrayList.add(UrlConstant.BASE_URL_HOST + it.cover_one)
        }

        mBanner?.apply {
            //设置图片集合
            setImages(arrayList)
            initBanner()
            setOnBannerListener { position ->
                //            跳转到详情页面
//            index/toutiao/detail/user_id/${user_id}/article_id/${val.id}?w=1      后台发布设置w为1
                val url = "index/toutiao/detail/user_id/$uid/article_id/${list[position].id}?w=1"
                LifeDetailActivity.startActivity(activity, url)
            }
        }
    }

    private fun initBanner() {
        mBanner?.apply {
            //设置banner样式
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            //设置图片加载器
            setImageLoader(GlideImageLoader())
            //设置banner动画效果
            setBannerAnimation(Transformer.DepthPage)

            //设置指示器位置（当banner模式中有指示器时）
            setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            start()
        }
    }

    override fun onStart() {
        super.onStart()
        mBanner?.startAutoPlay()
    }
    override fun onStop() {
        super.onStop()
        mBanner?.stopAutoPlay()
    }
    override fun setHomeNewsData(homeNewsData: HomeNewsData) {
        recyclerViewFunctionsAdapter?.replaceData(dataList)
        mData = homeNewsData.list
        setHomeAdapterData()
    }
    private fun setHomeAdapterData() {
        homeAdapter?.let {
            it.replaceData(mData)
            val headerLayout = it.headerLayout
            if (headerLayout == null) {
                it.addHeaderView(mHeaderView)
            }
        }
    }
    override fun lazyLoad() {
        mData.clear()
        dataList.clear()
        for (i in 0 until icons.size) {
            val map = HashMap<String,Any>()
            map[Constants.ICON] = icons[i]
            map[Constants.TEXT] = texts[i]
            dataList.add(map)
        }
        mPresenter.getHomeData(uid,token)
    }
    private fun getHeaderView() : View{
        val view = layoutInflater.inflate(R.layout.home_banner_view, mRecyclerView.parent as ViewGroup, false)
        mRecyclerViewFunctions = view.recycler_view_functions
        mFaultRepair = view.ll_fault_repair
        mOnlineHelp = view.ll_online_help
        mCommodityTrading = view.ll_commodity_trading
        mNearbyMerchants = view.ll_nearby_merchants
        return view
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
        smart_layout.finishRefresh()
    }
    override fun showError(msg: String, errorCode: Int) { showShort(msg) }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}