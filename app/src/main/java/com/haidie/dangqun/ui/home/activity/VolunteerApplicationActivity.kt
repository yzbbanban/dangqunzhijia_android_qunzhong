package com.haidie.dangqun.ui.home.activity

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VolunteerApplicationContract
import com.haidie.dangqun.mvp.event.VolunteerApplicationEvent
import com.haidie.dangqun.mvp.presenter.home.VolunteerApplicationPresenter
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.RegexUtils
import kotlinx.android.synthetic.main.activity_volunteer_application.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/09/11 18:23
 * description  志愿者申请
 */
class VolunteerApplicationActivity : BaseActivity(),VolunteerApplicationContract.View {

    private var mId: Int? = null
    private var gender = -1
    private var mData: List<String>? = null
    private var mDataFace: List<String>? = null
    private var study: Int = 0
    private var pvTime: TimePickerView? = null
    private var pvOptions: OptionsPickerView<String>? = null
    private var face: Int = 0
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private val mPresenter by lazy { VolunteerApplicationPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_volunteer_application

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,Constants.NEGATIVE_ONE)

        //    学历,0.保密,1小学,2初中,3高中,4.专科,5本科,6研究生
        mData = mutableListOf("保密", "小学", "初中", "高中", "专科", "本科", "研究生")
//        政治面貌0.群众,1团员,2党员
        mDataFace = mutableListOf("群众", "团员", "党员")
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "志愿者申请"

        initTimePicker()
        rg_gender.setOnCheckedChangeListener{
            _, checkedId ->
            //            性别,0表示女,1表示男
            when (checkedId) {
                R.id.rb_male -> gender = 1
                R.id.rb_female -> gender = 0
            }
        }
        tv_study.setOnClickListener {
            closeKeyboard(et_username,this)
            clearFocus()
            tv_study.requestFocus()
            pvOptions = OptionsPickerBuilder(this@VolunteerApplicationActivity) {
                options1, _, _, _ ->
                study = options1
                tv_study.text = mData!![options1]
            }.build()
            pvOptions!!.setPicker(mData)
            pvOptions!!.show()
        }
        tv_birthday.setOnClickListener {
            closeKeyboard(et_username,this)
            clearFocus()
            tv_birthday.requestFocus()
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        tv_face.setOnClickListener {
            closeKeyboard(et_username,this)
            clearFocus()
            tv_face.requestFocus()
            val build = OptionsPickerBuilder(this@VolunteerApplicationActivity) {
                options1, _, _, _ ->
                face = options1
                tv_face.text = mDataFace!![options1]
            }.build<String>()
            build.setPicker(mDataFace)
            build.show()
        }
        tv_submit_text.setOnClickListener {
            closeKeyboard(et_username,this)
            clearFocus()
            tv_submit_text.requestFocus()
            CircularAnim.hide(tv_submit_text)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
            val username = et_username.text.toString()
            if (username.isEmpty()) {
                showShort("请输入姓名")
                showSubmit()
                return@setOnClickListener
            }
            if (-1 == gender) {
                showShort("请选择性别")
                showSubmit()
                return@setOnClickListener
            }
            val identity = et_identity.text.toString()
            if (identity.isEmpty()) {
                showShort("请输入身份证")
                showSubmit()
                return@setOnClickListener
            }
            val address = et_address.text.toString()
            if (address.isEmpty()) {
                showShort("请输入家庭地址")
                showSubmit()
                return@setOnClickListener
            }
            val company = et_company.text.toString()
            if (company.isEmpty()) {
                showShort("请输入工作单位")
                showSubmit()
                return@setOnClickListener
            }
            val skill = et_skill.text.toString()
            if (skill.isEmpty()) {
                showShort("请输入专业技能")
                showSubmit()
                return@setOnClickListener
            }
            val hobby = et_hobby.text.toString()
            if (hobby.isEmpty()) {
                showShort("请输入个人爱好")
                showSubmit()
                return@setOnClickListener
            }
            val experience = et_experience.text.toString()
            if (experience.isEmpty()) {
                showShort("请输入志愿服务经历")
                showSubmit()
                return@setOnClickListener
            }
            if (!RegexUtils.isIDCard18(identity)) {
                showShort("身份证格式错误")
                showSubmit()
                return@setOnClickListener
            }
            val phone = et_phone.text.toString()
            if (phone.isNotEmpty() && !RegexUtils.isMobileSimple(phone)) {
                showShort("手机格式错误")
                showSubmit()
                return@setOnClickListener
            }
//          http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/add
            mPresenter.getVolunteerApplicationData(uid,mId!!,username,gender,phone,et_nation.text.toString(),tv_birthday.text.toString(),
                    face,study,identity,company,address,hobby,skill,experience,token)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showSubmit() {
        CircularAnim.show(tv_submit_text).go()
        progress_bar.visibility = View.GONE
    }
    private fun initTimePicker() {
        val start = Calendar.getInstance()
        start.set(1900, 0, 1)
        val endDate = Calendar.getInstance()
        pvTime = TimePickerBuilder(this) { date, _ -> tv_birthday.text = DateUtils.dateToString(date).split(" ")[0] }.isDialog(true)
                .setDate(start)
                .setRangDate(start, endDate)
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
    }
    private fun clearFocus() {
        et_username.clearFocus()
        et_nation.clearFocus()
        et_phone.clearFocus()
        et_identity.clearFocus()
        et_address.clearFocus()
        et_company.clearFocus()
        et_skill.clearFocus()
        et_hobby.clearFocus()
        et_experience.clearFocus()
    }
    override fun start() {}
    override fun setVolunteerApplicationData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            RxBus.getDefault().post(VolunteerApplicationEvent())
            showShort("提交成功")
            onBackPressed()
        } else {
            showShort(msg)
            showSubmit()
        }
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}