package com.burak.randomnumbers.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.solver.widgets.ConstraintAnchor
import com.burak.randomnumbers.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_sayilar_arasi.*
import kotlinx.android.synthetic.main.fragment_sayilar_arasi.view.*
import kotlinx.android.synthetic.main.fragment_zar.view.*
import java.util.concurrent.ThreadLocalRandom

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SayilarArasiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SayilarArasiFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var toast : Toast? = null
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        numbersCounter = 0
        numbersNeedReal = 1
        numberExclude = null
        MobileAds.initialize(activity)
        /*errorCounter = 0
        i = 0*/
    }

    var numbersCounter : Int = 0
    var numbersNeedReal : Int = 1
    var numberExclude : Int? = null
    /*var errorCounter : Int = 0
    var i : Int = 0*/

    var mDB : SQLiteDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sayilar_arasi, container, false)

        MobileAds.initialize(activity)
        mAdView = view.findViewById(R.id.adView1)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        try {
            mDB = activity?.openOrCreateDatabase("SayilarHistory", Context.MODE_PRIVATE, null)
            mDB?.execSQL("CREATE TABLE IF NOT EXISTS historysayilar (id INTEGER PRIMARY KEY, history VARCHAR)")
        }
        catch (e : Exception){
            println(e.localizedMessage)
        }

        view.delete1.setOnClickListener {
            view.numberOne.setText("")
        }
        view.delete2.setOnClickListener {
            view.numberTwo.setText("")
        }

        view.excludeTextView.text = "?"

        view.clearText.setOnClickListener {


            var gelenString = ""

            try {
                try {
                    val cursor = mDB?.rawQuery("SELECT * FROM historysayilar", null)

                    val historyIx = cursor?.getColumnIndex("history")

                    while (cursor!!.moveToNext()) {
                        gelenString = cursor.getString(historyIx!!)
                    }
                    //Toast.makeText(activity,"gelen",Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {

                }

                val result = view.cevapText.text.toString()


                val from = view.numberOneText.text.toString()
                val to = view.numberTwoText.text.toString()
                val allStrings = "----\nFrom $from to $to\n\n$result\n\nAmount Requested: $numbersNeedReal\nExclude Number: $numberExclude\n----"

                gelenString += "\n\n$allStrings"

                mDB?.execSQL("DELETE FROM historysayilar")
                mDB?.execSQL("INSERT INTO historysayilar (history) VALUES (?)", arrayOf(gelenString))
            }
            catch (e : Exception){
                showToast("History save error!", Toast.LENGTH_SHORT)
                println(e.stackTrace)
            }


            numbersCounter = 0
            /*errorCounter = 0
            i = 0*/
            cevapText.text = ""
            mLayout.visibility = View.INVISIBLE
            detailsText.visibility = View.INVISIBLE
            randomButton.visibility = View.VISIBLE
        }

        view.deleteOptions.setOnClickListener {

            numbersNeedReal = 1
            numberExclude = null

            view.deleteOptions.visibility = View.INVISIBLE

            view.istenilenMiktarText.text = "$numbersNeedReal"
            view.excludeTextView.text = "?"
        }

        view.optionsText.setOnClickListener {

            var numbersNeed : Int = 1

            var linearLayout = LinearLayout(activity)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(50,0,0,0)

            var linearLayout2 = LinearLayout(activity)
            linearLayout2.orientation = LinearLayout.HORIZONTAL

            var textForHowMany = TextView(activity)
            textForHowMany.text = "How many numbers do you need?"
            textForHowMany.setTextColor(Color.rgb(255,255,255))
            linearLayout2.addView(textForHowMany)

            var howManySpinner : Spinner = Spinner(activity)
            var items : Array<String> = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12")
            var adapter : ArrayAdapter<String> = ArrayAdapter(activity!!,R.layout.support_simple_spinner_dropdown_item,items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            howManySpinner.adapter = adapter

            howManySpinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    when(position){
                        0 -> { numbersNeed = 1 }
                        1 -> { numbersNeed = 2 }
                        2 -> { numbersNeed = 3 }
                        3 -> { numbersNeed = 4 }
                        4 -> { numbersNeed = 5 }
                        5 -> { numbersNeed = 6 }
                        6 -> { numbersNeed = 7 }
                        7 -> { numbersNeed = 8 }
                        8 -> { numbersNeed = 9 }
                        9 -> { numbersNeed = 10 }
                        10 -> { numbersNeed = 11 }
                        11 -> { numbersNeed = 12 }
                    }

                    //Toast.makeText(activity,"$numbersNeed",Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                    //Toast.makeText(activity,"$numbersNeed",Toast.LENGTH_SHORT).show()
                }
            }

            linearLayout2.addView(howManySpinner)
            linearLayout.addView(linearLayout2)

            val linearLayout3 = LinearLayout(activity)
            linearLayout3.orientation = LinearLayout.HORIZONTAL

            val textForExclude = TextView(activity)
            textForExclude.text = "Exclude this number:"
            textForExclude.setTextColor(Color.rgb(255,255,255))
            linearLayout3.addView(textForExclude)

            val editTextForExclude = EditText(activity)
            editTextForExclude.inputType = InputType.TYPE_CLASS_NUMBER
            editTextForExclude.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(9))
            editTextForExclude.width = 230
            editTextForExclude.setTextColor(Color.rgb(255,255,255))
            linearLayout3.addView(editTextForExclude)

            linearLayout.addView(linearLayout3)
            val alert = AlertDialog.Builder(activity,R.style.CustomAlertDialog)
            //alert.setTitle("Options")
            alert.setView(linearLayout)
            alert.setCancelable(false)
            alert.setPositiveButton("Save") {dialog : DialogInterface, i : Int ->

                numbersNeedReal = numbersNeed
                view.istenilenMiktarText.text = "$numbersNeedReal"
                view.deleteOptions.visibility = View.VISIBLE

                if (editTextForExclude.length() >= 1) {
                    numberExclude = editTextForExclude.text.toString().toInt()
                    view.excludeTextView.text = "$numberExclude"
                    //Toast.makeText(activity,"alındı",Toast.LENGTH_SHORT).show()
                }
            }
            alert.setNegativeButton("Cancel") {dialog : DialogInterface, i : Int ->
                /*numbersNeedReal = 1
                view.istenilenMiktarText.text = "$numbersNeedReal"
                view.excludeTextView.text = "?"*/
                dialog.cancel()
            }
            alert.show()

        }

        var errorCounterReal = 0
        var errorBool = false

        view.detailsText.setOnClickListener {

            val linearLayout = LinearLayout(activity)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(50,20,50,0)

            val istenilenMiktar = TextView(activity)
            istenilenMiktar.setTextColor(Color.rgb(255,255,255))
            istenilenMiktar.textSize = 16f
            istenilenMiktar.text = "How many numbers did I want? : $numbersNeedReal"
            linearLayout.addView(istenilenMiktar)

            val kacSayiVar = TextView(activity)
            kacSayiVar.setTextColor(Color.rgb(255,255,255))
            kacSayiVar.textSize = 16f
            kacSayiVar.text = "How many numbers are there: $numbersCounter"
            kacSayiVar.setPadding(0,20,0,0)
            linearLayout.addView(kacSayiVar)

            if (numberExclude != null){
                val numberExcludeText = TextView(activity)
                numberExcludeText.setTextColor(Color.rgb(255,255,255))
                numberExcludeText.textSize = 16f
                numberExcludeText.text = "What number i don't want to include? : $numberExclude"
                numberExcludeText.setPadding(0,20,0,0)
                linearLayout.addView(numberExcludeText)
            }

            if (errorBool) {
                val excludeCameCounter = TextView(activity)
                excludeCameCounter.setTextColor(Color.rgb(255,255,255))
                excludeCameCounter.textSize = 16f
                excludeCameCounter.text = "How many times has the number come that I don't want to include? : $errorCounterReal"
                excludeCameCounter.setPadding(0,20,0,0)
                linearLayout.addView(excludeCameCounter)
            }


            var alert = AlertDialog.Builder(activity,R.style.CustomAlertDialog)
            alert.setView(linearLayout)
            alert.setPositiveButton("Okey") {dialog : DialogInterface, i : Int ->
                dialog.cancel()
            }
            alert.show()
        }



        view.randomButton.setOnClickListener {

            view.randomButton.visibility = View.INVISIBLE

            val num2 = numberTwoText.text.toString().toInt() + 1
            val num1 = numberOneText.text.toString().toInt()

            var i : Int = 0
            var errorCounter : Int = 0


            if (num1 == num2){
                showToast("Num1 cannot be equals Num2.",Toast.LENGTH_SHORT)
                view.randomButton.visibility = View.VISIBLE
            }
            else if (num1 < num2) {
                mLayout.visibility = View.VISIBLE
                while (i < numbersNeedReal) {
                    if (numbersCounter < 12) {
                        if (errorCounter < 12) {
                            val randomNumber = ThreadLocalRandom.current().nextInt(num1, num2)
                            if (randomNumber != numberExclude) {
                                if (numbersCounter < 1) {
                                    cevapText.setText("$randomNumber")
                                }
                                else {
                                    cevapText.setText("${cevapText.text}, $randomNumber")
                                }
                                numbersCounter++
                                if (numbersCounter > 4) {
                                    detailsText.visibility = View.VISIBLE
                                }
                                i++
                                view.randomButton.visibility = View.VISIBLE
                            }
                            else if (randomNumber == numberExclude) {
                                if (errorCounter < 12) {
                                    errorCounter++
                                    errorCounterReal++
                                    errorBool = true
                                    view.randomButton.visibility = View.VISIBLE
                                }
                            }
                        }
                        else {
                            i = 12
                            detailsText.visibility = View.INVISIBLE
                            mLayout.visibility = View.INVISIBLE
                            numbersCounter = 0
                            showToast("Somethings went wrong. Try again.", Toast.LENGTH_SHORT)
                            //showToast("Max 12 numbers",Toast.LENGTH_SHORT)
                            view.randomButton.visibility = View.VISIBLE
                        }
                    }
                    else {
                        i = 12
                        showToast("Max 12 numbers",Toast.LENGTH_SHORT)
                        view.randomButton.visibility = View.VISIBLE
                    }
                }
                /*showToast("Max 12 numbers",Toast.LENGTH_SHORT)
                view.randomButton.visibility = View.VISIBLE*/
            }
            else if (num1 > num2) {
                showToast("Num 1 cannot be greater than Num 2", Toast.LENGTH_SHORT)
                view.randomButton.visibility = View.VISIBLE
            }
        }

        view.numberOne.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    view.numberOneText.setText("0")
                    num1Falan(view)
                }
                else {
                    view.numberOneText.setText("$s")
                    num1Falan(view)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        view.numberTwo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().equals("")){
                    view.numberTwoText.setText("10")
                    num1Falan(view)
                }
                else {
                    view.numberTwoText.setText("$s")
                    num1Falan(view)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        return view
    }

    private fun num1Falan(view : View){
        val num1 = view.numberOneText.text.toString().toInt()
        val num2 = view.numberTwoText.text.toString().toInt()
        if (num1 > num2){
            view.warningImage.visibility = View.VISIBLE
            view.warningImage2.visibility = View.INVISIBLE
        }
        else if (num1 == num2){
            view.warningImage.visibility = View.VISIBLE
            view.warningImage2.visibility = View.VISIBLE
        }
        else{
            view.warningImage.visibility = View.INVISIBLE
            view.warningImage2.visibility = View.INVISIBLE
        }
    }

    private fun showToast (cumle : String, duration : Int){
        if (toast != null){
            toast!!.cancel()
        }
        toast = Toast.makeText(activity,cumle,duration)
        toast!!.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SayilarArasiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SayilarArasiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}