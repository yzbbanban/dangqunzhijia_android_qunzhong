package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.OnlineOrderContract
import com.haidie.dangqun.mvp.presenter.home.OnlineOrderPresenter
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.RegexUtils
import kotlinx.android.synthetic.main.activity_online_order.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/09/10 13:41
 * description  在线下单
 */
class OnlineOrderActivity : BaseActivity(),OnlineOrderContract.View {

    private val mPresenter by lazy { OnlineOrderPresenter() }
    private var mId: Int? = null
    private var title: String? = null
    private var pvTime: TimePickerView? = null
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_online_order

    override fun initData() {
        title = intent.getStringExtra(Constants.TEXT)
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = title

        val selectedDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.set(2100, 11, 31)
        pvTime = TimePickerBuilder(this) { date, _ ->
            tv_time.text = DateUtils.dateToString(date)
        }.setType(booleanArrayOf(true, true, true, true, true, true))     // 默认全部显示
                .setDate(selectedDate)
                .setRangDate(selectedDate, endDate)
                .isDialog(true)
                .build()
        val mDialog = pvTime!!.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM)
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime!!.dialogContainerLayout.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
            }
        }

        tv_time.setOnClickListener {
            closeKeyboard(et_username,this)
            tv_time.requestFocus()
            et_username.clearFocus()
            et_phone.clearFocus()
            et_content.clearFocus()
            et_address.clearFocus()
            //弹出时间选择
            if (pvTime != null) {
                pvTime!!.show(tv_time)
            }
        }
        tv_submit_service.setOnClickListener {
            CircularAnim.hide(tv_submit_service)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
            closeKeyboard(et_username,this)
            val username = et_username.text.toString()
            if (username.isEmpty()) {
                showShort("请输入称呼")
                showSubmit()
                return@setOnClickListener
            }
            val phone = et_phone.text.toString()
            if (phone.isEmpty()) {
                showShort("请输入手机号")
                showSubmit()
                return@setOnClickListener
            }
            val time = tv_time.text.toString()
            if (time.isEmpty()) {
                showShort("请选择上门服务时间")
                showSubmit()
                return@setOnClickListener
            }
            val address = et_address.text.toString()
            if (address.isEmpty()) {
                showShort("请输入服务地址")
                showSubmit()
                return@setOnClickListener
            }
            val content = et_content.text.toString()
            if (content.isEmpty()) {
                showShort("请输入备注")
                showSubmit()
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                showShort("手机格式错误")
                showSubmit()
                return@setOnClickListener
            }
//        http://192.168.3.3/dangqun_backend_mayun/public/api/service/create_order
            mPresenter.getCreateOrderData( uid,mId!!, username, phone, content, time,address, token)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showSubmit() {
        CircularAnim.show(tv_submit_service).go()
        progress_bar.visibility = View.GONE
    }
    override fun start() {}
    override fun setCreateOrderData(isSuccess: Boolean,msg : String) {
        //跳转到订单完成页面
        if (isSuccess) {
            val intent = Intent(this@OnlineOrderActivity, OrderCompletedActivity::class.java)
            intent.putExtra(Constants.ID, mId)
            startActivity(intent)
        } else {
            showShort(msg)
        }
        showSubmit()
    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}