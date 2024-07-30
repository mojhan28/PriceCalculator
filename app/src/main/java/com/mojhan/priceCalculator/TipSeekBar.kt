package com.mojhan.priceCalculator

import android.animation.ArgbEvaluator
import android.widget.SeekBar
import android.widget.TextView
import android.util.Log
import androidx.core.content.ContextCompat


class TipSeekBar(
        private val tvTipPercentLabel: TextView,
        private val tvTipDescription: TextView,
        private val seekBarTip: SeekBar,
        private val mainActivity: MainActivity,
) : SeekBar.OnSeekBarChangeListener {


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        Log.i(TAG, "progress seek bar: $progress")
        tvTipPercentLabel.text = "$progress%"
        updateTipDescription(progress)
        mainActivity.computeEverything()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(p0: SeekBar?) {}

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Good"
            in 15..19 -> "Great"
            in 20..24 -> "Amazing"
            else -> "I'm rich"
        }
        tvTipDescription.text = tipDescription

        //update color
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(mainActivity, R.color.colorWorstTip),    //used to be (context, R.
            ContextCompat.getColor(mainActivity, R.color.colorBestTip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    fun initialize() {
        seekBarTip.progress = INITIAL_TIP_PERCENT  // Set initial SeekBar value
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"  // Set initial tip percent label
        updateTipDescription(INITIAL_TIP_PERCENT)  // Set initial tip description
    }

    companion object {
        private const val TAG = "TipSeekBar"
        private const val INITIAL_TIP_PERCENT = 15  // 15% tip by default
    }

}
