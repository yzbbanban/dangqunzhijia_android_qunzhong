package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VolunteerDetailContract
import com.haidie.dangqun.mvp.event.EditAdministratorStatusEvent
import com.haidie.dangqun.mvp.event.EditPendingVolunteerChangeEvent
import com.haidie.dangqun.mvp.model.bean.VolunteerDetailData
import com.haidie.dangqun.mvp.presenter.home.VolunteerDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import kotlinx.android.synthetic.main.activity_volunteer_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/12 09:07
 * description  志愿者详情
 */
class VolunteerDetailActivity : BaseActivity(),VolunteerDetailContract.View {

    private var mId: Int? = null
    private var mType: String? = null
    private var status = -1
    private var isRefresh = false
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { VolunteerDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_volunteer_detail
    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
        mType = intent.getStringExtra(Constants.TYPE)
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }

        mLayoutStatusView = multiple_status_view
 //       http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/editChange
//        如果同意加入组织,则传入2,如果不同意,则传入3
        tv_refuse.setOnClickListener {  mPresenter.getEditPendingVolunteerChangeData(uid, mId!!, 3, token) }
        tv_agree.setOnClickListener { mPresenter.getEditPendingVolunteerChangeData(uid, mId!!, 2, token) }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {  mPresenter.getVolunteerDetailData(uid,mId!!,token) }

    @SuppressLint("SetTextI18n")
    override fun setVolunteerDetailData(volunteerDetailData: VolunteerDetailData) {
        val username = volunteerDetailData.username
        tv_username.text = username
        val rank = volunteerDetailData.rank
        rating_bar.isClickable = false
        rating_bar.setStar(rank * 1.0f)
        tv_rank.text = "${rank}星志愿者"
        val avatar = volunteerDetailData.avator
        ImageLoader.loadCircle(this, avatar, iv_avator)
        val createTime = volunteerDetailData.create_time
        tv_create_time.text = createTime
        val activity = volunteerDetailData.activity
        tv_activity.text = "${activity}个"
        val score = volunteerDetailData.score
        if (score != null) {
            tv_score.text = if (score.isEmpty()) "" else score
        }else{
            tv_score.text =  ""
        }

        val nation = volunteerDetailData.nation
        tv_nation.text = nation
        val face = volunteerDetailData.face
        tv_face.text = face
        val study = volunteerDetailData.study
        tv_study.text = study
        val skill = volunteerDetailData.skill
        tv_skill.text = skill
        val hobby = volunteerDetailData.hobby
        tv_hobby.text = hobby

        val isLeader = volunteerDetailData.is_leader
        if (Constants.VOLUNTEER == mType) {
            ll_message.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
            when (isLeader) {
                0 -> {
                    tv_is_leader.text = "设为管理员"
                    status = 2
                    frame_layout.visibility = View.VISIBLE
                    tv_is_leader.visibility = View.VISIBLE
                }
                1 ->  frame_layout.visibility = View.GONE
                2 -> {
                    tv_is_leader.text = "取消管理员"
                    status = 0
                    frame_layout.visibility = View.VISIBLE
                    tv_is_leader.visibility = View.VISIBLE
                }
            }
        } else if (Constants.PENDING_VOLUNTEER == mType) {
            frame_layout.visibility = View.GONE
            tv_is_leader.visibility = View.GONE
            linear_layout.visibility = View.VISIBLE
            tv_refuse.visibility = View.VISIBLE
            tv_agree.visibility = View.VISIBLE
        }
        val message = volunteerDetailData.message
        tv_message.text = if (TextUtils.isEmpty(message)) "" else message

        showEditAdministratorStatus()

        tv_is_leader.setOnClickListener {
            CircularAnim.hide(tv_is_leader)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go { progress_bar.visibility = View.VISIBLE }
//                    http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/editStatus
            mPresenter.getEditAdministratorStatusData(uid, mId!!, status, token)
        }
    }

    override fun setEditAdministratorStatusData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            //刷新当前页面数据
            showShort("设置成功")
            start()
            RxBus.getDefault().post(EditAdministratorStatusEvent())
        } else {
            showShort(msg)
            showEditAdministratorStatus()
        }
    }
    override fun setEditPendingVolunteerChangeData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            RxBus.getDefault().post(EditPendingVolunteerChangeEvent())
            showShort("审核成功")
            onBackPressed()
        } else {
            showShort(msg)
        }
    }
    private fun showEditAdministratorStatus() {
        CircularAnim.show(tv_is_leader).go()
        progress_bar.visibility = View.GONE
    }

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
    override fun dismissLoading() {  mLayoutStatusView?.showContent() }
}