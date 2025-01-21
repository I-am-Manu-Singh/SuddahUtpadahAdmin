package com.neatroots.suddahutpadahadmin.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.neatroots.suddahutpadahadmin.R
import com.neatroots.suddahutpadahadmin.databinding.DialogProgressBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun navigate(view: View, id: Int) {
        findNavController(view).navigate(id)
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.inVisible() {
        visibility = View.INVISIBLE
    }

    fun showMessage(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLoading(context: Context): AlertDialog {
        val progress = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
        val processLayout = DialogProgressBinding.inflate(LayoutInflater.from(context))
        progress.setView(processLayout.root)
        progress.setCancelable(false)
        return progress
    }

    fun openDialer(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}