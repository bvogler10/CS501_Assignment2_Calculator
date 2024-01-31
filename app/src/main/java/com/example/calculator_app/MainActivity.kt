package com.example.calculator_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var inputTextView: TextView
    private lateinit var resultsTV: TextView

    private fun isNumeric(input: String): Boolean {
        return try {
            input.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun isNumericOrOperator(input: String): Boolean {
        return try {
            input.toDouble()
            true
        } catch (e: NumberFormatException) {
            //used ChatGPT for the 3 lines below to check for Operators
            when (input) {
                "+", "-", "*", "/", "√" -> true
                else -> false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputTextView = findViewById(R.id.workingsTV)
        resultsTV = findViewById(R.id.resultsTV)
    }

    private var canAddOperation = false
    private var canAddDecimal = false

    fun numberAction(view: View) {
        if (view is Button){
            if (view.text == ".") {
                if (canAddDecimal)
                    inputTextView.append(view.text)
                canAddDecimal = false
            }
            else
                inputTextView.append(view.text)

            canAddOperation = true
        }
    }
    fun inputOperation(view: View) {
        if (view is Button && canAddOperation){
            inputTextView.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun allClear(view: View) {
        inputTextView.text = ""
        resultsTV.text = ""
    }
    fun backSpace(view: View) {
        val length = inputTextView.length()
        if(length > 0) {
            inputTextView.text = inputTextView.text.subSequence(0, length - 1)
        }
    }
    fun equalsAction(view: View) {
        resultsTV.text = calculateResult()
    }

    private fun calculateResult(): CharSequence? {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        var result = 0.0
        var currentOperator = ""

        for (item in digitsOperators) {
            if (isNumeric(item)) {
                val operand = item.toDouble()
                if (currentOperator.isEmpty()) {
                    result = operand
                } else {
                    when (currentOperator) {
                        "+" -> result += operand
                        "-" -> result -= operand
                        "*" -> result *= operand
                        "/" -> result /= operand
                    }
                }
            } else if (isNumericOrOperator(item)) {
                if (item == "√") {
                    result = sqrt(result)
                    break
                } else {
                    currentOperator = item
                }
            } else {
                Toast.makeText(this, "Please make sure all inputs are numerical or operators!", Toast.LENGTH_SHORT).show()
                return ""
            }
        }
        return result.toString()
    }

    private fun digitsOperators(): MutableList<String> {
        val list = mutableListOf<String>()
        var currentDigit = ""
        for(character in inputTextView.text) {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit)
                currentDigit = ""
                list.add(character.toString())
            }
        }
        if (currentDigit != "")
            list.add(currentDigit)

        return list
    }
}