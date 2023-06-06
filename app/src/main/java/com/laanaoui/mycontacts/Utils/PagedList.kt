package com.laanaoui.mycontacts.Utils

import com.google.gson.annotations.SerializedName

data class PagedList<T>(
	@SerializedName("page")
	val page: Int,
	@SerializedName("perPage")
	val perPage: Int,
	@SerializedName("totalItems")
	val totalItems: Int,
	@SerializedName("totalPages")
	val totalPages: Int,
	@SerializedName("items")
	val items: List<T>
)
