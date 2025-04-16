package com.example.kitchenkompanion

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var editButton: ImageButton
    private lateinit var backButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNameTextView = view.findViewById(R.id.userName)
        editButton = view.findViewById(R.id.editProfileButton)
        backButton = view.findViewById(R.id.backButton)

        val sharedPrefs = requireContext().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        val savedName = sharedPrefs.getString("user_name", null)
        savedName?.let {
            userNameTextView.text = it
        }

        editButton.setOnClickListener {
            showEditNameDialog()
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            requireActivity().findViewById<View>(R.id.fragmentOverlayContainer).visibility = View.GONE
        }
    }

    private fun showEditNameDialog() {
        val context = requireContext()
        val editText = EditText(context).apply {
            hint = "Enter your name"
            setText(userNameTextView.text.toString())
            setSelection(text.length)
        }

        AlertDialog.Builder(context)
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    userNameTextView.text = newName
                    val sharedPrefs = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
                    sharedPrefs.edit().putString("user_name", newName).apply()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
