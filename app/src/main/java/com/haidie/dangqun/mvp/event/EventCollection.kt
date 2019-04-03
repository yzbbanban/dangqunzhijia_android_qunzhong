package com.haidie.dangqun.mvp.event

/**
 * Create by   Administrator
 *      on     2018/08/28 11:09
 * description
 */
class MineEvent

class PayEvent(val isSuccess : Boolean)

class PayResultEvent

class WorkOrderManagementDetailEditStatus

class VolunteerApplicationEvent

class EditPendingVolunteerChangeEvent

class EditAdministratorStatusEvent

class SwitchToBusinessEvent

class ReloadLifeEvent

class ReloadBusinessEvent

class ReloadMyReportEvent

class ReloadMyHelpEvent

class ReleaseActivitiesEvent

class ReleaseVoteEvent

class ReloadFaultRepairEvent

class ReloadLifePaymentEvent

class AutoLoginEvent(var mobile : String,var password : String)

class RegisterXGEvent
