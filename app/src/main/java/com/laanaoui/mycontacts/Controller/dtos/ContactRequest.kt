package com.laanaoui.mycontacts.Controller.dtos

import com.google.gson.annotations.SerializedName

data class ContactRequest (
	@SerializedName("emailAddress")
	val emailAddress: String,
	@SerializedName("gender")
	val gender: String,
	@SerializedName("tag")
	val tag: String,
	@SerializedName("firstName")
	val firstName: String,
	@SerializedName("lastName")
	val lastName: String,
	@SerializedName("phoneNumber")
	val phoneNumber: String,
)