package com.bangkit.capstone.lukaku.data.resources

import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.data.models.Headline

object HeadlineData {

    fun getHeadlines(): List<Headline> = HEADLINE

    private val HEADLINE_1: Headline =
        Headline(R.drawable.headline1, "Mendaki Gunung", "Siapkan perobatan ini sebelum mendaki gunung.")

    private val HEADLINE_2: Headline =
        Headline(R.drawable.headline2, "Mengganti Perban", "Langkah yang tepat saat mengganti perban.")

    private val HEADLINE_3: Headline =
        Headline(
            R.drawable.headline3,
            "Menyembuhkan Luka",
            "Obat semprot serba guna untuk luka ringan."
        )

    private val HEADLINE: ArrayList<Headline> = arrayListOf(HEADLINE_1, HEADLINE_2, HEADLINE_3)
}