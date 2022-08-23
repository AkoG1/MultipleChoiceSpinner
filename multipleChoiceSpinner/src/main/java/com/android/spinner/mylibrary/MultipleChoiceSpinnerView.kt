package com.android.spinner.mylibrary

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CheckedTextView

class MultipleChoiceSpinnerView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatSpinner(context, attrs) {

    private var placeholderText: String = context.getString(R.string.choose_ur_options)

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultipleChoiceSpinnerView)
        placeholderText =
            typedArray.getString(R.styleable.MultipleChoiceSpinnerView_placeholder_text)
                ?: context.getString(R.string.choose_ur_options)
        typedArray.recycle()
    }

    private val emptyString = context.getString(R.string.empty_string)

    fun initializeSpinner(
        itemTitleList: List<String>,
        chosenPositions: List<Int> = emptyList(),
        selectedSectionListener: (List<Int>, String?) -> Unit,
    ) {
        val spinnerList = mutableListOf<MultipleChoiceSpinnerFields>()
        itemTitleList.forEach { item ->
            spinnerList.add(MultipleChoiceSpinnerFields(item, false))
        }
        val adapter = MultipleChoiceSpinnerAdapter(
            myContext = this.context,
            resource = 0,
            chosenPositions = chosenPositions,
            dataList = spinnerList,
        ) { adapterList ->
            var displayText = emptyString

            val selectedPosition = arrayListOf<Int>()
            for (i in adapterList.indices) {
                if (adapterList[i].isSelected) {
                    val item = itemTitleList[i]
                    displayText += context.getString(R.string.formatted_display_text, item)
                    selectedPosition.add(i)
                }

            }
            displayText = displayText.replace(
                context.getString(R.string.separator_comma).toRegex(),
                emptyString
            )

            if (displayText.isEmpty()) {
                selectedSectionListener(selectedPosition, null)
            } else {
                selectedSectionListener(selectedPosition, displayText)
            }

            val receivedDataList = mutableListOf<MultipleChoiceSpinnerFields>()
            receivedDataList.addAll(adapterList)
            spinnerList.clear()
            spinnerList.addAll(receivedDataList)

            if (this.selectedItemPosition % 2 == 0) {
                this.setSelection(1)
            } else {
                this.setSelection(0)
            }
            this.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val dispTxt = displayText.ifEmpty { placeholderText }
                    val text = view?.findViewById<CheckedTextView>(android.R.id.text1)
                    text?.text = dispTxt
                    text?.gravity = Gravity.CENTER_HORIZONTAL
                    val cb = view?.findViewById<CheckBox>(R.id.cbCheck)
                    cb?.visibility = View.GONE
                    displayText = emptyString
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        }
        this.adapter = adapter
    }

}