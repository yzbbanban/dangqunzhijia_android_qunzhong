package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.OnlineHelpFormSubmissionContract
import com.haidie.dangqun.mvp.model.bean.BlockInfoData
import com.haidie.dangqun.mvp.model.bean.HouseListData
import com.haidie.dangqun.mvp.model.bean.UnitListData
import com.haidie.dangqun.mvp.presenter.home.OnlineHelpFormSubmissionPresenter
import com.haidie.dangqun.ui.home.view.BlockUnitHousePopupWindow
import com.haidie.dangqun.ui.main.activity.MainActivity
import com.haidie.dangqun.ui.mine.adapter.GridImageAdapter
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.RegexUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_online_help_form_submission.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/09/12 16:26
 * description  网上求助表单提交
 */
class OnlineHelpFormSubmissionActivity : BaseActivity(),OnlineHelpFormSubmissionContract.View {

    private val mPresenter by lazy { OnlineHelpFormSubmissionPresenter() }
    private var selectList = mutableListOf<LocalMedia>()
    private val maxSelectNum = 3
    private var adapter: GridImageAdapter? = null
    private var themeId: Int = Constants.NEGATIVE_ONE
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var gender = -1          //性别,0表示女,1表示男
    private var troubleType = -1     //困难类型0.其他, 1.因病,2.因残,3.因学,4.因灾,5.因技术,6.缺劳动力,7.缺资金
    private val isOnline: String = "1"  //是否是智慧互助-网上求助,0否 1是,这里填1
    private var mPvOptions: OptionsPickerView<String>? = null
    private  var blockUnitHousePopupWindow: BlockUnitHousePopupWindow? = null
    override fun getLayoutId(): Int = R.layout.activity_online_help_form_submission
    override fun initData() {}


    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "网上求助"

        initRecyclerView()
        initTypeView()
        rgGender.setOnCheckedChangeListener{ _, checkedId ->
            rgGender.requestFocus()
            clearFocus()
            when (checkedId) {
                R.id.rbMale -> gender = 1
                R.id.rbFemale -> gender = 0
            }
        }

        llHelpType.setOnClickListener {
            //  弹出类型选项
            clearFocus()
            if (mPvOptions != null) {
                mPvOptions!!.show()
            }
        }
        blockUnitHousePopupWindow = BlockUnitHousePopupWindow(this@OnlineHelpFormSubmissionActivity,nsv)

