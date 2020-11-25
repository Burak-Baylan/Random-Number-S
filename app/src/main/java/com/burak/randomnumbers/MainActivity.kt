package com.burak.randomnumbers

import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.burak.randomnumbers.fragments.GelismisSayilarArasiFragment
import com.burak.randomnumbers.fragments.SayilarArasiFragment
import com.burak.randomnumbers.fragments.ZarFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var toast : Toast? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.history_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_history){

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            val array = arrayOf("RNG History","DICE History")
            val adapter = ArrayAdapter(this,R.layout.list_row_settings,R.id.textView4,array)
            val listForHistory = ListView(this)

            val linearLayout = LinearLayout(this)
            linearLayout.addView(listForHistory)

            linearLayout.setPadding(10,30,10,0)

            listForHistory.adapter = adapter

            try {

                /*************************************************************************************************/
                val cursor2 = mDBSayilar?.rawQuery("SELECT * FROM historysayilar",null)

                val historyIxx = cursor2?.getColumnIndex("history")

                var b = ""
                while (cursor2!!.moveToNext()){
                    b = cursor2.getString(historyIxx!!)

                    var bLenght = b.length
                    b = b.substring(2,bLenght)
                }

                if (b.length < 2){
                    b = "Here is empty :/"
                }
                /*************************************************************************************************/



                var cursor = mDBZar?.rawQuery("SELECT * FROM historyzar",null)

                var historyIx = cursor?.getColumnIndex("history")

                var a = ""
                while (cursor!!.moveToNext()){
                    a = cursor.getString(historyIx!!)

                    val aLenght = a.length
                    a = a.substring(2,aLenght)

                    //Toast.makeText(this,"$a",Toast.LENGTH_SHORT).show()
                }

                if (a.length < 2){
                    a = "Here is empty :/"
                }

                var alert = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                alert.setView(linearLayout)
                //alert.setTitle("History")
                alert.setCancelable(false)
                //alert.setMessage("$a")

                listForHistory.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                    if (position == 0) {

                        //Toast.makeText(this,"RNG",Toast.LENGTH_SHORT).show()
                        val alertRng = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                        alertRng.setMessage("$b")
                        alertRng.setPositiveButton("OK") {dialogRng : DialogInterface, i : Int ->
                            dialogRng.cancel()
                        }
                        alertRng.setNeutralButton("Clear") {dialogRng : DialogInterface, i : Int ->
                            val alertBilmem = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                            alertBilmem.setCancelable(false)
                            //alertBilmem.setTitle("Clear")
                            alertBilmem.setMessage("If you clear history you can't get it back!")
                            alertBilmem.setPositiveButton("Clear") {dialogRng : DialogInterface, i : Int ->
                                try{
                                    mDBSayilar?.execSQL("DELETE FROM historysayilar")
                                    b = "Here is empty :/"
                                    Toast.makeText(this,"Cleaned",Toast.LENGTH_SHORT).show()
                                }
                                catch (e : Exception){
                                    e.printStackTrace()
                                    Toast.makeText(this,"Could not be deleted. Try again.",Toast.LENGTH_SHORT).show()
                                }
                                dialogRng.cancel()
                            }
                            alertBilmem.setNegativeButton("Cancel") {dialogRng : DialogInterface, i : Int ->
                                dialogRng.cancel()
                            }
                            alertBilmem.setNeutralButton("Back") {dialogRng : DialogInterface, i : Int ->
                                dialogRng.cancel()
                                alertRng.show()
                            }
                            alertBilmem.show()
                        }
                        alertRng.show()
                    }
                    /*else if (position == 1) {
                        Toast.makeText(this,"......",Toast.LENGTH_SHORT).show()
                    }*/
                    else if (position == 1) {
                        val alert2 = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                        alert2.setMessage("$a")
                        alert2.setPositiveButton("OK") {dialogs : DialogInterface, i : Int ->
                            dialogs.cancel()
                        }
                        alert2.setNeutralButton("Clear") {dialogs : DialogInterface, i : Int ->

                            val alert3 = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                            //alert3.setTitle("Clear")
                            alert3.setCancelable(false)
                            alert3.setMessage("If you clear history you can't get it back!")
                            alert3.setPositiveButton("Clear") {dialog2 : DialogInterface, b : Int ->
                                try{
                                    mDBZar?.execSQL("DELETE FROM historyzar")
                                    a = "Here is empty :/"
                                    Toast.makeText(this,"Cleaned",Toast.LENGTH_SHORT).show()
                                }
                                catch (e : Exception){
                                    e.printStackTrace()
                                    Toast.makeText(this,"Could not be deleted. Try again.",Toast.LENGTH_SHORT).show()
                                }
                                dialog2.cancel()
                            }
                            alert3.setNegativeButton("Cancel") {dialog2 : DialogInterface, a : Int ->
                                dialog2.cancel()
                            }
                            alert3.setNeutralButton("Back") {dialog2 : DialogInterface, a : Int ->
                                dialog2.cancel()
                                alert2.show()
                            }
                            alert3.show()
                        }
                        alert2.show()
                    }
                }
                alert.setPositiveButton("Done") {dialog : DialogInterface, d : Int ->
                    dialog.cancel()
                }
                alert.show()
            }
            catch (e : Exception){
                println(e.printStackTrace())
                Toast.makeText(this,"History get error!",Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    var mDBZar : SQLiteDatabase? = null
    var mDBSayilar : SQLiteDatabase? = null
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /******************************************************************************************/

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        /******************************************************************************************/


        try {
            mDBZar = this.openOrCreateDatabase("ZarHistory", MODE_PRIVATE, null)
            mDBZar?.execSQL("CREATE TABLE IF NOT EXISTS historyzar (id INTEGER PRIMARY KEY, history VARCHAR)")

            //Toast.makeText(this,"geldi",Toast.LENGTH_SHORT).show()
        }
        catch (e : Exception){
        }

        try {
            mDBSayilar = this.openOrCreateDatabase("SayilarHistory", Context.MODE_PRIVATE, null)
            mDBSayilar?.execSQL("CREATE TABLE IF NOT EXISTS historysayilar (id INTEGER PRIMARY KEY, history VARCHAR)")
        }
        catch (e : Exception){
            println(e.localizedMessage)
        }

        supportActionBar?.title = "RNG"

        val sayilarArasi = SayilarArasiFragment()
        val gelismisSayilarArasi = GelismisSayilarArasiFragment()
        val zar = ZarFragment()


        makeCurrentFragment(sayilarArasi)


        bottom_navigation.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.main_home){

                supportActionBar?.title = "RNG"
                makeCurrentFragment(sayilarArasi)
            }
            /*else if (it.itemId == R.id.main_favorite){

                supportActionBar?.title = "???????"
                makeCurrentFragment(gelismisSayilarArasi)
            }*/
            else if (it.itemId == R.id.main_dice){

                supportActionBar?.title = "DICE"
                makeCurrentFragment(zar)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment : Fragment) =
        supportFragmentManager
            .beginTransaction().apply {
                replace(R.id.fl_wrapper,fragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
}