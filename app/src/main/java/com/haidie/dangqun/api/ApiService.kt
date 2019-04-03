package com.haidie.dangqun.api

import com.haidie.dangqun.mvp.model.bean.*
import com.haidie.dangqun.net.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * Created by admin2
 *  on 2018/08/08  20:20
 * description  Api 接口
 */
interface ApiService {

    /**
     * 用户账号密码登录接口
     * http://192.168.3.161/dangqun_backend/public/api/user/login
     */
    @FormUrlEncoded
    @POST("api/user/login")
    fun getLoginData(@Field("username") username: String, @Field("group_id") group_id: String,
                     @Field("password") password: String, @Field("device_id") device_id: String
                     , @Field("device_type") device_type: String): Observable<BaseResponse<RegisterData>>

    /**
     * 退出登录
     * http://192.168.3.161/dangqun_backend_mayun/public/api/user/logout
     */
    @FormUrlEncoded
    @POST("api/user/logout")
    fun getLogoutData(@Field("uid") uid: Int,
                      @Field("token") token: String): Observable<BaseResponse<String>>

    /**
     * 获取个人信息接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/user/getInfo
     */
    @FormUrlEncoded
    @POST("api/user/getInfo")
    fun getMineData(@Field("uid") uid: Int,
                    @Field("token") token: String): Observable<BaseResponse<MineData>>

    /**
     * 修改用户个人中心接口
     * http://192.168.124.9/dangqunzhijia_web/public/api/user/profile
     */
    @Multipart
    @POST("api/user/profile")
    fun getModifyUserInfoData(@Part("uid") uid: RequestBody, @Part("token") token: RequestBody,
                              @Part("gender") gender: RequestBody, @Part("nickname") nickname: RequestBody,
                              @Part("birthday") birthday: RequestBody,
                              @Part parts: MultipartBody.Part?): Observable<BaseResponse<String>>

    /**
     * 修改用户密码接口
     * http://192.168.3.161/dangqun_backend_mayun/public/api/user/changepwd
     */
    @FormUrlEncoded
    @POST("api/user/changepwd")
    fun getChangePasswordData(@Field("uid") uid: Int, @Field("token") token: String, @Field("oldpwd") oldpwd: String,
                              @Field("newpwd") newpwd: String, @Field("repwd") repwd: String): Observable<BaseResponse<String>>

    /**
     * 获取应收账单列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/receive/getList
     */
    @FormUrlEncoded
    @POST("api/receive/getList")
    fun getLivingPaymentData(@Field("uid") uid: Int, @Field("token") token: String, @Field("page") page: Int,
                             @Field("size") size: Int, @Field("id") id: Int): Observable<BaseResponse<LivingPaymentData>>

    /**
     * 获取应收账单详情接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/receive/getInfo
     */
    @FormUrlEncoded
    @POST("api/receive/getInfo")
    fun getBillDetailData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<BillDetailData>>

    /**
     * 微信支付预付订单接口
     * http://XXX.com/api/Wxpay/Pay
     */
    @FormUrlEncoded
    @POST("api/Wxpay/Pay")
    fun getPrepaidOrderData(@Field("uid") uid: Int, @Field("token") token: String, @Field("orderNo") orderNo: String,
                            @Field("price") price: String, @Field("body") body: String): Observable<BaseResponse<PrepaidOrderData>>

    /**
     * 获取应收账单列表接口
     * http://xxx.com/api/receive/getHistoryList
     */
    @FormUrlEncoded
    @POST("api/receive/getHistoryList")
    fun getHistoricalBillData(@Field("uid") uid: Int, @Field("year") year: Int?, @Field("month") month: String?, @Field("page") page: Int,
                              @Field("size") size: Int, @Field("token") token: String): Observable<BaseResponse<HistoricalBillData>>

    /**
     * 商品分类列表
     * http://xx.com/api/shop_goods/get_catalog_list
     */
    @FormUrlEncoded
    @POST("api/shop_goods/get_catalog_list")
    fun getCommodityClassificationListData(@Field("user_id") uid: Int, @Field("token") token: String):
            Observable<BaseResponse<ArrayList<CommodityClassificationListData>>>

