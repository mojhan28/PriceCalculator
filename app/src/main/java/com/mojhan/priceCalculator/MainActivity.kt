package com.mojhan.priceCalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher


//added imports:
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner

//tool bar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {

    /**
    LATE INITIALIZATION:
    - initializing inside onCreate method and not in the constructor
    - variable equal to name of ID
    - used to declare a non-null variable that will be initialized later.
     */
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarMonitor: TipSeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var etNumberPeople: EditText
    private lateinit var tvTotalPerson: TextView
    private lateinit var tvPerPerson: TextView
    private lateinit var tvTaxAmount: TextView
    private lateinit var spinner: Spinner
    private lateinit var spinnerMonitor: SpinnerActivity
    private lateinit var etCustomTaxPercentage: EditText
    private lateinit var etDiscount: EditText

    private var discountPercentage: Double = 0.0

    /**
     * The onCreate function is a key lifecycle method in Android development, particularly for activities.
     * It is called when an activity is first created, and it is where you should
     * - initialize your activity,
     * - set up the user interface, and
     * - perform initial setup that only needs to happen once for the entire lifespan of the activity.
     *
     * Takes a Bundle parameter. This Bundle contains any state previously saved in the onSaveInstanceState
     * method. It can be used to restore the activity's state if it was previously terminated and is now being recreated.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Calling super.onCreate(savedInstanceState) within the onCreate method is essential for ensuring that the base
         * class's implementation of onCreate is executed.
         *
         * When you call super.onCreate(savedInstanceState), you are invoking the onCreate method defined in the parent
         * class of your activity, which is typically AppCompatActivity or Activity.
         */
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        //TOOLBAR

        // Find the toolbar view inside the activity layout
        val toolbar: Toolbar = findViewById(R.id.mytoolbar)

        // Set the Toolbar as the action bar
        setSupportActionBar(toolbar)

        // Set the title
        supportActionBar?.title = "Price calculator"


        /**
         * INITIALIZE VIEWS:
         * findViewById is a method in Android used to find and return a view that was previously defined in an
         * XML layout file. It is a way to access UI components (such as TextView, Button, EditText, etc.) in
         * your activity or fragment.
        */
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBar = findViewById(R.id.seekBar)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etNumberPeople = findViewById(R.id.etNumberPeople)
        tvTotalPerson = findViewById(R.id.tvTotalPerson)
        tvPerPerson = findViewById(R.id.tvPerPerson)
        tvTaxAmount = findViewById(R.id.tvTaxAmount)
        spinner = findViewById(R.id.spinner)
        etCustomTaxPercentage = findViewById(R.id.etCustomTaxPercentage)
        etDiscount = findViewById(R.id.etDiscount)


        //SPINNER

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.CANProvinces,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter

            Log.d("SpinnerActivity", "ArrayAdapter set to Spinner successfully.")
        }

        //Parsing taxes document
        val taxProvincesList = parseTaxProvincesXml(this)

        spinnerMonitor =
            SpinnerActivity(
                this,
                taxProvincesList,
                etCustomTaxPercentage,
            )
        // Set spinner listener
        spinner.onItemSelectedListener = spinnerMonitor



        //SEEK BAR

        // Create an instance of TipSeekBar and set it as the listener for seekBar
        seekBarMonitor =
            TipSeekBar(
                tvTipPercentLabel,
                tvTipDescription,
                seekBar,
                this,
        )

        // Set SeekBar listener
        seekBar.setOnSeekBarChangeListener(seekBarMonitor)

        // Initialize SeekBar so that all parameters are at 15%
        seekBarMonitor.initialize()



        //BASE AMOUNT UPDATED
        //defining anonymous class which implements interface TextWatcher
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            //s: what user is typing
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "base amount: $s")
                computeEverything()
                computeTotalPerPerson()
            }
        })


        //NUMBER OF PPL SPLITTING UPDATED
        etNumberPeople.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "number ppl splitting: $s")
                computeTotalPerPerson()
            }
        })


        //CUSTOM TAX
        etCustomTaxPercentage.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                //etCustomTaxPercentage.isEnabled = true

                if (s == null || s.toString().isEmpty())
                    spinnerMonitor.setTaxPercentage(0.0)
                else {
                    val customTax = s.toString().toDouble()
                    spinnerMonitor.setTaxPercentage(customTax)
                }
                computeEverything()
            }
        })


        //DISCOUNT
        etDiscount.addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                discountPercentage = if (s.toString().isNotEmpty())
                    s.toString().toDouble()
                else
                    0.0
                computeEverything()
            }
        })

    }

    fun computeEverything() {
        var baseAmount = 0.0

        if (etBaseAmount.text.toString().isNotEmpty())
            baseAmount = etBaseAmount.text.toString().toDouble()

        //2. Retrieve base amount, tax %, tip % and discount %
        val taxPercentage = spinnerMonitor.getTaxPercentage()
        val tipPercent = seekBar.progress.toDouble()

        //3. Compute tax, tip, discount and total amounts
        val tipAmount = (tipPercent / 100) * baseAmount
        val taxAmount = (taxPercentage / 100) * baseAmount
        val discountAmount = (discountPercentage / 100) * baseAmount
        val totalAmount = baseAmount + tipAmount + taxAmount - discountAmount

        //4. Update the UI to show those values
        tvTipAmount.text = getString(R.string._2f).format(tipAmount)
        tvTaxAmount.text = getString(R.string._2f).format(taxAmount)
        tvTotalAmount.text = getString(R.string._2f).format(totalAmount)

        computeTotalPerPerson()
    }



    fun computeTotalPerPerson() {
        val numberPeople = etNumberPeople.text.toString().toDoubleOrNull()

        //not displaying anything if not splitting
        if (numberPeople == null || numberPeople == 1.0 || numberPeople == 0.0) {
            tvTotalPerson.text = getString(R.string.empty)
            tvPerPerson.text = getString(R.string.empty)
            return
        }

        try {
            //retrieve total amount
            val totalAmount = tvTotalAmount.text.toString().toDouble()

            //compute total per person
            val totalPerPerson = totalAmount / numberPeople

            //display total per person
            tvTotalPerson.text = getString(R.string._2f).format(totalPerPerson)
            tvPerPerson.text = getString(R.string.per_person)
        }
        catch(e: IllegalArgumentException) {    //exception if entering non-integers
            return
        }

        catch (e: NullPointerException) {
            tvTotalPerson.text = getString(R.string.empty)
            tvPerPerson.text = getString(R.string.empty)
            return
        }

    }

    companion object {
        const val TAG = "MainActivity"
    }

}

