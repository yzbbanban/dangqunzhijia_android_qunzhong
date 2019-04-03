package com.haidie.dangqun.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.haidie.dangqun.R

/**
 * Create by   Administrator
 *      on     2018/08/30 15:05
 * description
 */
class DatePickerDialog(activity: Activity, theme: Int,viewGroup: ViewGroup, callBack: OnDateSetListener, year: Int, monthOfYear: Int,
                       dayOfMonth: Int) : AlertDialog(activity, theme), DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

    private val startYear = "start_year"
    private val startMonth = "start_month"
    private val startDay = "start_day"

    private var mDatePickerStart: DatePicker? = null
    private var mCallBack: OnDateSetListener? = null

    init {
        mCallBack = callBack
        setButton(DialogInterface.BUTTON_POSITIVE, "确 定", this)
        setButton(DialogInterface.BUTTON_NEGATIVE, "取 消", this)
        setIcon(0)

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_date_picker, viewGroup,false)
        setView(view)
        mDatePickerStart = view.findViewById(R.id.datePickerStart)
        mDatePickerStart!!.init(year, monthOfYear, dayOfMonth, this)
//        设置NumberPicker，中间的EditView不可以点击
        mDatePickerStart!!.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
        hideDay(mDatePickerStart!!)
    }

    private fun hideDay(mDatePicker: DatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
                if (daySpinnerId != 0) {
                    val daySpinner = mDatePicker.findViewById<View>(daySpinnerId)
                    if (daySpinner != null) {
                        daySpinner.visibility = View.GONE
                    }
                }
            } else {
                val datePickerFields = mDatePicker.javaClass.declaredFields
                for (datePickerField in datePickerFields) {
                    if ("mDaySpinner" == datePickerField.name || "mDayPicker" == datePickerField.name) {
                        datePickerField.isAccessible = true
                        var dayPicker = Any()
                        try {
                            dayPicker = datePickerField.get(mDatePicker)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }
                        (dayPicker as View).visibility = View.GONE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun setMinDate(minDate: Long) {
        mDatePickerStart!!.minDate = minDate
    }
    fun setMaxDate(maxDate: Long) {
        mDatePickerStart!!.maxDate = maxDate
    }
    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE)
            tryNotifyDateSet()
    }
    private fun tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePickerStart!!.clearFocus()
            mCallBack!!.onDateSet(mDatePickerStart!!, mDatePickerStart!!.year, mDatePickerStart!!.month,
                    mDatePickerStart!!.dayOfMonth)
        }
    }
    override fun onDateChanged(view: DatePicker?, year: Int, month: Int, day: Int) {
        if (view!!.id == R.id.datePickerStart)
            mDatePickerStart!!.init(year, month, day, this)
    }
    interface OnDateSetListener {
        fun onDateSet(startDatePicker: DatePicker, startYear: Int, startMonthOfYear: Int, startDayOfMonth: Int)
    }
    override fun onSaveInstanceState(): Bundle {
        val state = super.onSaveInstanceState()
        state.putInt(startYear, mDatePickerStart!!.year)
        state.putInt(startMonth, mDatePickerStart!!.month)
        state.putInt(startDay, mDatePickerStart!!.dayOfMonth)
        return state
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val startYear = savedInstanceState.getInt(startYear)
        val startMonth = savedInstanceState.getInt(startMonth)
        val startDay = savedInstanceState.getInt(startDay)
        mDatePickerStart!!.init(startYear, startMonth, startDay, this)
    }
}