package com.parkme.findparking.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.parkme.findparking.databinding.DialogAddCarBinding
import com.parkme.findparking.databinding.DialogLogoutLayoutBinding
import java.util.Calendar
import java.util.Date

object Dialogs {

    fun logOutDialog(
        context: Context,
        inflater: LayoutInflater,
        setProfileImgCallback:() -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogLogoutLayoutBinding.inflate(inflater, null, false)
        dialog.apply {
            setContentView(binding.root)
            setCancelable(true)

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.btnYes.setOnClickListener {
                setProfileImgCallback.invoke()
                dismiss()
            }

            binding.btnNo.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    fun addCarDialog(
        context: Context,
        inflater: LayoutInflater,
        carInfo:(String,String) -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogAddCarBinding.inflate(inflater, null, false)

        dialog.apply {
            setContentView(binding.root)
            setCancelable(false)

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            binding.btnAdd.setOnClickListener {
                val carName = binding.etCarName.text.toString().trim()
                val carNumber = binding.etCarNumber.text.toString().trim()
                carInfo.invoke(carName,carNumber)
                dismiss()
            }

            binding.btnCancel.setOnClickListener {
                dismiss()
            }

            show()
        }
    }

    inline fun permissionAlertDialog(context: Context,message:String,crossinline callback:() -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.apply{
            setMessage(message)
            setPositiveButton("Yes") { dialog, _ ->
                callback.invoke()
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            setCancelable(false)
            create()
            show()
        }
    }
    inline fun showDatePickerDialog(context: Context,crossinline onDateSelected:(date:Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val date = selectedDate.time
                onDateSelected.invoke(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }

}