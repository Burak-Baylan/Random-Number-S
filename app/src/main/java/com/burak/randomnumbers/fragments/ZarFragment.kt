package com.burak.randomnumbers.fragments

import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import com.burak.randomnumbers.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_sayilar_arasi.*
import kotlinx.android.synthetic.main.fragment_zar.*
import kotlinx.android.synthetic.main.fragment_zar.view.*
import java.util.concurrent.ThreadLocalRandom

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [ZarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ZarFragment : Fragment() {
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.history_menu,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_history){

        }

        return super.onOptionsItemSelected(item)
    }

    var howManyIntDice = 1
    var howManySides = 6
    var mDB : SQLiteDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_zar, container, false)

        MobileAds.initialize(activity)
        mAdView = view.findViewById(R.id.adView2)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        try {
            mDB = activity?.openOrCreateDatabase("ZarHistory", MODE_PRIVATE, null)
            mDB?.execSQL("CREATE TABLE IF NOT EXISTS historyzar (id INTEGER PRIMARY KEY, history VARCHAR)")
        }
        catch (e : Exception){
            println(e.localizedMessage)
        }


        view.howManyBut1.setBackgroundResource(R.drawable.cust_button_selected)
        howManyIntDice = 1

        view.side6.setBackgroundResource(R.drawable.cust_button_selected)
        howManySides = 6

        val animate : AnimationDrawable = view.rollButton.background as AnimationDrawable
        animate.setEnterFadeDuration(500)
        animate.setExitFadeDuration(500)
        animate.start()

        view.howManyBut1.setOnClickListener {

            butBackgroundRemoveDice(view,view.howManyBut1)
            howManyIntDice = 1
        }
        view.howManyBut2.setOnClickListener {

            butBackgroundRemoveDice(view,view.howManyBut2)
            howManyIntDice = 2
        }
        view.howManyBut3.setOnClickListener {

            butBackgroundRemoveDice(view,view.howManyBut3)
            howManyIntDice = 3
        }
        view.howManyBut4.setOnClickListener {

            butBackgroundRemoveDice(view,view.howManyBut4)
            howManyIntDice = 4
        }



        view.side4.setOnClickListener {
            butBackgroundRemoveSides(view,view.side4)
            howManySides = 4
        }
        view.side6.setOnClickListener {
            butBackgroundRemoveSides(view,view.side6)
            howManySides = 6
        }
        view.side8.setOnClickListener {
            butBackgroundRemoveSides(view,view.side8)
            howManySides = 8
        }
        view.side12.setOnClickListener {
            butBackgroundRemoveSides(view,view.side12)
            howManySides = 12
        }
        view.side20.setOnClickListener {
            butBackgroundRemoveSides(view,view.side20)
            howManySides = 20
        }


        view.rollButton.setOnClickListener {

            mLinear.visibility = View.VISIBLE

            var a = 0
            var forSum = 0
            while (a < howManyIntDice) {

                val randomNumber = ThreadLocalRandom.current().nextInt(1, howManySides + 1)
                forSum = randomNumber + forSum
                view.sumText.visibility = View.INVISIBLE
                view.sumText.text = null
                if (howManyIntDice != 1){
                    sumText.text = "Sum: $forSum"
                    view.sumText.visibility = View.VISIBLE
                }
                if (a < 1) {
                    view.resultText.setText("$randomNumber")
                }
                else {
                    view.resultText.setText("${resultText.text}, $randomNumber")
                }
                a++
            }

            try {

                var gelenString = ""

                try {
                    var cursor = mDB?.rawQuery("SELECT * FROM historyzar",null)

                    var historyIx = cursor?.getColumnIndex("history")

                    while (cursor!!.moveToNext()){
                        gelenString = cursor.getString(historyIx!!)
                    }
                    //Toast.makeText(activity,"gelen",Toast.LENGTH_SHORT).show()
                }
                catch (e : Exception){

                }

                var resultToplamText= view.resultText.text.toString()
                var sumString = view.sumText.text.toString()

                var gidecekToplamText = "$resultToplamText \n$sumString"

                gelenString += "\n\n$gidecekToplamText"

                mDB?.execSQL("DELETE FROM historyzar")
                mDB?.execSQL("INSERT INTO historyzar (history) VALUES (?)", arrayOf(gelenString))

                //Toast.makeText(activity,"koyuldu",Toast.LENGTH_SHORT).show()
            }
            catch (e : Exception){

            }
        }
        return view
    }

    private fun butBackgroundRemoveDice(view : View, but : Button){
        view.howManyBut1.setBackgroundResource(R.drawable.cust_button)
        view.howManyBut2.setBackgroundResource(R.drawable.cust_button)
        view.howManyBut3.setBackgroundResource(R.drawable.cust_button)
        view.howManyBut4.setBackgroundResource(R.drawable.cust_button)


        but.setBackgroundResource(R.drawable.cust_button_selected)
    }

    private fun butBackgroundRemoveSides(view : View, but : Button){
        view.side4.setBackgroundResource(R.drawable.cust_button)
        view.side6.setBackgroundResource(R.drawable.cust_button)
        view.side8.setBackgroundResource(R.drawable.cust_button)
        view.side12.setBackgroundResource(R.drawable.cust_button)
        view.side20.setBackgroundResource(R.drawable.cust_button)


        but.setBackgroundResource(R.drawable.cust_button_selected)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ZarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ZarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}