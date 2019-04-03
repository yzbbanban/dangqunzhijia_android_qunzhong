package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 18:30
 * description
 */
class VolunteerApplicationModel {
    fun getVolunteerApplicationData( uid: Int,  group_id: Int, username: String,gender: Int,  phone: String,  nation: String, birthday: String,
                                     face: Int, study: Int, identity: String,  company: String, address: String,  hobby: String,
                                    skill: String,  experience: String, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getVolunteerApplicationData(uid, group_id, username, gender, phone, nation, birthday, face, study, identity,
                company, address, hobby, skill, experience, token)
                .compose(SchedulerUtils.ioToMain())
    }
}