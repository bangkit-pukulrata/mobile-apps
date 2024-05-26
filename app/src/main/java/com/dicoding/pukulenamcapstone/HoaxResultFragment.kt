package com.dicoding.pukulenamcapstone

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.mahozad.android.PieChart
import ir.mahozad.android.component.Alignment
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import android.graphics.Typeface

class HoaxResultFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_hoax_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil argumen yang dikirimkan dari fragment sebelumnya
        val predictedLabel = arguments?.getString("predictedLabel")
        val accuracy = arguments?.getFloat("accuracy")

        Log.d("HoaxResultFragment", "Accuray: $accuracy")


        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
        val decimalFormat = DecimalFormat("#.##", symbols)

        var hoaxPercentageString : String = ""
        var notHoaxPercentageString : String = ""
        var finalHoaxPercentage : String = ""
        var finalLabel : String = ""
        var hoaxFraction : Float = 0.0f
        var notHoaxFraction : Float = 0.0f

        if (accuracy != null) {
            val hoaxFractionString = decimalFormat.format(accuracy)

            hoaxFraction = hoaxFractionString.toFloat()
            notHoaxFraction = decimalFormat.format(1 - hoaxFraction).toFloat()

            hoaxPercentageString = decimalFormat.format(accuracy * 100) + "%"
            notHoaxPercentageString = decimalFormat.format((1 - accuracy) * 100) + "%"

            if (hoaxFraction > 0.5) {
                finalHoaxPercentage = hoaxPercentageString
                finalLabel = "Hoax"
            } else {
                finalHoaxPercentage = notHoaxPercentageString
                finalLabel = "Bukan Hoax"
            }


        }



        Log.d("HoaxResultFragment", "Hoax: $hoaxFraction")
        Log.d("HoaxResultFragment", "Not Hoax: $notHoaxFraction")

        val resultPercentage = view.findViewById<TextView>(R.id.textView22)
        resultPercentage.text =  "${finalHoaxPercentage} pencarian mengatakan itu"


        // Tampilkan hasil prediksi
        val resultTextView: TextView = view.findViewById(R.id.textView23)
        resultTextView.text = "$finalLabel"

        val pieChart: PieChart = view.findViewById(R.id.pieChart)

        pieChart.apply {
            slices = listOf(
                PieChart.Slice(
                    hoaxFraction.toFloat(),
                    Color.rgb(255, 34, 21),
//                    label = hoaxPercentageString,
                    labelColor = Color.rgb(0, 0, 0),

                    label = "",
                ),
                PieChart.Slice(
                    notHoaxFraction,
                    Color.rgb(2, 150, 168),
//                    label = notHoaxPercentageString,
                    labelColor = Color.rgb(0, 0, 0),

                    label = "",
                ),
            )
            startAngle = 100
            isCenterLabelEnabled = false
            gradientType = PieChart.GradientType.SWEEP
            holeRatio = 0.7f
            labelsFont = Typeface.createFromAsset(requireContext().assets, "fonts/gilroy_semibold.ttf")
            labelsOffset = 1f

        }
    }
}