        etAddress.setOnClickListener {
            //  弹出楼栋单元房间选项
            mPresenter.getBlockData(uid, token)
//            mPresenter.getHouseListData(uid, token)
        }
        tv_submit_report.setOnClickListener {
            CircularAnim.hide(tv_submit_report)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go { progress_bar.visibility = View.VISIBLE }
            val title = etTitle.text.toString()
            if (title.isEmpty()) {
                showShort("请输入标题")
                showSubmit()
                return@setOnClickListener
            }
            val description = etDescription.text.toString()
            if (description.isEmpty()) {
                showShort("请输入求助内容")
                showSubmit()
                return@setOnClickListener
            }
            val username = etName.text.toString()
            if (username.isEmpty()) {
                showShort("请输入求助人")
                showSubmit()
                return@setOnClickListener
            }
            val phone = etPhone.text.toString()
            if (phone.isEmpty()) {
                showShort("请输入手机号")
                showSubmit()
                return@setOnClickListener
            }
            if (gender == -1) {
                showShort("请选择性别")
                showSubmit()
                return@setOnClickListener
            }
            val identity = etIdentity.text.toString()
            if (identity.isEmpty()) {
                showShort("请输入身份证")
                showSubmit()
                return@setOnClickListener
            }
            if (troubleType == -1) {
                showShort("请选择求助类型")
                showSubmit()
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                showShort("手机格式错误")
                showSubmit()
                return@setOnClickListener
            }
            if (!RegexUtils.isIDCard18(identity)) {
                showShort("身份证格式错误")
                showSubmit()
                return@setOnClickListener
            }
            var parts : List<MultipartBody.Part>? = null
            if (selectList.size != 0) {
                val builder = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                for (i in selectList.indices) {
                    val filePath = Constants.PATH_PIC + Constants.PIC + (i + 1) + Constants.PNG
                    val oldFile = File(selectList[i].compressPath)
                    CommonUtils.copyFile(oldFile, Constants.PATH_PIC, Constants.PIC + (i + 1) + Constants.PNG)
                    val file = File(filePath)
                    val imageBody = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                    builder.addFormDataPart(Constants.PIC + (i + 1), file.name, imageBody)
                }
                parts = builder.build().parts()
            }

//           http://192.168.3.3/dangqun_backend_mayun/public/api/Helps/create
            mPresenter.getOnlineHelpFormSubmissionData(toRequestBody(uid.toString()),toRequestBody(token),null,toRequestBody(troubleType.toString()),
                    toRequestBody(isOnline),toRequestBody(username),toRequestBody(gender.toString()),toRequestBody(phone),toRequestBody(identity),
                    toRequestBody(etAddress.text.toString()),toRequestBody(title),toRequestBody(description),parts)
        }
    }
    private fun initTypeView() {
        val data = ArrayList<String>()
        data.add("其他")
        data.add("因病")
        data.add("因残")
        data.add("因学")
        data.add("因灾")
        data.add("因技术")
        data.add("缺劳动力")
        data.add("缺资金")
        mPvOptions = OptionsPickerBuilder(this@OnlineHelpFormSubmissionActivity, OnOptionsSelectListener { options1, _, _, _ ->
            val type = data[options1]
            troubleType = options1
            tvHelpType.text = type
        }).build()
        mPvOptions!!.setPicker(data)
    }
    private fun showSubmit() {
        CircularAnim.show(tv_submit_report).go()
        progress_bar.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun initRecyclerView() {
        themeId = R.style.picture_default_style
        adapter = GridImageAdapter(this@OnlineHelpFormSubmissionActivity, onAddPicClickListener)
        adapter!!.setList(selectList)
        adapter!!.setSelectMax(maxSelectNum)
        recycler_view.layoutManager = GridLayoutManager(this@OnlineHelpFormSubmissionActivity, 4)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
        adapter!!.mItemClickListener =  object : GridImageAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, v: View) {
                if (selectList.size > 0) {
                    PictureSelector.create(this@OnlineHelpFormSubmissionActivity).themeStyle(themeId).openExternalPreview(position, selectList)
//                    禁用动画
                    overridePendingTransition(0, 0)
                }
            }
        }
    }
    private val onAddPicClickListener = object : GridImageAdapter.OnAddPicClickListener{
        override fun onAddPicClick() {
            clearFocus()
            PictureSelector.create(this@OnlineHelpFormSubmissionActivity)
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
        etName.clearFocus()
        etPhone.clearFocus()
        closeKeyboard(etName, this)
    }
    override fun start() {}
    override fun showLoading() {}
    override fun dismissLoading() {}
    override fun setOnlineHelpFormSubmissionData(isSuccess: Boolean,msg : String) {
        if (isSuccess) {
            showShort("提交成功")
            val intent = Intent(this@OnlineHelpFormSubmissionActivity, MainActivity::class.java)
            intent.putExtra(Constants.TAB,3)
            intent.putExtra(Constants.IS_REPORT_OR_HELP,2)
            startActivity(intent)
        }else{
            showShort(msg)
            showSubmit()
        }
    }
    override fun setBlockListData(blockInfoData: ArrayList<BlockInfoData>) {
        if (blockInfoData.isEmpty()) {
            showShort("暂无楼栋信息")
            return
        }
        blockUnitHousePopupWindow?.setBlockAdapter(blockInfoData)
    }
    fun getUnitListData(uid: Int, token: String, title: String){
        mPresenter.getUnitListData(uid, token, title)
    }
    override fun setUnitListData(unitListData: ArrayList<UnitListData>) {
        if (unitListData.isEmpty()) {
            showShort("暂无单元信息")
            blockUnitHousePopupWindow?.clearUnit()
            blockUnitHousePopupWindow?.clearHouse()
            return
        }
        blockUnitHousePopupWindow?.setUnitAdapter(unitListData)
    }
    fun getHouseListData(uid: Int, token: String, title: String, unit: String){
        mPresenter.getHouseListData(uid, token, title, unit)
    }
    override fun setHouseListData(houseListData: ArrayList<HouseListData>) {
        if (houseListData.isEmpty()) {
            showShort("暂无房间信息")
            blockUnitHousePopupWindow?.clearHouse()
            return
        }
        blockUnitHousePopupWindow?.setHouseAdapter(houseListData)
        if (!blockUnitHousePopupWindow!!.isShowing) {
            blockUnitHousePopupWindow?.showPopupWindow()
        }
    }
    fun getBlockUnitHouse(blockUnitHouse : String){
        etAddress.text = blockUnitHouse
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
}