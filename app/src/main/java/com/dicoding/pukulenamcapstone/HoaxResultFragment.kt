package com.dicoding.pukulenamcapstone

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahozad.android.PieChart
import ir.mahozad.android.component.Alignment

class HoaxResultFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hoax_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChart: PieChart = view.findViewById(R.id.pieChart)


        pieChart.apply {
            slices = listOf(
                PieChart.Slice(
                    0.3f,
                    Color.rgb(2, 150, 168),
                    label = "",

                ),
                PieChart.Slice(
                    0.1f,
                    Color.rgb(250, 204, 21),
                    label = "",

                ),
                PieChart.Slice(
                    0.6f,
                    Color.rgb(255, 34, 21),
                    label = "",

                ),
            )
            startAngle = 100
            isCenterLabelEnabled = false
            gradientType = PieChart.GradientType.SWEEP
            holeRatio = 0.7f
            isLegendsPercentageEnabled = false
        }
    }
}