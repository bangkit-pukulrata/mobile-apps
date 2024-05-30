package com.dicoding.pukulenamcapstone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.Interpreter.Options
import org.tensorflow.lite.flex.FlexDelegate
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<TextView>(R.id.textView16)
        button.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_newsDetailFragment)
        }

        val button2 = view.findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_allNewsFragment)
        }
    }
}
