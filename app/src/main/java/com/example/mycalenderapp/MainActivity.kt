package com.example.mycalenderapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.parseInt
import java.util.*
import android.widget.TextView
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Year.isLeap

import android.R.bool





class MainActivity : AppCompatActivity() {

    //Variables
    val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep",
        "Oct", "Nov", "Dec"
    )

    val years = arrayOf(
        "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
        "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026",
        "2027", "2028", "2029", "2030"
    )

    val monthDays = arrayOf("31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31")
    private lateinit var mmonthSpinner: Spinner
    private lateinit var myearSpinner: Spinner
    private var calender: Calendar = Calendar.getInstance()
    private var currentMonth = calender.get(Calendar.MONTH)
    private var currentYear = calender.get(Calendar.YEAR)
    lateinit var selectYear: String
    lateinit var selectMonth: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialization
        mmonthSpinner = monthSpinner
        myearSpinner = yearSpinner
        updateSpinners()
        selectYear = myearSpinner.selectedItem.toString()
        selectMonth = myearSpinner.selectedItem.toString()


        myearSpinner
            .setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    arg0: AdapterView<*>, arg1: View?,
                    pos: Int, id: Long
                ) {
                    jump()
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                    // TODO Auto-generated method stub
                }
            })

        mmonthSpinner
            .setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    arg0: AdapterView<*>, arg1: View?,
                    pos: Int, id: Long
                ) {
                    jump()
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {
                    // TODO Auto-generated method stub
                }
            })
    }

    private fun updateSpinners() {

        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)
        mmonthSpinner.adapter = monthAdapter

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
        myearSpinner.adapter = yearAdapter
        myearSpinner.setSelection(currentYear - 1990)
        mmonthSpinner.setSelection(currentMonth)

    }


    fun showCalander(month: Int, year: Int) {
        myearSpinner.setSelection(year - 1990)
        mmonthSpinner.setSelection(month)
        Log.e("month and year :", "" + month + " and " + year)
        var firstDay: Int = Date(year,month,1).day-1
        if(firstDay<0)firstDay+=7
        Log.e("First Day" , ""+firstDay)
        tableLayout.removeViews(1, Math.max(0, tableLayout.childCount - 1))
        updatedYear.text = year.toString()
        updatedMonth.text = months[month]
        var date = 1
        val textArray = arrayOfNulls<TextView>(7)
        val tr_head: Array<TableRow?> = arrayOfNulls<TableRow>(6)

        for (i in 0 until 6) {
            tr_head[i] = TableRow(this)
            tr_head[i]?.id = i + 1
            tr_head[i]?.setBackgroundColor(Color.GRAY)
            tr_head[i]?.layoutParams = TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            for (j in 0 until 7) {
                textArray[j] = TextView(this)
                textArray[j]!!.id = i + 111
                textArray[j]?.setTextColor(Color.WHITE)
                textArray[j]!!.textSize = 20f
                textArray[j]!!.setPadding(10, 5, 10, 5)

                if (i == 0 && j < firstDay) {
                    textArray[j]!!.text = ""

                    tr_head[i]!!.addView(textArray[j])
                } else if (date > daysInMonth(month, year)) {
                    break;
                } else {
                    textArray[j]!!.text = date.toString()
                    if (date == calender.get(Calendar.DATE) && year == calender.get(Calendar.YEAR) && month == calender.get(
                            Calendar.MONTH
                        )
                    ) {
                        textArray[j]?.setTextColor(Color.RED)
                    }
                    tr_head[i]!!.addView(textArray[j])
                    date++;
                }
            }
            tableLayout.addView(
                tr_head[i], TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

    }

    fun next(view: View) {
        if (currentMonth == 11) {
            currentYear += 1
        }
        currentMonth = (currentMonth + 1) % 12;
        showCalander(currentMonth, currentYear)
    }

    fun previous(view: View) {
        if (currentMonth == 0) {
            currentYear -= 1
            currentMonth = 11;
        } else {
            currentMonth -= 1
        }
        showCalander(currentMonth, currentYear)
    }

    fun jump() {
        selectYear = myearSpinner.selectedItem.toString()
        selectMonth = mmonthSpinner.selectedItemPosition.toString()
        currentYear = parseInt(selectYear)
        currentMonth = parseInt(selectMonth)
        showCalander(currentMonth, currentYear)
    }

    private fun daysInMonth(month: Int, year: Int): Int {
        if (month == 1 && leapYear(year)) return 29
        else if (month == 1 && !leapYear(year)) return 28
        if (month < 7) {
            if (month % 2 == 0) {
                return 31
            } else {
                return 30
            }
        } else {
            if (month % 2 == 0) {
                return 30
            } else {
                return 31
            }
        }
    }

    private fun leapYear(year: Int): Boolean {
        var leap = false

        if (year % 4 == 0) {
            if (year % 100 == 0) {
                // year is divisible by 400, hence the year is a leap year
                return year % 400 == 0
            } else
                return true
        }
        return false

    }

    fun getWeekOfYear(view: View) {
        if(yearTv.text.toString() == "" || monthTv.text.toString()  == ""|| dateTv.text.toString() ==""){
            Toast.makeText(this,"all entries are compulsary",Toast.LENGTH_SHORT).show()
            return
        }

        val year : Int = parseInt(yearTv.text.toString())
        val month : Int = parseInt(monthTv.text.toString())
        val day : Int = parseInt(dateTv.text.toString())
        if(!isValidDate(day,month,year)) {
            Toast.makeText(this,"Invalid date",Toast.LENGTH_SHORT).show()
            return
        }
        var firstDay: Int = Date(year,0,1).day-1
        if(firstDay<0)firstDay+=7
        var totalDays: Int = firstDay

        if(leapYear(year)){
            if(month > 1 || month == 1 && day == 29)
                totalDays += 1
        }
        for(i in 0 until month){
            totalDays += parseInt(monthDays[i])
        }
        totalDays += day
        var result = -1
        if(totalDays % 7 == 0){
            result= totalDays/7
        }
        result= totalDays/7 +1
        screen.text = "Date comes in "+result.toString()+"th week"
    }

    fun isValidDate(d: Int, m: Int, y: Int): Boolean {
        if (y > 2199 ||
            y < 1999
        ) return false
        if (m < 1 || m > 12) return false
        if (d < 1 || d > 31) return false
        if (m == 2) {
            return if (leapYear(y)) d <= 29 else d <= 28
        }
        return if (m == 4 || m == 6 || m == 9 || m == 11
        ) d <= 30
        else true
    }
}
