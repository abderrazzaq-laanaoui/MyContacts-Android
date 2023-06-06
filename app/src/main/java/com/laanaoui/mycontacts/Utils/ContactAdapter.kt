package com.laanaoui.mycontacts.Utils

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.laanaoui.mycontacts.R
import com.squareup.picasso.Picasso
import com.laanaoui.mycontacts.Controller.ContactsController
import com.laanaoui.mycontacts.View.UpdateContactActivity
import retrofit2.Call
import retrofit2.Response

class ContactAdapter(private val contacts: List<ContactReadModel>) :
	RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

	lateinit var contactsChanged: () -> Unit
	private lateinit var contactsController: ContactsController

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_contact, parent, false)
		contactsController = ContactsController.create()
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val contact = contacts[position]
		holder.textViewName.text = "${contact.firstName} ${contact.lastName}"
		holder.textViewPhone.text =  "${contact.phoneNumber}"
		holder.textViewEmail.text = contact.emailAddress
		holder.textViewTag.text = contact.tag
		if(contact.photoUrl.lowercase().endsWith(".png") || contact.photoUrl.lowercase().endsWith(".jpg"))
			Picasso.get().load(contact.photoUrl).into(holder.imageViewPhoto)
		else
			holder.imageViewPhoto.setImageResource(R.mipmap.ic_avatar);


		holder.btnDelete.setOnClickListener {
			val builder = AlertDialog.Builder(holder.itemView.context)
			builder.setTitle("Delete Contact")
			builder.setMessage("Are you sure you want to delete this contact '${contact.firstName} ${contact.lastName}'?")
			builder.setPositiveButton("Delete") { dialog, which ->
				contactsController.deleteContact(contact.id).enqueue(object : retrofit2.Callback<Void> {
					override fun onResponse(call: Call<Void>, response: Response<Void>) {
						Toast.makeText(holder.itemView.context, "Deleted", Toast.LENGTH_LONG).show()
						contactsChanged()
					}
					override fun onFailure(call: Call<Void>, t: Throwable) {
						Log.e("ContactAdapter", "Error deleting contact", t)
						Toast.makeText(holder.itemView.context, "Error", Toast.LENGTH_LONG).show()
					}
				})
			}
			builder.setNegativeButton("Cancel") { dialog, _ ->
				dialog.dismiss()
			}
			val dialog: AlertDialog = builder.create()
			dialog.show()
		}
		holder.btnEdit.setOnClickListener {
			val intent = Intent(holder.itemView.context, UpdateContactActivity::class.java)
			intent.putExtra("contactId", contact.id)
			holder.itemView.context.startActivity(intent)
		}
	}

	override fun getItemCount(): Int {
		return contacts.size
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val textViewName: TextView = view.findViewById(R.id.textViewName)
		val textViewPhone: TextView = view.findViewById(R.id.textViewPhone)
		val textViewEmail: TextView = view.findViewById(R.id.textViewEmail)
		val textViewTag: TextView = view.findViewById(R.id.textViewTag)
		val imageViewPhoto: ImageView = view.findViewById(R.id.imageViewPhoto)
		val btnDelete: MaterialButton = view.findViewById(R.id.btnDelete)
		val btnEdit: MaterialButton = view.findViewById(R.id.btnEdit)
	}
}
