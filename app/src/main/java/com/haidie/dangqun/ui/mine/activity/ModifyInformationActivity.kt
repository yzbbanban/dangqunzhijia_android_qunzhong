package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.glide.GlideApp
import com.haidie.dangqun.mvp.contract.mine.ModifyInformationContract
import com.haidie.dangqun.mvp.event.MineEvent
import com.haidie.dangqun.mvp.presenter.mine.ModifyInformationPresenter
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.ImageLoader
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_modify_information.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File
import java.util.*


/**
 * Created by admin2
 *  on 2018/08/23  10:03
 * description  修改个人信息
 */
class ModifyInformationActivity : BaseActivity(), ModifyInformationContract.View {

    private val mPresenter by lazy { ModifyInformationPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var mAvatar: String? = null
    private var mNickname: String? = null
    private var mGender: String? = null
    private var mBirthday: String? = null

    private var selectList: List<LocalMedia> = ArrayList()
    private val maxSelectNum = 1
    private var themeId: Int = 0
    private var mPvOptions: OptionsPickerView<String>? = null
    private var pvTime: TimePickerView? = null
    private var gender: Int? = null
    override fun getLayoutId(): Int = R.layout.activity_modify_information
    override fun initData() {
        mAvatar = intent.getStringExtra(Constants.AVATAR)
        mNickname = intent.getStringExtra(Constants.NICKNAME)
        mGender = intent.getStringExtra(Constants.GENDER)
        mBirthday = intent.getStringExtra(Constants.BIRTHDAY)
        gender = if ("男" == mGender) 1 else 0
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "我的资料"

        ImageLoader.loadCircle(this,mAvatar!!,iv_avatar)
        et_nickname.setText(mNickname)
        tv_gender.text = mGender
        tv_birthday.text = mBirthday

        themeId = R.style.picture_default_style
        ll_avatar.setOnClickListener {
            closeKeyboard(et_nickname, this@ModifyInformationActivity)
            et_nickname.clearFocus()
            //弹出相册选择
            showPictureChoose()
        }
        initGenderView()
        ll_gender.setOnClickListener {
            closeKeyboard(et_nickname, this@ModifyInformationActivity)
            et_nickname.clearFocus()
            //弹出性别选项
            if (mPvOptions != null) {
                mPvOptions!!.show()
            }
        }
        initTimePicker()
        ll_birthday.setOnClickListener {
            closeKeyboard(et_nickname, this@ModifyInformationActivity)
            et_nickname.clearFocus()
            //弹出生日选项
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        tv_modify.setOnClickListener {
            CircularAnim.hide(tv_modify)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go{ progress_bar.visibility = View.VISIBLE }
            when {
                et_nickname.text.isEmpty() -> {
                    showShort("昵称为空")
                    showModify()
                }
                tv_gender.text.isEmpty() -> {
                    showShort("未选择性别")
                    showModify()
                }
                tv_birthday.text.isEmpty() -> {
                    showShort("未选择生日")
                    showModify()
                }
                else -> {
                    var part : MultipartBody.Part? = null
                    if (!selectList.isEmpty()) {
                        val filePath = Constants.PATH_PIC + Constants.PIC_PNG
                        val oldFile = File(selectList[0].compressPath)
                        CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC_PNG)
                        val file = File(filePath)
                        val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                        part = MultipartBody.Part.createFormData(Constants.AVATAR, file.name, requestFile)
                    }
                    //调用修改用户个人中心接口
                    mPresenter.getModifyUserInfoData(toRequestBody("$uid"), toRequestBody(token), toRequestBody("$gender"),
                            toRequestBody(et_nickname.text.toString()), toRequestBody(tv_birthday.text.toString()), part)
                }
            }
        }
    }
    private fun initTimePicker() {
        val endDate = Calendar.getInstance()
        val nowTime = DateUtils.getNowTime()
        val strings = nowTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = strings[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        endDate.set(Integer.parseInt(calendar[0]), Integer.parseInt(calendar[1]) - 1,
                Integer.parseInt(calendar[2]))
        val start = Calendar.getInstance()
        start.set(1950, 0, 1)
        pvTime = TimePickerBuilder(this) { date, _ ->
            tv_birthday.text = DateUtils.dateToStr(date)
        }.isDialog(true)
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
    private fun initGenderView() {
        val data = ArrayList<String>()
        data.add("男")
        data.add("女")
        mPvOptions = OptionsPickerBuilder(this@ModifyInformationActivity, OnOptionsSelectListener { options1, _, _, _ ->
            val gender = data[options1]
            tv_gender.text = gender
            this.gender = when(options1){
                0 -> 1
                else -> 0
            }
        }).build()
        mPvOptions!!.setPicker(data)
    }
    private fun showPictureChoose() {
        PictureSelector.create(this@ModifyInformationActivity)
                .openGallery(PictureMimeType.ofImage())
                .theme(themeId)
                .maxSelectNum(maxSelectNum)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .isZoomAnim(true)
                .imageFormat(PictureMimeType.PNG)
                .compress(true)
                .synOrAsy(true)
                .glideOverride(160, 160)
                .selectionMedia(selectList)
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }
    override fun start() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    val media = selectList[0]
                    GlideApp.with(this)
                            .load(media.compressPath)
                            .placeholder(R.drawable.icon_default)
                            .dontAnimate()
                            .circleCrop()
                            .into(iv_avatar)
                }
            }
        }
    }
    override fun modifyUserInfoSuccess() {
        RxBus.getDefault().post(MineEvent())
        showShort("修改成功")
        onBackPressed()
    }
    private fun showModify() {
        CircularAnim.show(tv_modify).go()
        progress_bar.visibility = View.GONE
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        showModify()
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}