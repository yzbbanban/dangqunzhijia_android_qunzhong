package com.haidie.dangqun.ui.mine.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.mine.ReleaseVoteContract
import com.haidie.dangqun.mvp.event.ReleaseVoteEvent
import com.haidie.dangqun.mvp.presenter.mine.ReleaseVotePresenter
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.mine.adapter.GridImageAdapter
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_release_vote.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/09/17 13:50
 * description  发布投票
 */
class ReleaseVoteActivity : BaseActivity(),ReleaseVoteContract.View {

    private var selectList = mutableListOf<LocalMedia>()
    private val maxSelectNum = 1
    private var adapter: GridImageAdapter? = null
    private var themeId: Int = 0
    private var pvTime: TimePickerView? = null
    private var isStartTime = true
    private var type = -1
    private val mPresenter by lazy { ReleaseVotePresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun getLayoutId(): Int = R.layout.activity_release_vote
    override fun initData() {}
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "发布投票"
        initRecyclerView()
        initTimePicker()
        initLinearLayout()
        adapter!!.mItemClickListener = object : GridImageAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, v: View) {
                if (selectList.size > 0) {
                    PictureSelector.create(this@ReleaseVoteActivity).themeStyle(themeId).openExternalPreview(position, selectList)
                }
            }
        }
        rg_voting_type.setOnCheckedChangeListener{
            _, checkedId ->
            closeKeyboard(et_headline,this@ReleaseVoteActivity)
            clearFocus()
            rg_voting_type.requestFocus()
            //投票类型(0单选,1多选)
            when (checkedId) {
                R.id.rb_single_selection -> type = 0
                R.id.rb_multiple_selection -> type = 1
            }
        }
        tv_start_time.setOnClickListener {
            closeKeyboard(et_headline,this@ReleaseVoteActivity)
            clearFocus()
            tv_start_time.requestFocus()
            isStartTime = true
            initTimePicker()
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        tv_end_time.setOnClickListener {
            closeKeyboard(et_headline,this@ReleaseVoteActivity)
            clearFocus()
            tv_end_time.requestFocus()
            isStartTime = false
            initTimePicker()
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        tv_release.setOnClickListener {
            closeKeyboard(et_headline,this@ReleaseVoteActivity)
            clearFocus()
            tv_release.requestFocus()
            CircularAnim.hide(tv_release)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
            if (-1 == type) {
                showShort("请选择投票类型")
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
                showShort("请添加投票封面图")
                showRelease()
                return@setOnClickListener
            }
            val content = et_vote_description.text.toString()
            if (content.isEmpty()) {
                showShort("请输入投票描述")
                showRelease()
                return@setOnClickListener
            }
            val startTime = tv_start_time.text.toString()
            if (startTime.isEmpty()) {
                showShort("请选择投票开始时间")
                showRelease()
                return@setOnClickListener
            }
            val endTime = tv_end_time.text.toString()
            if (endTime.isEmpty()) {
                showShort("请选择投票结束时间")
                showRelease()
                return@setOnClickListener
            }
            var choose = ""
            val count = linear_layout_choose.childCount
            for (i in 0 until count) {
                val childAt = linear_layout_choose.getChildAt(i)
                val string = (childAt.findViewById(R.id.et_option) as EditText).text.toString()
                if (!TextUtils.isEmpty(string)) {
                    choose += "$string,"
                }
            }
            if (TextUtils.isEmpty(choose)) {
                showShort("请输入选项内容")
                showRelease()
                return@setOnClickListener
            }
            if (choose.endsWith(",")) {
                choose = choose.substring(0, choose.length - 1)
            }

            val filePath = Constants.PATH_PIC + Constants.PIC_PNG
            val oldFile = File(selectList[0].compressPath)
            CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC_PNG)
            val file = File(filePath)
            val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
            val part = MultipartBody.Part.createFormData(Constants.PIC, file.name, requestFile)
            mPresenter.getReleaseVoteData(toRequestBody(uid.toString()),toRequestBody(token),toRequestBody( "$type"), toRequestBody(title),
                    toRequestBody(content),toRequestBody(startTime),toRequestBody(endTime), toRequestBody(choose),part)
        }
    }

    private fun showRelease() {
        CircularAnim.show(tv_release).go()
        progress_bar.visibility = View.GONE
    }
    private fun initRecyclerView() {
        themeId = R.style.picture_default_style
        adapter = GridImageAdapter(this@ReleaseVoteActivity,onAddPicClickListener)
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recycler_view.layoutManager = GridLayoutManager(this@ReleaseVoteActivity, 4)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
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
        pvTime = TimePickerBuilder(this) { date, _ ->
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

    @SuppressLint("SetTextI18n")
    private fun initLinearLayout() {
        val view = LayoutInflater.from(this).inflate(R.layout.linear_layout_choose_item, linear_layout_choose,false)
        val childCount = linear_layout_choose.childCount
        val tvOption = view.findViewById<TextView>(R.id.tv_option)
        tvOption.text = "选项" + (childCount + 1)
        val tvAdd = view.findViewById<TextView>(R.id.tv_add)
        val tvDelete = view.findViewById<TextView>(R.id.tv_delete)
        tvAdd.setOnClickListener {
            if (linear_layout_choose.childCount == 15){
                showShort("已经添加到最大限制")
                return@setOnClickListener
            }
            initLinearLayout()
        }
        tvDelete.setOnClickListener {
            if (linear_layout_choose.childCount == 1){
                tvDelete.visibility = View.GONE
                return@setOnClickListener
            }
            linear_layout_choose.removeView(it.parent.parent as View)
            val count = linear_layout_choose.childCount
            for (i in 0 until count) {
                val childAt = linear_layout_choose.getChildAt(i)
                (childAt.findViewById(R.id.tv_option) as TextView).text = "选项" + (i + 1)
            }
        }
        linear_layout_choose.addView(view)
        if (linear_layout_choose.childCount == 1) {
            tvDelete.visibility = View.GONE
        }
    }
    private val onAddPicClickListener = object : GridImageAdapter.OnAddPicClickListener {
        override fun onAddPicClick() {
            closeKeyboard(et_headline,this@ReleaseVoteActivity)
            clearFocus()
            PictureSelector.create(this@ReleaseVoteActivity)
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
        et_vote_description.clearFocus()
        linear_layout_choose.clearFocus()
    }
    override fun start() {}
    override fun setReleaseVoteData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            RxBus.getDefault().post(ReleaseVoteEvent())
            showShort("发布成功")
            //返回到上级页面
            onBackPressed()
        }else{
            showShort("发布失败")
            showRelease()
        }
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        showRelease()
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}