package com.haidie.dangqun.ui.mine.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
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
import com.haidie.dangqun.mvp.contract.mine.ReleaseActivitiesContract
import com.haidie.dangqun.mvp.event.ReleaseActivitiesEvent
import com.haidie.dangqun.mvp.model.bean.ProvinceCityAreaData
import com.haidie.dangqun.mvp.presenter.mine.ReleaseActivitiesPresenter
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.mine.adapter.GridImageAdapter
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_release_activities.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/09/14 16:20
 * description  发布活动
 */
class ReleaseActivitiesActivity : BaseActivity(),ReleaseActivitiesContract.View {

    private var mData: MutableList<String>? = null
    private var pvOptions: OptionsPickerView<String>? = null
    private var selectList = mutableListOf<LocalMedia>()
    private val maxSelectNum = 1
    private var adapter: GridImageAdapter? = null
    private var themeId: Int = 0
    private var pvTime: TimePickerView? = null
    private var isStartTime = true
    private var type: Int = 0
    private val mPresenter by lazy { ReleaseActivitiesPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_release_activities

    override fun initData() {
        mData = ArrayList()
        mData!!.add("党务活动")
        mData!!.add("党员活动")
    }

    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "发布活动"

        initRecyclerView()
        initTimePicker()
        mPresenter.getJson(this)

        tv_activity_type.setOnClickListener {
            closeKeyboard(et_headline,this)
            clearFocus()
            tv_activity_type.requestFocus()
            pvOptions = OptionsPickerBuilder(this) { options1, _, _, _ ->
                type = options1 + 1
                tv_activity_type.text = mData!![options1]
            }.build()
            pvOptions!!.setPicker(mData)
            pvOptions!!.show()
        }

        adapter!!.mItemClickListener = object : GridImageAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, v: View) {
                if (selectList.size > 0) {
                    PictureSelector.create(this@ReleaseActivitiesActivity).themeStyle(themeId).openExternalPreview(position, selectList)
                }
            }
        }

        tv_start_time.setOnClickListener {
            closeKeyboard(et_headline,this)
            clearFocus()
            tv_start_time.requestFocus()
            isStartTime = true
            initTimePicker()
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        tv_end_time.setOnClickListener {
            closeKeyboard(et_headline,this)
            clearFocus()
            tv_end_time.requestFocus()
            isStartTime = false
            initTimePicker()
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        tv_area.setOnClickListener {
            closeKeyboard(et_headline,this)
            clearFocus()
            tv_area.requestFocus()
            if (mPvOptions != null) {
                mPvOptions!!.show()
            }
        }

        tv_release.setOnClickListener {
            closeKeyboard(et_headline,this)
            clearFocus()
            tv_release.requestFocus()
            CircularAnim.hide(tv_release)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
            if (tv_activity_type.text.toString().isEmpty()) {
                showShort("请选择活动类型")
                showRelease()
                return@setOnClickListener
            }
            val title = et_headline.text.toString()
            if (title.isEmpty()) {
                showShort("请输入标题")
                showRelease()
                return@setOnClickListener
            }
            if (selectList.size == 0) {
                showShort("请添加活动封面图")
                showRelease()
                return@setOnClickListener
            }
            val content = et_activity_description.text.toString()
            if (TextUtils.isEmpty(content)) {
                showShort("请输入活动描述")
                showRelease()
                return@setOnClickListener
            }
            val startTime = tv_start_time.text.toString()
            if (startTime.isEmpty()) {
                showShort("请选择活动开始时间")
                showRelease()
                return@setOnClickListener
            }
            val endTime = tv_end_time.text.toString()
            if (endTime.isEmpty()) {
                showShort("请选择活动结束时间")
                showRelease()
                return@setOnClickListener
            }
            val area = tv_area.text.toString()
            if (area.isEmpty()) {
                showShort("选择活动地区")
                showRelease()
                return@setOnClickListener
            }
            val address = et_address.text.toString()
            if (address.isEmpty()) {
                showShort("请输入活动详细地址")
                showRelease()
                return@setOnClickListener
            }
            val filePath = Constants.PATH_PIC + Constants.PIC_PNG
            val oldFile = File(selectList[0].compressPath)
            CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC_PNG)
            val file = File(filePath)
            val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
            val part = MultipartBody.Part.createFormData(Constants.PIC, file.name, requestFile)

            mPresenter.getReleaseActivitiesData(toRequestBody(uid.toString()),toRequestBody(type.toString()),
                    toRequestBody(title),toRequestBody(content),toRequestBody(startTime),toRequestBody(endTime),
                    toRequestBody(area),toRequestBody(address),toRequestBody(token),part)
        }
    }

    private fun showRelease() {
        CircularAnim.show(tv_release).go()
        progress_bar.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun initRecyclerView() {
        themeId = R.style.picture_default_style
        adapter = GridImageAdapter(this@ReleaseActivitiesActivity, onAddPicClickListener)
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recycler_view.layoutManager = GridLayoutManager(this@ReleaseActivitiesActivity, 4)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
    }
    private val onAddPicClickListener = object : GridImageAdapter.OnAddPicClickListener {
        override fun onAddPicClick() {
            closeKeyboard(et_headline,this@ReleaseActivitiesActivity)
            clearFocus()
            PictureSelector.create(this@ReleaseActivitiesActivity)
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
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    adapter!!.setList(selectList)
                    adapter!!.notifyDataSetChanged()
                }
            }
        }
    }
    private fun clearFocus() {
        et_headline.clearFocus()
        et_activity_description.clearFocus()
        et_address.clearFocus()
    }
    private fun initTimePicker() {
        val start = Calendar.getInstance()
        val nowTime = DateUtils.getNowTime()
        val strings = nowTime.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val calendar = strings[0].split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val split = strings[1].split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        start.set(Integer.parseInt(calendar[0]), Integer.parseInt(calendar[1]) - 1,
                if (isStartTime) Integer.parseInt(calendar[2]) else Integer.parseInt(calendar[2]) + 1,
                Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]))
        val endDate = Calendar.getInstance()
        endDate.set(2100, 11, 31)
        pvTime = TimePickerBuilder(this) {
            date, _ ->
            if (isStartTime) {
                tv_start_time.text = DateUtils.dateToString(date)
            } else {
                tv_end_time.text = DateUtils.dateToString(date)
            }
        }.isDialog(true)
                .setDate(start)
                .setRangDate(start, endDate)
                .setType(booleanArrayOf(true, true, true, true, true, true))
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
    override fun start() {}
    private var mPvOptions: OptionsPickerView<String>? = null
    override fun setJson(provinceCityAreaData: ProvinceCityAreaData) {
        val province = provinceCityAreaData.province
        val selectProvince = mutableListOf<String>()
        province!!.forEach {
            selectProvince.add(it.name)
        }
        val city = provinceCityAreaData.city
        val area = provinceCityAreaData.area
        //返回的分别是三个级别的选中位置
        //设置选中项文字颜色
        mPvOptions = OptionsPickerBuilder(this) { options1, options2, options3, _ ->
            //返回的分别是三个级别的选中位置
            val tx = selectProvince[options1] +
                    city!![options1][options2] +
                    area!![options1][options2][options3]
            tv_area.text = tx
        }.setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build<String>()
        mPvOptions!!.setPicker(selectProvince, city, area)//三级选择器
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
    override fun setReleaseActivitiesData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            RxBus.getDefault().post(ReleaseActivitiesEvent())
            showShort("发布成功")
            //返回到上级页面
            onBackPressed()
        }else{
            showShort(msg)
            showRelease()
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        showRelease()
    }
}