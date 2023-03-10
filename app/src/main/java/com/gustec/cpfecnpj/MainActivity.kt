package com.gustec.cpfecnpj

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.gustec.cpfecnpj.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.etNum.addTextChangedListener(MaskEditUtil.mask(binding.etNum))
        binding.btnCheck.setOnClickListener {
            when(binding.etNum.text.length){
                14 -> {
                    if(checkCpf(binding.etNum.text.toString())){
                        binding.tvCheck.text = "CPF válido"
                        binding.tvCheck.setTextColor(Color.GREEN)
                    }
                    else{
                        binding.tvCheck.text = "CPF inválido"
                        binding.tvCheck.setTextColor(Color.RED)
                    }
                }
                18 -> {
                    if(checkCnpj(binding.etNum.text.toString())){
                        binding.tvCheck.text = "CNPJ válido"
                        binding.tvCheck.setTextColor(Color.GREEN)
                    }
                    else{
                        binding.tvCheck.text = "CNPJ inválido"
                        binding.tvCheck.setTextColor(Color.RED)
                    }
                }
                else -> {
                    binding.tvCheck.text = "Não é um CPF nem CNPJ válido."
                    binding.tvCheck.setTextColor(getColor(R.color.purple_200))
                }
            }
        }
    }
    fun checkCpf(et: String): Boolean{
        var str = et.replace("-", "").replace("/","").replace(".","")
        var calc: Int
        var num = 10
        var sum = 0
        for(x in 0..8) {
            calc = str[x].toString().toInt() * num
            sum += calc
            --num
        }
        var rest = sum % 11
        var test = 11 - rest
        if(test > 9) test = 0
        if(test != str[9].toString().toInt()) return false
        num = 11
        sum = 0
        for(x in 0..9) {
            calc = str[x].toString().toInt() * num
            sum += calc
            --num
        }
        rest = sum % 11
        test = 11 - rest
        if(test > 9) test = 0
        if(test != str[10].toString().toInt()) return false
        return true
    }
    fun checkCnpj(et: String): Boolean{
        var str = et.replace("-", "").replace("/","").replace(".","")
        var calc: Int
        var num = 5
        var sum = 0
        for(x in 0..11) {
            calc = str[x].toString().toInt() * num
            sum += calc
            --num
            if(num == 1) num = 9
        }
        var rest = sum % 11
        var test = 11 - rest
        if(test < 2) test = 0
        if(test != str[12].toString().toInt()) return false
        num = 6
        sum = 0
        for(x in 0..12) {
            calc = str[x].toString().toInt() * num
            sum += calc
            --num
            if(num == 1) num = 9
        }
        rest = sum % 11
        test = 11 - rest
        if(test < 2) test = 0
        if(test != str[13].toString().toInt()) return false
        return true
    }
    object MaskEditUtil {
        fun mask(ediTxt: EditText): TextWatcher {
            var isUpdating: Boolean = false
            var mask = ""
            var old = ""
            return object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = unmask(s.toString())
                    var mascara = ""
                    when (str.length) {
                        in 0..11 -> mask = "###.###.###-##"
                        else -> mask = "##.###.###/####-##"
                    }
                    if (isUpdating) {
                        old = str
                        isUpdating = false
                        return
                    }
                    var i = 0
                    for (m in mask.toCharArray()) {
                        if (m != '#' && str.length > old.length) {
                            mascara += m
                            continue
                        }
                        try {
                            mascara += str[i]
                        } catch (e: Exception) {
                            break
                        }
                        i++
                    }
                    isUpdating = true
                    ediTxt.setText(mascara)
                    ediTxt.setSelection(mascara.length)
                }
            }
        }
        fun unmask(s: String): String {
            return s.replace("-", "").replace("/","").replace(".", "")
        }
    }
}