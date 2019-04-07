package com.haidie.dangqun.ui.mine.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.mine.MineContract
import com.haidie.dangqun.mvp.model.bean.MineData
import com.haidie.dangqun.mvp.presenter.mine.MinePresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.OnlineConsultingActivity
import com.haidie.dangqun.ui.main.view.RuntimeRationale
import com.haidie.dangqun.ui.mine.activity.*
import com.haidie.dangqun.ui.mine.adapter.MineAdapter
import com.haidie.dangqun.ui.mine.view.MineRecyclerViewDividerItemDecoration
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.mine_header_view.view.*

/**
 * Created by admin2
 *  on 2018/08/10  14:30
 * description  我的
 */
class MineFragment : BaseFragment() , MineContract.View{

    private val mPresenter by lazy { MinePresenter() }
    private var mData = mutableListOf<String>()
    private var mineAdapter : MineAdapter? = null
    private var mTitle: String? = null
    private var isRefresh = false
    private var ivScan: ImageView? = null
    private var mIvPhoto: ImageView? = null
    private var tvNickName: TextView? = null
    private var collection: TextView? = null
    private var attention: TextView? = null
    private var fans: TextView? = null
    private var ivEdit: ImageView? = null
    private var llCollection: LinearLayout? = null
    private var llAttention: LinearLayout? = null
    private var llMyFans: LinearLayout? = null
    private val title = arrayOf(
//            "我的订单","我的活动","我的投票",
            "我的求助","我的上报","我的咨询",
            "我发布的动态", "我发布的商品",
            "我的评价", "志愿者绑定",
            "其他功能")

    private val mHeaderView by lazy { getHeaderView() }

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    companion object {
        fun getInstance(title: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine
    override fun initView() {
        mPresenter.attachView(this)
        smartLayout.setOnRefreshListener {
            isRefresh = true
            lazyLoad()
        }
        mineAdapter = MineAdapter(R.layout.mine_item, mData)
        mRecyclerView?.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(MineRecyclerViewDividerItemDecoration(activity))
            it.adapter = mineAdapter
        }
        smartLayout.setEnableHeaderTranslationContent(true)
        mLayoutStatusView = multipleStatusView
    }
    override fun lazyLoad() { mPresenter.getMineData(uid,token) }
    override fun showRefreshEvent() {  smartLayout.autoRefresh() }
    override fun setMineData(mineData: MineData) {
        smartLayout.finishRefresh()
        mData.clear()
        mData.addAll(title)

        mineAdapter?.let {
            it.replaceData(mData)
            val headerLayout = it.headerLayout
            if (headerLayout == null) {
                it.addHeaderView(mHeaderView)
            }
        }
        tvNickName?.text = mineData.nickname
        ImageLoader.loadCircle(activity,mineData.avatar.trim(),mIvPhoto!!)
        val count = mineData.count
        collection?.text = count.collection.toString()
        attention?.text = count.attention.toString()
        fans?.text = count.fans.toString()
        ivScan?.setOnClickListener { _ ->
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                    .rationale(RuntimeRationale())
                    //跳转到二维码页面
                    .onGranted { startActivity(Intent(activity, ScanActivity::class.java)) }
                    .onDenied { permissions -> showSettingDialog(activity, permissions) }
                    .start()
        }
        ivEdit?.setOnClickListener {
            //跳转到修改个人信息页面
            val intent = Intent(activity, ModifyInformationActivity::class.java)
            intent.putExtra(Constants.AVATAR, if (mineData.avatar.isEmpty())
                Constants.EMPTY_STRING else mineData.avatar.trim())
            intent.putExtra(Constants.NICKNAME, mineData.nickname)
            intent.putExtra(Constants.GENDER, mineData.gender)
            intent.putExtra(Constants.BIRTHDAY, if (mineData.birthday.isNullOrEmpty())
                Constants.EMPTY_STRING else mineData.birthday)
            startActivity(intent)
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        //跳转到我的收藏页面
        llCollection?.setOnClickListener {
            MyPublicPageActivity.start(activity, Constants.MY_TYPE_COLLECTION)
        }
        //跳转到我的关注页面
        llAttention?.setOnClickListener {
             MyPublicPageActivity.start(activity,Constants.MY_TYPE_ATTENTION)
        }
        //跳转到我的粉丝页面
        llMyFans?.setOnClickListener {
            MyPublicPageActivity.start(activity, Constants.MY_TYPE_FANS)
        }
        mineAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener {
            _, _, position ->
            when(position){
//                0 ->    toActivity(MyOrderActivity::class.java)                            //跳转到我的订单页面
//                1 ->  toActivity(MyActivityActivity::class.java)                         //跳转到我的活动页面
//                2 ->  toActivity(MyVoteActivity::class.java)                             //跳转到我的投票页面
                //跳转到我的求助页面
                0 ->  toActivity(MyHelpActivity::class.java)
                //跳转到我的上报页面
                1 ->  toActivity(MyReportActivity::class.java)
                //跳转到在线咨询页面
                2 ->  toActivity(OnlineConsultingActivity::class.java)
                //跳转到我的文章页面
                3 ->  MyPublicPageActivity.start(activity,Constants.MY_TYPE_ARTICLE)
                //跳转到我的商品页面
                4 ->   MyPublicPageActivity.start(activity,Constants.MY_TYPE_PRODUCT)
                //跳转到我的消息页面
                5 ->  MyPublicPageActivity.start(activity,Constants.MY_TYPE_MESSAGE)
                //跳转到我的关注页面
//                6 ->   MyPublicPageActivity.start(activity,Constants.MY_TYPE_ATTENTION)
                //跳转到志愿者绑定页面
                6 ->  toActivity(VolunteerBindingActivity::class.java)
                //跳转到其他功能页面
                7 ->  toActivity(OtherFunctionsActivity::class.java)
            }
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
    private fun getHeaderView() : View{
        val view = layoutInflater.inflate(R.layout.mine_header_view, mRecyclerView.parent as ViewGroup, false)
//        ivScan = view.ivScan
        mIvPhoto = view.iv_photo
        tvNickName = view.tv_nick_name
        collection = view.tvCollection
        attention = view.tvAttention
        fans = view.tvFans
        ivEdit = view.iv_edit
        llCollection = view.ll_collection
        llAttention = view.llAttention
        llMyFans = view.ll_my_fans
        return view
    }
    override fun showLoading() {
        if (!isRefresh) { mLayoutStatusView?.showLoading() }
    }
    override fun dismissLoading() {  mLayoutStatusView?.showContent() }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun logoutSuccess() {}
}