package com.example.kitchenkompanion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar

/*Looked at this to figure out snackbar https://www.geeksforgeeks.org/how-to-add-a-snackbar-in-android*/
class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val img = view.findViewById<ImageView>(R.id.pfpimage)

        img.setOnClickListener {
            val alert = Snackbar
                .make(view, "Warning! Do not click on the profile image.",Snackbar.LENGTH_INDEFINITE)
            alert.setAction("close") {alert.dismiss()}
            alert.show()
        }

        return view
    }
}