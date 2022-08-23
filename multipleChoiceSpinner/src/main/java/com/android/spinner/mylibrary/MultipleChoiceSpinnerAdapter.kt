package com.android.spinner.mylibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.TextView

class MultipleChoiceSpinnerAdapter(
    private val myContext: Context,
    val resource: Int,
    private val dataList: List<MultipleChoiceSpinnerFields>,
    val chosenPositions: List<Int>,
    var checkedListener: (List<MultipleChoiceSpinnerFields>) -> Unit
) : ArrayAdapter<MultipleChoiceSpinnerFields>(myContext, resource, dataList) {

    init {
        chosenPositions.forEach {
            dataList[it].isSelected = true
        }
    }

    private fun getPlaceholderText() = dataList
        .filter { it.isSelected }
        .joinToString(myContext.getString(R.string.separator_comma)) { it.text }
        .ifEmpty { myContext.getString(R.string.choose_ur_options) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customOuterView(convertView)
    }

    private fun customOuterView(convertView: View?): View {
        var returnView: View? = convertView
        if (convertView == null) {
            returnView = LayoutInflater.from(myContext)
                .inflate(android.R.layout.simple_spinner_dropdown_item, null)
            val text = returnView.findViewById<CheckedTextView>(android.R.id.text1)
            text.text = getPlaceholderText()
        }

        return returnView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customDropdownView(position, convertView)
    }

    private fun customDropdownView(position: Int, convertView: View?): View {
        val viewHolder: VH
        var returnView: View? = convertView
        if (convertView == null) {
            returnView =
                LayoutInflater.from(myContext)
                    .inflate(R.layout.multiple_choice_spinner_layout, null)
            viewHolder = VH(returnView)
            returnView.tag = viewHolder

        } else {
            viewHolder = convertView.tag as VH
        }

        viewHolder.myText.text = dataList[position].text
        viewHolder.isSelected.isChecked = dataList[position].isSelected

        viewHolder.isSelected.setOnClickListener {
            dataList[position].isSelected = !dataList[position].isSelected
            checkedListener(dataList)
            notifyDataSetChanged()
        }

        return returnView!!
    }

    inner class VH(view: View) {
        var myText: TextView = view.findViewById(R.id.tvText)
        var isSelected: CheckBox = view.findViewById(R.id.cbCheck)
    }
}
