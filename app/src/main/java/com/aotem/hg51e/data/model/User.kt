package com.aotem.hg51e.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("retcode") val retcode: String = "",
    @SerializedName("data") val data: DataBean = DataBean()
) {
    data class DataBean(
        @SerializedName("web_user_name") val webUserName: String = "",
        @SerializedName("web_tele_account_name") val webTeleAccountName: String = "",
        @SerializedName("userNameModify_enable") val userNameModifyEnable: String = "",
        @SerializedName("aucDefaultAccountPassword") val aucDefaultAccountPassword: String = "",
        @SerializedName("teleAccount_password") val teleAccountPassword: String = "",
        @SerializedName("telecomNameModify") val telecomNameModify: String = "",
        @SerializedName("userNameModify") val userNameModify: String = ""
    )
}
