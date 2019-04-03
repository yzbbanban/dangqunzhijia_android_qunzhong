package com.haidie.dangqun.ui.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.VoteDetailData

/**
 * Create by   Administrator
 *      on     2018/09/12 14:49
 * description
 */
class VoteDetailListViewAdapter(context : Activity, data: MutableList<VoteDetailData.VoteDetailListItemData>, total : Int) : BaseAdapter() {
    private var mActivity = context
    private var mData: List<VoteDetailData.VoteDetailListItemData> = data
    private var totalVote: Int = total
    private var isCheck = SparseBooleanArray()
    private var isSingleChoice: Boolean = false
    private var tempPosition = -1  //记录已经点击的CheckBox的位置
    override fun getCount(): Int {
        return mData.size
    }
    override fun getItem(position: Int): Any {
        return mData[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setTotalVote(total: Int) {
        totalVote = total
    }

    //返回当前CheckBox选中的位置,便于获取值.
    fun getSelectPosition(): Int {
        return tempPosition
    }
    fun getMap(): SparseBooleanArray {
        return isCheck
    }
    internal inner class ViewHolder {
        var mTvContent: TextView? = null
        var mTvNum: TextView? = null
        var cbVote: CheckBox? = null
        var mProgressBar: ProgressBar? = null
        var tvPercent: TextView? = null
    }
    fun setSingleChoiceOrMultipleChoice(isSingle: Boolean) {
        isSingleChoice = isSingle
    }
    fun getChoiceMode(): Boolean {
        return isSingleChoice
    }
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(mActivity)
        val viewHolder: ViewHolder
        var convertView = view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vote_detail_list_view_item,parent,false)
            viewHolder = ViewHolder()
            viewHolder.mTvContent = convertView.findViewById(R.id.tv_content)
            viewHolder.mTvNum = convertView.findViewById(R.id.tv_num)
            viewHolder.cbVote = convertView.findViewById(R.id.cb_vote)
            viewHolder.mProgressBar = convertView.findViewById(R.id.progress_bar)
            viewHolder.tvPercent = convertView.findViewById(R.id.tv_percent)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val voteDetailListItemData = mData[position]
        viewHolder.mTvContent!!.text = (position + 1).toString() + "." + voteDetailListItemData.content
        val num = voteDetailListItemData.num
        if (totalVote != 0) {
            val percent = 100 * num / totalVote
            viewHolder.mProgressBar!!.progress = percent
            viewHolder.mTvNum!!.text = "已投票：" + num + "票"
            viewHolder.tvPercent!!.text = "$percent%"
        } else {
            viewHolder.mTvNum!!.text = "0票"
            viewHolder.tvPercent!!.text = "0%"
        }
        viewHolder.cbVote!!.id = position    //设置当前position为CheckBox的id
        // 勾选框的点击事件
        viewHolder.cbVote!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isSingleChoice) {
                if (isChecked) {
                    if (tempPosition != -1) {
                        //根据id找到上次点击的CheckBox,将它设置为false.
                        val tempCheckBox = mActivity.findViewById<CheckBox>(tempPosition)
                        if (tempCheckBox != null) {
                            tempCheckBox.isChecked = false
                        }
                    }
                    //保存当前选中CheckBox的id值
                    tempPosition = buttonView.id
                } else {    //当CheckBox被选中,又被取消时,将tempPosition重新初始化.
                    tempPosition = -1
                }
                return@setOnCheckedChangeListener
            }
            isCheck.put(position, isChecked)
        }
        if (isSingleChoice) {
            viewHolder.cbVote!!.isChecked = position == tempPosition
        } else {
            viewHolder.cbVote!!.isChecked = isCheck.get(position)
        }
        return convertView!!
    }
}