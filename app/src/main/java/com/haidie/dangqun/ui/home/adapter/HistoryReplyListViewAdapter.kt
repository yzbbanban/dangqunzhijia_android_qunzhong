package com.haidie.dangqun.ui.home.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.HistoryReplayData

/**
 * Create by   Administrator
 *      on     2018/09/08 13:49
 * description
 */
class HistoryReplyListViewAdapter(context : Context,list : List<HistoryReplayData.HistoryReplayItemData> ) : BaseAdapter() {
    private var mContext: Context = context
    private var mList: List<HistoryReplayData.HistoryReplayItemData> = list
    private var mLoginUsername: String? = null
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(mContext)
        val holder: ViewHolder?
        var convertView = view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_reply_item, parent,false)
            holder = ViewHolder()
            holder.username = convertView.findViewById(R.id.tv_username)
            holder.createTime = convertView.findViewById(R.id.tv_create_time)
            holder.content = convertView.findViewById(R.id.tv_content)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val historyReplayItemData = mList[position]
        val username = historyReplayItemData.username
        if (mLoginUsername == username) {
            holder.username!!.setTextColor(Color.parseColor(Constants.F_5_F_5_F_5))
            holder.content!!.setTextColor(Color.parseColor(Constants.F_5_F_5_F_5))
        } else {
            holder.username!!.setTextColor(ContextCompat.getColor(mContext,R.color.black))
            holder.content!!.setTextColor(ContextCompat.getColor(mContext,R.color.black))
            val paint = holder.content!!.paint
            paint.isFakeBoldText = true
        }
        holder.username!!.text = username
        holder.createTime!!.text = historyReplayItemData.create_time
        holder.content!!.text = historyReplayItemData.content
        return convertView!!
    }
    override fun getItem(position: Int): Any {
        return mList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return mList.size
    }
    internal class ViewHolder {
        var username: TextView? = null
        var createTime: TextView? = null
        var content: TextView? = null
    }
    fun setLoginUsername(loginUsername: String) {
        mLoginUsername = loginUsername
    }
}