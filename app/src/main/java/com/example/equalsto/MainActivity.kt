package com.example.equalsto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.resultTV
import kotlinx.android.synthetic.main.activity_main.workingsTV

class MainActivity : AppCompatActivity() {
    private var canAddOperation=false
    private var canAddDecimal= true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View) {
        if(view is Button){
            if(view.text==".") {
                if (canAddDecimal) {
                    workingsTV.append(view.text)

                }
                canAddDecimal = false
            }

        else{
            workingsTV.append(view.text)
        }
        canAddOperation = true
    }}
    fun operationAction(view: View) {
        if(view is Button && canAddOperation){
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true

        }
    }

    fun allClearAction(view: View) {
        workingsTV.text = ""
        resultTV.text = ""
    }
    fun backSpaceAction(view: View) {
        val length = workingsTV.length()
    if(length > 0)
        workingsTV.text=workingsTV.text.subSequence(0,length-1)
    }
    fun equalsAction(view: View) {
        resultTV.text = calculateResults()
    }
    private fun calculateResults(): String{
        val digitOperators = digitsOperator()
        if(digitOperators.isEmpty())return ""
        val timeDivision = timesDivisionCalculate(digitsOperator())
        if(timeDivision.isEmpty()) {
            return ""
        }
        val result = addSubstractCalculate(timeDivision)
        return result.toString()
    }

    private fun addSubstractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
            for (i in passedList.indices) {
                if (passedList[i] is Char && i != passedList.lastIndex) {
                    val operator = passedList[i]
                    var nextDigit = passedList[i + 1] as Float
                    if (operator == '%') {
                        nextDigit = (result*nextDigit)/100
                    }
                    if (operator == '+') {
                        result += nextDigit
                    }
                    if (operator == '-') {
                        result -= nextDigit
                    }
                    if (operator == 'x') {
                        result *= nextDigit
                    }
                    if (operator == '/') {
                        result /= nextDigit
                    }

                }
            }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list  = passedList
        while(list.contains("x") || list.contains("/")){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for(i in passedList.indices){
            if(passedList [i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit  = passedList [i+1] as Float
                when (operator){
                    'x' ->
                    {

                        newList.add(prevDigit*nextDigit)
                        restartIndex = i+1

                    }
                    '/' ->
                    {

                        newList.add(prevDigit/nextDigit)
                        restartIndex = i+1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }

            }
            if(i>restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitsOperator():MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingsTV.text){
            if(character.isDigit() || character == '.'){
                currentDigit += character
            }else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }
        return list
    }

    fun DecimalAction(view: View) {
        if (view is Button) {
            if (canAddDecimal) {
                workingsTV.append(view.text)
            }
            canAddDecimal = false
        }
    }

    fun percentAction(view: View) {
        if (view is Button) {
            // Convert the current displayed number to a percentage
            val currentValue = workingsTV.text.toString().toFloat()
            val percentageValue = currentValue / 100
            workingsTV.text = percentageValue.toString()
        }
    }


}