package com.laanaoui.mycontacts.Utils

import com.laanaoui.mycontacts.Model.Contact

class ContactReadModel(origin: Contact) {
	val id: String = origin.id
	val emailAddress: String = origin.emailAddress
	val firstName: String = origin.firstName
	val gender: String = origin.gender
	val lastName: String = origin.lastName
	val tag: String = origin.tag
	val phoneNumber: String = origin.phoneNumber
	val photoUrl: String =  "http://192.168.1.7:8090/api/files/${origin.collectionId}/${origin.id}/${origin.photo}"
}