    /**
     * 异步获取微信支付结果通知
     * http://XXX.com/api/Wxpay/getPayResult
     */
    @FormUrlEncoded
    @POST("api/Wxpay/getPayResult")
    fun getPayResult(@Field("uid") uid: Int, @Field("token") token: String, @Field("orderNo") orderNo: String): Observable<BaseResponse<PayResultData>>

    /**
     * 户主绑定缴费账户页面
     * http://www.xxx.com/api/Receive/toPaymentAccount
     */
    @FormUrlEncoded
    @POST("api/Receive/toPaymentAccount")
    fun getBuildingUnitAndRoomNumberData(@Field("uid") uid: Int, @Field("token") token: String): Observable<BaseResponse<BuildingUnitAndRoomNumberData>>

    /**
     * 获取楼栋列表
     * {{local}}fix_order/get_block_info
     */
    @FormUrlEncoded
    @POST("api/fix_order/get_block_info")
    fun getBlockInfoData(@Field("uid") uid: Int, @Field("token") token: String): Observable<BaseResponse<ArrayList<BlockInfoData>>>

    /**
     * 获取单元的列表
     * {{local}}fix_order/get_unit_list
     */
    @FormUrlEncoded
    @POST("api/fix_order/get_unit_list")
    fun getUnitListData(@Field("uid") uid: Int, @Field("token") token: String,
                        @Field("title") title: String): Observable<BaseResponse<ArrayList<UnitListData>>>

    /**
     * 获取房子的列表
     * {{local}}fix_order/get_house_list
     */
    @FormUrlEncoded
    @POST("api/fix_order/get_house_list")
    fun getHouseListData(@Field("uid") uid: Int, @Field("token") token: String, @Field("title") title: String,
                         @Field("unit") unit: String): Observable<BaseResponse<ArrayList<HouseListData>>>

    /**
     * 获取业主姓名,手机号信息接口
     * http://www.xxx.com/api/Receive/getUserInfo
     */
    @FormUrlEncoded
    @POST("api/Receive/getUserInfo")
    fun getRoomNumberUserInfoData(@Field("uid") uid: Int, @Field("token") token: String,
                                  @Field("house_id") house_id: String): Observable<BaseResponse<RoomNumberUserInfoData>>

    /**
     * 户主绑定缴费账户表单提交接口
     * http://www.xxx.com/api/Receive/addPaymentAccount
     */
    @FormUrlEncoded
    @POST("api/Receive/addPaymentAccount")
    fun getAddPaymentAccountResultData(@Field("uid") uid: Int, @Field("token") token: String,
                                       @Field("house_id") house_id: Int): Observable<BaseResponse<Boolean>>

    /**
     * 获取绑定的缴费账户列表名单
     * http://www.xxx.com/api/Receive/getMyAccount
     */
    @FormUrlEncoded
    @POST("api/Receive/getMyAccount")
    fun getBoundPaymentAccountListData(@Field("uid") uid: Int, @Field("token") token: String): Observable<BaseResponse<ArrayList<BoundPaymentAccountListData>>>

    /**
     * 用户解除绑定缴费账户接口
     * http://www.xxx.com/api/Receive/unbind
     */
    @FormUrlEncoded
    @POST("api/Receive/unbind")
    fun getUnbindPaymentAccountResultData(@Field("uid") uid: Int, @Field("token") token: String,
                                          @Field("id") id: Int): Observable<BaseResponse<Boolean>>

    /**
     * 用户注册接口
     * http://192.168.3.3/dangqun_backend/public/api/user/register
     */
    @FormUrlEncoded
    @POST("api/user/register")
    fun getRegisterData(@Field("mobile") mobile: String, @Field("nickname") nickname: String?, @Field("group_id") group_id: Int?,
                        @Field("password") password: String, @Field("captcha") captcha: String): Observable<BaseResponse<RegisterData>>

    /**
     * 获取家政服务分类接口
     * http://192.168.3.3/dangqun_backend/public/api/service/getServiceCategory
     */
    @GET("api/service/getServiceCategory")
    fun getServiceCategoryData(@Query("id") id: String): Observable<BaseResponse<ServiceCategoryData>>

