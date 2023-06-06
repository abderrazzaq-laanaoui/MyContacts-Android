package com.laanaoui.mycontacts.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Contact  (
	@SerializedName("collectionId")
	val collectionId: String,
	@SerializedName("collectionName")
	val collectionName: String,
	@SerializedName("created")
	val created: String,
	@SerializedName("emailAddress")
	val emailAddress: String,
	@SerializedName("firstName")
	val firstName: String,
	@SerializedName("gender")
	val gender: String,
	@SerializedName("id")
	val id: String,
	@SerializedName("lastName")
	val lastName: String,
	@SerializedName("tag")
	val tag: String,
	@SerializedName("phoneNumber")
	val phoneNumber: String,
	@SerializedName("photo")
	val photo: String,
	@SerializedName("updated")
	val updated: String
)