package com.mojhan.priceCalculator

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText


class SpinnerActivity(
    private val mainActivity: MainActivity,
    private val taxProvinces: List<Map<String, Double>>,
    private val etCustomTax: EditText,
) : AdapterView.OnItemSelectedListener {


    private var taxPercentage = 0.0
    private var selectedProvince = "null"


    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        //Retrieving selected province
        selectedProvince = parent.getItemAtPosition(pos).toString()
        Log.i(TAG, "selected province: $selectedProvince")

        //Enable custom tax if N/A
        if (pos == 0)
            etCustomTax.isEnabled = true
        else
            etCustomTax.isEnabled = false

        //Retrieving tax percentage of selected province
        for (map in taxProvinces) {
            Log.i(TAG, "searching for selected province")
            if (map.containsKey(selectedProvince)) {
                Log.i(TAG, "selectedProvince $map[selectedProvince]")
                taxPercentage = map[selectedProvince] ?: 0.0
                break
            }
        }
        mainActivity.computeEverything()
    }


    fun setTaxPercentage(customTax: Double) {
        taxPercentage = customTax
    }

    fun getTaxPercentage(): Double {
        return taxPercentage
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    companion object {
        const val TAG = "SpinnerActivity"
    }
}