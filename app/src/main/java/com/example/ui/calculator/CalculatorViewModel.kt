package com.example.ui.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License
import org.mariuszgromada.math.mxparser.mXparser

class CalculatorViewModel : ViewModel() {
    init {
        License.iConfirmNonCommercialUse("developer")
        mXparser.disableImpliedMultiplicationMode()
    }

    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _isShift = MutableStateFlow(false)
    val isShift: StateFlow<Boolean> = _isShift.asStateFlow()

    private val _isAlpha = MutableStateFlow(false)
    val isAlpha: StateFlow<Boolean> = _isAlpha.asStateFlow()

    fun onKeyPress(btn: CalcButton) {
        val shift = _isShift.value
        val alpha = _isAlpha.value
        
        var inserted = false

        if (btn.mainText == "SHIFT") {
            _isShift.value = !shift
            _isAlpha.value = false
            return
        }
        if (btn.mainText == "ALPHA") {
            _isAlpha.value = !alpha
            _isShift.value = false
            return
        }

        val textToInsert = if (shift && btn.shiftText != null) {
            btn.shiftText
        } else if (alpha && btn.alphaText != null) {
            btn.alphaText
        } else {
            btn.mainText
        }

        _isShift.value = false
        _isAlpha.value = false

        if (textToInsert == "AC") {
            _expression.value = ""
            _result.value = ""
            return
        }

        if (textToInsert == "DEL") {
            if (_expression.value.isNotEmpty()) {
                _expression.value = _expression.value.dropLast(1)
                evaluatePartial()
            }
            return
        }

        if (textToInsert == "=") {
            evaluate()
            return
        }

        val expMap = mapOf(
            "×" to "*",
            "÷" to "/",
            "x²" to "^2",
            "x⁻¹" to "^-1",
            "xⁿ" to "^",
            "√" to "sqrt(",
            "³√" to "cbrt(",
            "x√" to "^(1/",
            "log" to "log10(",
            "ln" to "ln(",
            "10^x" to "10^",
            "e^x" to "e^",
            "x!" to "!",
            "(-)" to "-",
            "π" to "pi",
            "×10^x" to "*10^",
            "sin" to "sin(",
            "cos" to "cos(",
            "tan" to "tan(",
            "sin⁻¹" to "asin(",
            "cos⁻¹" to "acos(",
            "tan⁻¹" to "atan(",
            "nPr" to "nP",
            "nCr" to "nC",
            "%" to "#%"
        )

        val mapped = expMap[textToInsert] ?: textToInsert
        _expression.value += mapped
        evaluatePartial()
    }

    private fun evaluatePartial() {
        if (_expression.value.isEmpty()) {
            _result.value = ""
            return
        }
        val e = Expression(_expression.value)
        val r = e.calculate()
        if (!r.isNaN()) {
            val resStr = if (r % 1.0 == 0.0) r.toLong().toString() else r.toString()
            _result.value = resStr
        } else {
            _result.value = ""
        }
    }

    private fun evaluate() {
        if (_expression.value.isEmpty()) return
        val e = Expression(_expression.value)
        val r = e.calculate()
        if (!r.isNaN()) {
            val resStr = if (r % 1.0 == 0.0) r.toLong().toString() else r.toString()
            _expression.value = resStr
            _result.value = ""
        } else {
            _result.value = "Error"
        }
    }
}
