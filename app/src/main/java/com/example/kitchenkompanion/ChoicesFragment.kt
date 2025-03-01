package com.example.kitchenkompanion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView

/*Button functionality https://www.youtube.com/watch?v=aJ8zsPbjdqc&ab_channel=MaskedProgrammer*/
class ChoicesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choices, container, false)

        val txt = view.findViewById<TextView>(R.id.result)
        val rgroup = view.findViewById<RadioGroup>(R.id.RadioGroup)
        val spinner =view.findViewById<Spinner>(R.id.spinner)
        val button = view.findViewById<Button>(R.id.button)
        val items = arrayOf("red","blue","yellow")

        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        button.setOnClickListener {
            val rID = rgroup.checkedRadioButtonId
            val rText = view.findViewById<RadioButton>(rID).text.toString()
            val spinnerText = spinner.selectedItem.toString()

            txt.setText("$rText $spinnerText")
        }

        return view
    }
}