    /**
     * 物业工单提交接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/toWorkOrder
     */
    @Multipart
    @POST("api/workorder/toWorkOrder")
    fun getToWorkOrderData(@Part("uid") uid: RequestBody, @Part("cid") cid: RequestBody,
                           @Part("title") title: RequestBody, @Part("content") content: RequestBody,
                           @Part("token") token: RequestBody, @Part parts: List<MultipartBody.Part>): Observable<BaseResponse<Boolean>>

    /**
     * 物业工单管理-列表展示接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/getOrderList
     */
    @FormUrlEncoded
    @POST("api/workorder/getOrderList")
    fun getOrderListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                         @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<OrderListData>>

    /**
     * 物业工单管理-工单详情
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/getOrderInfo
     */
    @FormUrlEncoded
    @POST("api/workorder/getOrderInfo")
    fun getOrderInfoData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<OrderInfoData>>

    /**
     * 物业工单管理-获取工单回复历史记录接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/getHistoryReplay
     */
    @FormUrlEncoded
    @POST("api/workorder/getHistoryReplay")
    fun getHistoryReplayData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("page") page: Int, @Field("size") size: Int,
                             @Field("token") token: String): Observable<BaseResponse<HistoryReplayData>>

    /**
     * 物业工单管理-工单评价接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/toReplay
     */
    @FormUrlEncoded
    @POST("api/workorder/toReplay")
    fun getToReplayData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("title") title: String, @Field("content") content: String,
                        @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 物业工单管理-修改工单状态接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/editStatus
     */
    @FormUrlEncoded
    @POST("api/workorder/editStatus")
    fun getEditStatusData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("status") status: Int, @Field("token") token: String)
            : Observable<BaseResponse<Boolean>>

    /**
     * 物业工单管理-工单评价接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/workorder/toComment
     */
    @FormUrlEncoded
    @POST("api/workorder/toComment")
    fun getToCommentData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("rank") rank: Int,
                         @Field("content") content: String, @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 获取文章列表接口
     * http://192.168.3.3/dangqun_backend/public/api/article/getArticleList?id=26
     */
    @GET("api/article/getArticleList")
    fun getArticleListData(@Query("id") id: String): Observable<BaseResponse<ArticleListData>>

    /**
     * 获取文章详情
     * http://192.168.3.3/dangqun_backend/public/api/article/getArticleDetail
     */
    @GET("api/article/getArticleDetail")
    fun getArticleDetailData(@Query("id") id: String): Observable<BaseResponse<List<ArticleDetailData>>>

    /**
     * 获取活动详情
     * http://192.168.3.3/dangqun_backend/public/api/activity/getActivityDetail
     */
    @FormUrlEncoded
    @POST("api/activity/getActivityDetail")
    fun getActivityDetailData(@Field("id") id: String, @Field("uid") uid: Int, @Field("token") token: String)
            : Observable<BaseResponse<List<ActivityDetailData>>>

    /**
     * 获取家政服务列表接口
     * http://192.168.3.3/dangqun_backend/public/api/service/getServiceList
     */
    @GET("api/service/getServiceList")
    fun getServiceListData(@Query("id") id: String): Observable<BaseResponse<ServiceListData>>

    /**
     * 获取家政服务详情接口
     * http://192.168.3.3/dangqun_backend/public/api/service/getServiceDetail
     */
    @GET("api/service/getServiceDetail")
    fun getServiceDetailData(@Query("id") id: String): Observable<BaseResponse<List<ServiceDetailItemData>>>

    /**
     * 家政(物业)服务下单接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/service/create_order
     */
    @FormUrlEncoded
    @POST("api/service/create_order")
    fun getCreateOrderData(@Field("uid") uid: Int, @Field("sid") sid: Int, @Field("username") username: String, @Field("phone") phone: String,
                           @Field("content") content: String, @Field("time") time: String, @Field("address") address: String,
                           @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 家政(物业)服务订单评论接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/service/toOrderComment
     */
    @FormUrlEncoded
    @POST("api/service/toOrderComment")
    fun getSubmitEvaluationData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("rank") rank: Int, @Field("content") content: String,
                                @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 发送短信接口
     * http://xxx.com/api/sms/sends
     */
    @FormUrlEncoded
    @POST("api/sms/sends")
    fun getSendSMSData(@Field("mobile") mobile: String, @Field("event") event: String): Observable<BaseResponse<String>>

    /**
     * 短信验证码验证接口
     * http://xxx.com/api/sms/check
     */
    @FormUrlEncoded
    @POST("api/sms/check")
    fun getCheckVerificationCodeData(@Field("mobile") mobile: String, @Field("event") event: String, @Field("captcha") captcha: String)
            : Observable<BaseResponse<String>>

    /**
     * 找回忘记密码功能接口
     * http://192.168.3.161/dangqun_backend_mayun/public/api/user/resetpwd
     */
    @FormUrlEncoded
    @POST("api/user/resetpwd")
    fun getResetPwdData(@Field("mobile") mobile: String, @Field("type") type: String, @Field("newpwd") newpwd: String,
                        @Field("captcha") captcha: String): Observable<BaseResponse<String>>

    /**
     * 智慧新风-获取积分商城列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/goods/getList
     */
    @FormUrlEncoded
    @POST("api/goods/getList")
    fun getPointsMallListData(@Field("uid") uid: Int, @Field("type") type: Int, @Field("page") page: Int,
                              @Field("size") size: Int, @Field("token") token: String): Observable<BaseResponse<PointsMallListData>>

    /**
     * 积分商品兑换接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/goods/toOrder
     */
    @FormUrlEncoded
    @POST("api/goods/toOrder")
    fun getExchangeData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 智慧新风-获取志愿者活动列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getActivityList
     */
    @FormUrlEncoded
    @POST("api/activity/getActivityList")
    fun getVolunteerActivitiesListData(@Field("uid") uid: Int, @Field("groupid") groupid: Int?, @Field("page") page: Int,
                                       @Field("size") size: Int, @Field("type") type: Int, @Field("status") status: Int,
                                       @Field("token") token: String): Observable<BaseResponse<MyVolunteerActivitiesListData>>

    /**
     * 智慧新风-获取志愿者活动列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getActivityList
     */
    @FormUrlEncoded
    @POST("api/activity/getActivityList")
    fun getActivityRecordListData(@Field("uid") uid: Int, @Field("token") token: String, @Field("page") page: Int,
                                  @Field("size") size: Int, @Field("type") type: Int): Observable<BaseResponse<MyVolunteerActivitiesListData>>

    /**
     * 获取活动记录详情接口
     * http://www.xxx.com/api/activity/getActivityRecordDetail
     */
    @FormUrlEncoded
    @POST("api/activity/getActivityRecordDetail")
    fun getActivityRecordDetailData(@Field("uid") uid: Int, @Field("token") token: String,
                                    @Field("id") id: Int): Observable<BaseResponse<ActivityRecordDetailData>>

    /**
     * 获取我参加的志愿者活动列表
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getMyVolunteerList
     */
    @FormUrlEncoded
    @POST("api/activity/getMyVolunteerList")
    fun getMyVolunteerActivitiesListData(@Field("uid") uid: Int, @Field("token") token: String, @Field("page") page: Int, @Field("size") size: Int)
            : Observable<BaseResponse<MyVolunteerActivitiesListData>>

    /**
     * 智慧新风-获取志愿者活动详情接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getActivityDetail
     */
    @FormUrlEncoded
    @POST("api/activity/getActivityDetail")
    fun getVolunteerActivitiesDetailData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String)
            : Observable<BaseResponse<List<VolunteerActivitiesDetailData>>>

    /**
     * 志愿者活动签到打卡功能
     * http://www.xxx.com/api/activity/signin
     */
    @FormUrlEncoded
    @POST("api/activity/signin")
    fun getVolunteerActivitiesSignInData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 志愿者活动签到签退
     * http://www.xxx.com/api/Volunteer_Record_Log/ign_in_and_out
     * */
    @FormUrlEncoded
    @POST("api/Volunteer_Record_Log/ign_in_and_out")
    fun getVolunteerSignInOut(@Field("user_id") user_id: Int,
                              @Field("token") token: String,
                              @Field("voluntter_id") voluntter_id: String,
                              @Field("activity_id") activity_id: String
    ): Observable<BaseResponse<Boolean>>

    /**
     * 智慧新风-获取志愿者参加活动人员列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getMember
     */
    @FormUrlEncoded
    @POST("api/activity/getMember")
    fun getVolunteersParticipateActivitiesListData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("page") page: Int, @Field("size") size: Int,
                                                   @Field("token") token: String): Observable<BaseResponse<VolunteersParticipateActivitiesListData>>

    /**
     * 活动报名接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/addActivity
     */
    @FormUrlEncoded
    @POST("api/activity/addActivity")
    fun getAddActivityData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 志愿者活动签到打卡功能
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/signin
     */
    @FormUrlEncoded
    @POST("api/activity/signin")
    fun getSignInData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 志愿者活动签退功能
     * http://www.XXX.com/api/activity/signout
     */
    @Multipart
    @POST("api/activity/signout")
    fun getVolunteerActivitiesSignOutData(@Part("uid") uid: RequestBody, @Part("id") id: RequestBody, @Part("token") token: RequestBody,
                                          @Part("content") content: RequestBody, @Part part: MultipartBody.Part): Observable<BaseResponse<Boolean>>

    /**
     * 智慧新风-获取志愿者组织列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/getGroupList
     */
    @FormUrlEncoded
    @POST("api/volunteer/getGroupList")
    fun getVoluntaryOrganizationListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int, @Field("token") token: String)
            : Observable<BaseResponse<VoluntaryOrganizationListData>>

    /**
     * 智慧新风-获取志愿者组织详情接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/getGroupInfo
     */
    @FormUrlEncoded
    @POST("api/volunteer/getGroupInfo")
    fun getVoluntaryOrganizationDetailData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String)
            : Observable<BaseResponse<VoluntaryOrganizationDetailData>>

    /**
     * 智慧新风-获取志愿者列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/getList
     */
    @FormUrlEncoded
    @POST("api/volunteer/getList")
    fun getVolunteerListData(@Field("uid") uid: Int, @Field("group_id") group_id: Int, @Field("type") type: Int, @Field("search") search: String,
                             @Field("page") page: Int, @Field("size") size: Int, @Field("token") token: String): Observable<BaseResponse<VolunteerListData>>

    /**
     * 智慧新风-志愿者申请表单提交接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/add
     */
    @FormUrlEncoded
    @POST("api/volunteer/add")
    fun getVolunteerApplicationData(@Field("uid") uid: Int, @Field("group_id") group_id: Int, @Field("username") username: String,
                                    @Field("gender") gender: Int, @Field("phone") phone: String,
                                    @Field("nation") nation: String, @Field("birthday") birthday: String,
                                    @Field("face") face: Int, @Field("study") study: Int,
                                    @Field("identity") identity: String, @Field("company") company: String,
                                    @Field("address") address: String, @Field("hobby") hobby: String,
                                    @Field("skill") skill: String, @Field("experience") experience: String,
                                    @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 智慧新风-获取志愿者详情接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/getInfo
     */
    @FormUrlEncoded
    @POST("api/volunteer/getInfo")
    fun getVolunteerDetailData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<VolunteerDetailData>>

    /**
     * 智慧新风-设为/取消管理员接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/editStatus
     */
    @FormUrlEncoded
    @POST("api/volunteer/editStatus")
    fun getEditAdministratorStatusData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("status") status: Int,
                                       @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 智慧新风-审核志愿者入组织接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/volunteer/editChange
     */
    @FormUrlEncoded
    @POST("api/volunteer/editChange")
    fun getEditPendingVolunteerChangeData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("status") status: Int,
                                          @Field("token") token: String): Observable<BaseResponse<Boolean>>

    /**
     * 智慧新风-获取评优评先列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/vote/getVoteList
     */
    @FormUrlEncoded
    @POST("api/vote/getVoteList")
    fun getEvaluationListData(@Field("uid") uid: Int, @Field("category") category: Int, @Field("page") page: Int,
                              @Field("size") size: Int, @Field("token") token: String): Observable<BaseResponse<EvaluationListData>>

    /**
     * 获取投票详情
     * http://192.168.3.3/dangqun_backend/public/api/vote/getVoteDetail
     */
    @GET("api/vote/getVoteDetail")
    fun getVoteDetailData(@Query("id") id: String): Observable<BaseResponse<VoteDetailData>>

    /**
     * 用户参与投票接口
     * http://192.168.3.3/dangqun_backend/public/api/vote/addVoteIn
     */
    @FormUrlEncoded
    @POST("api/vote/addVoteIn")
    fun getAddVoteInData(@Field("vid") vid: Int, @Field("vinfoid") vInfoId: String, @Field("uid") uid: Int): Observable<BaseResponse<Boolean>>

    /**
     * 获取网上求助列表展示接口
     * http://192.168.124.9/dangqunzhijia_web/public/api/Helps/getList
     */
    @FormUrlEncoded
    @POST("api/Helps/getList")
    fun getOnlineHelpListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                              @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<OnlineHelpListData>>

    /**
     * 获取网上求助列表展示接口-更改
     * http://192.168.124.9/dangqunzhijia_web/public/api/Helps/getList
     */
    @FormUrlEncoded
    @POST("api/Helps/getList")
    fun getOnlineHelpData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int, @Field("id") id: Int,
                          @Field("check_status") check_status: Int, @Field("status") status: Int,
                          @Field("token") token: String): Observable<BaseResponse<OnlineHelpListData>>

    /**
     * 网上求助再次提交接口
     * http://www.xxx.com/api/helps/re_status
     */
    @FormUrlEncoded
    @POST("api/helps/re_status")
    fun getOnlineHelpSubmitAgainData(@Field("uid") uid: Int, @Field("token") token: String, @Field("id") id: Int,
                                     @Field("status") status: Int): Observable<BaseResponse<Boolean>>

    /**
     * 网上求助删除接口
     * http://www.xxx.com/api/helps/re_del
     */
    @FormUrlEncoded
    @POST("api/helps/re_del")
    fun getOnlineHelpDeleteData(@Field("uid") uid: Int, @Field("token") token: String, @Field("id") id: Int,
                                @Field("status") status: Int): Observable<BaseResponse<Boolean>>

    /**
     * 网上求助表单提交接口
     * http://192.168.124.9/dangqunzhijia_web/public/api/Helps/create
     */
    @Multipart
    @POST("api/Helps/create")
    fun getOnlineHelpFormSubmissionData(@Part("uid") uid: RequestBody, @Part("token") token: RequestBody,
                                        @Part("type") type: RequestBody?, @Part("troubletype") troubletype: RequestBody,
                                        @Part("is_online") is_online: RequestBody, @Part("username") username: RequestBody, @Part("gender") gender: RequestBody,
                                        @Part("phone") phone: RequestBody, @Part("identity") identity: RequestBody,
                                        @Part("address") address: RequestBody?, @Part("title") title: RequestBody,
                                        @Part("content") content: RequestBody, @Part parts: List<MultipartBody.Part>?): Observable<BaseResponse<Boolean>>

    /**
     * 网上求助编辑接口
     * http://www.xxx.com/api/Helps/re_edit
     */
    @Multipart
    @POST("api/Helps/re_edit")
    fun getMyHelpEditData(@Part("uid") uid: RequestBody, @Part("token") token: RequestBody, @Part("id") id: RequestBody,
                          @Part("type") type: RequestBody?, @Part("troubletype") troubletype: RequestBody,
                          @Part("is_online") is_online: RequestBody, @Part("username") username: RequestBody, @Part("gender") gender: RequestBody,
                          @Part("phone") phone: RequestBody, @Part("identity") identity: RequestBody,
                          @Part("address") address: RequestBody?, @Part("title") title: RequestBody,
                          @Part("content") content: RequestBody, @Part("pic1") pic1: RequestBody?, @Part("pic2") pic2: RequestBody?,
                          @Part("pic3") pic3: RequestBody?, @Part part1: MultipartBody.Part?, @Part part2: MultipartBody.Part?,
                          @Part part3: MultipartBody.Part?): Observable<BaseResponse<Boolean>>

    /**
     * 获取网上求助详情接口
     * http://192.168.124.9/dangqunzhijia_web/public/api/Helps/getOrderInfo
     */
    @FormUrlEncoded
    @POST("api/Helps/getOrderInfo")
    fun getOnlineHelpDetailsData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<OnlineHelpDetailsData>>

    /**
     * 查看网上求助历史评论接口
     * http://192.168.124.9/dangqunzhijia_web/public/api/workorder/getHistoryReplay
     */
    @FormUrlEncoded
    @POST("api/workorder/getHistoryReplay")
    fun getOnlineHelpHistoryReplayData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("page") page: Int, @Field("size") size: Int,
                                       @Field("token") token: String): Observable<BaseResponse<OnlineHelpHistoryReplayData>>

    /**
     * 网上求助留言接口
     * http://192.168.124.9/dangqunzhijia_web/public/api/workorder/toReplay
     */
    @FormUrlEncoded
    @POST("api/workorder/toReplay")
    fun getOnlineHelpToReplayData(@Field("uid") uid: Int, @Field("token") token: String, @Field("id") id: Int, @Field("type") type: Int,
                                  @Field("content") content: String): Observable<BaseResponse<Boolean>>

    /**
     * APP首页轮播文章, 暂时设计只有3页滚动.
     * http://xx.com/api/toutiao_article/slideshow_article
     */
    @FormUrlEncoded
    @POST("api/toutiao_article/slideshow_article")
    fun getHomeBannerData(@Field("user_id") user_id: Int, @Field("token") token: String): Observable<BaseResponse<HomeBannerData>>

    /**
     * APP首页 功能栏下方的各栏目新闻推荐
     * http://xx.com/api/toutiao_article/top_news
     */
    @FormUrlEncoded
    @POST("api/toutiao_article/top_news")
    fun getHomeNewsData(@Field("user_id") user_id: Int, @Field("token") token: String): Observable<BaseResponse<HomeNewsData>>

    /**
     * 获取文章列表
     * http://192.168.3.3/dangqun_backend_mayun/public/api/article/getList
     */
    @FormUrlEncoded
    @POST("api/article/getList")
    fun getGovernmentArticleListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                                     @Field("cid") cid: Int, @Field("token") token: String,
                                     @Field("type") type: Int?): Observable<BaseResponse<GovernmentArticleListData>>

    /**
     * 获取我的兑换数据列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/goods/getExchangeList
     */
    @FormUrlEncoded
    @POST("api/goods/getExchangeList")
    fun getMyExchangeDataListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                                  @Field("token") token: String): Observable<BaseResponse<ExchangeDataListData>>

    /**
     * 家政(物业)服务订单列表展示接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/service/getOrderList
     */
    @FormUrlEncoded
    @POST("api/service/getOrderList")
    fun getMyOrderListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                           @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<MyOrderListData>>

    /**
     * 家政(物业)服务订单详情页面接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/service/getOrderInfo
     */
    @FormUrlEncoded
    @POST("api/service/getOrderInfo")
    fun getMyOrderDetailData(@Field("uid") uid: Int, @Field("id") id: Int, @Field("token") token: String): Observable<BaseResponse<List<MyOrderDetailData>>>

    /**
     * 个人中心-获取我参加的活动列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getMyActivityList
     */
    @FormUrlEncoded
    @POST("api/activity/getMyActivityList")
    fun getIParticipatedData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                             @Field("id") id: Int?, @Field("token") token: String): Observable<BaseResponse<IParticipatedData>>

    /**
     * 个人中心-获取我发布的活动列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/getMyPutList
     */
    @FormUrlEncoded
    @POST("api/activity/getMyPutList")
    fun getIReleasedData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                         @Field("token") token: String): Observable<BaseResponse<IReleasedData>>

    /**
     * APP前台用户发布活动接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/activity/add
     */
    @Multipart
    @POST("api/activity/add")
    fun getReleaseActivitiesData(@Part("uid") uid: RequestBody, @Part("type") type: RequestBody, @Part("title") title: RequestBody,
                                 @Part("content") content: RequestBody, @Part("start_time") start_time: RequestBody,
                                 @Part("end_time") end_time: RequestBody, @Part("area") area: RequestBody,
                                 @Part("address") address: RequestBody, @Part("token") token: RequestBody,
                                 @Part part: MultipartBody.Part): Observable<BaseResponse<Boolean>>

    /**
     * 个人中心-获取我发布的投票列表接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/vote/getMyPutList
     */
    @FormUrlEncoded
    @POST("api/vote/getMyPutList")
    fun getMyVoteListData(@Field("uid") uid: Int, @Field("page") page: Int, @Field("size") size: Int,
                          @Field("token") token: String): Observable<BaseResponse<MyVoteListData>>

    /**
     * 用户发布投票接口
     * http://192.168.3.3/dangqun_backend_mayun/public/api/vote/add
     */
    @Multipart
    @POST("api/vote/add")
    fun getReleaseVoteData(@Part("uid") uid: RequestBody, @Part("token") token: RequestBody,
                           @Part("type") type: RequestBody, @Part("title") title: RequestBody,
                           @Part("content") content: RequestBody, @Part("start_time") start_time: RequestBody,
                           @Part("end_time") end_time: RequestBody, @Part("choose") choose: RequestBody,
                           @Part part: MultipartBody.Part): Observable<BaseResponse<Boolean>>

    /**
     * 版本检查
     * http://xx.com/index/app/version_check
     */
    @POST("index/app/version_check")
    fun getCheckVersionData(): Observable<BaseResponse<CheckVersionData>>

    /**
     * 获取志愿者基本信息
     * http://www.xxx.com/api/volunteer/get_info
     */
    @FormUrlEncoded
    @POST("api/volunteer/get_info")
    fun getVolunteerInfoData(@Field("uid") uid: Int, @Field("token") token: String): Observable<BaseResponse<VolunteerInfoData>>

    /**
     * 获取儿童成绩查询列表接口
     * http://xxx.com/api/score/searchScore
     */
    @FormUrlEncoded
    @POST("api/score/searchScore")
    fun getScoreQueryData(@Field("uid") uid: Int, @Field("token") token: String,
                          @Field("username") username: String, @Field("year") year: String,
                          @Field("class") clazz: String, @Field("type") type: Int): Observable<BaseResponse<ScoreQueryData>>

    /**
     * 活动公告-新风活动
     * http://xx.com/api/toutiao_article/article_list
     */
    @FormUrlEncoded
    @POST("api/toutiao_article/article_list")
    fun getFreshAirActivitiesData(@Field("user_id") user_id: Int, @Field("token") token: String,
                                  @Field("page") page: Int, @Field("size") size: Int, @Field("module_type") module_type: Int,
                                  @Field("is_register_copy") is_register_copy: Int): Observable<BaseResponse<FreshAirActivitiesData>>

    /**
     * 活动公告-新风活动-详情
     * http://xx.com/api/toutiao_article/detail
     */
    @FormUrlEncoded
    @POST("api/toutiao_article/detail")
    fun getFreshAirActivitiesDetailData(@Field("user_id") user_id: Int, @Field("token") token: String,
                                        @Field("article_id") article_id: Int): Observable<BaseResponse<FreshAirActivitiesDetailData>>

    /**
     * 智慧关爱-获取四点半课堂记录列表接口
     * http://xxx.com/api/activity/getClassList
     */
    @FormUrlEncoded
    @POST("api/activity/getClassList")
    fun getHalfPastFourClassData(@Field("uid") uid: Int, @Field("token") token: String,
                                 @Field("page") page: Int, @Field("size") size: Int,
                                 @Field("type") type: Int): Observable<BaseResponse<HalfPastFourClassData>>

    /**
     * 获取课堂记录详情接口
     * http://www.xxx.com/api/activity/getClassDetail
     */
    @FormUrlEncoded
    @POST("api/activity/getClassDetail")
    fun getHalfPastFourClassDetailData(@Field("uid") uid: Int, @Field("token") token: String,
                                       @Field("id") id: Int): Observable<BaseResponse<HalfPastFourClassDetailData>>

    /**
     * 更新信鸽推送token
     * http://xx.com/api/user/update_device_token
     */
    @FormUrlEncoded
    @POST("api/user/update_device_token")
    fun getXGData(@Field("user_id") admin_id: Int, @Field("device_token") device_token: String,
                  @Field("token") token: String): Observable<BaseResponse<String>>

}