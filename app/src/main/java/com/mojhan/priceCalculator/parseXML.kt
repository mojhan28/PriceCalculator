package com.mojhan.priceCalculator

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

import android.content.Context
import android.util.Log


fun parseTaxProvincesXml(context: Context): List<Map<String, Double>> {
    val taxProvincesList = mutableListOf<Map<String, Double>>()
    val TAG = "parseTaxProvincesXml"
    Log.i(TAG, "reached taxProvincesList")

    try {
        val inputStream = context.resources.openRawResource(R.raw.taxprovinces)
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(inputStream)

        val rows: NodeList = doc.getElementsByTagName("row")
        // Define a TAG for logging
        val TAG = "parseTaxProvincesXml"
        Log.i(TAG, "reached val rows")

        for (i in 0 until rows.length) {
            val element = rows.item(i) as Element
            val abb = element.getElementsByTagName("Abb").item(0).textContent
            val province = element.getElementsByTagName("Province").item(0).textContent
            val gstAndHst = element.getElementsByTagName("GSTandHST").item(0).textContent
            val pst = element.getElementsByTagName("PST").item(0).textContent

            //Strip % symbol off taxes
            val gstAndHstAsDouble = gstAndHst.replace("%", "").toDouble()
            val pstAsDouble = pst.replace("%", "").toDouble()

            val tax = gstAndHstAsDouble + pstAsDouble

            // Define a TAG for logging
            Log.i(TAG, "Processing province: $province with abb: $abb and tax: $tax")

            taxProvincesList.add(mapOf(abb to tax))

        }
    } catch (e: Exception) {
        val TAG = "parseTaxProvincesXml"
        Log.i(TAG, "caught exception")
        e.printStackTrace()
    }

    return taxProvincesList
}

