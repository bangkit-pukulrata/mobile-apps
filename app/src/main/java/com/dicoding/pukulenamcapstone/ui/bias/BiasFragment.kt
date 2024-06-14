package com.dicoding.pukulenamcapstone.ui.bias

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dicoding.pukulenamcapstone.R
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.flex.FlexDelegate
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BiasFragment : Fragment() {
    private lateinit var interpreter: Interpreter
    private lateinit var tokenizer: Tokenizer
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sumber : String = ""
        val btnCheck : Button = view.findViewById(R.id.button_bias)
        val inputTitle : EditText = view.findViewById(R.id.bias_title)
        val inputContent : EditText = view.findViewById(R.id.input_bias)

        spinner = view.findViewById(R.id.spinner)

        // Menggunakan array dari strings.xml
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Languages,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sumber = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada aksi yang perlu dilakukan
            }
        }


        btnCheck.setOnClickListener() {
            if (inputTitle.text.toString() == "") {
                Toast.makeText(requireContext(), "Judul Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (inputContent.text.toString() == "") {
                Toast.makeText(requireContext(), "Konten Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inputText = sumber.lowercase()  + " " + inputContent.text.toString()

            val inputTextList = listOf(inputText)


            Log.d("HomeFragment", "Button Clicked")
            tokenizer = readTokenizerFromAssets("tokenizer_bias.json")

            val tfliteModel = loadModelFileFromAssets("bias_detection_lstm.tflite")
            val options = Interpreter.Options()
            options.addDelegate(FlexDelegate())
            interpreter = Interpreter(tfliteModel, options)


//            val newTexts = listOf("sejarah world water forum indonesia jadi tuan rumah tahun world water forum wwf selenggara indonesia tahun indonesia jadi tuan rumah akan selenggara rangkai acara forum sektor air besar dunia tanggal mei datang world water forum forum internasional sektor air libat bagai mang penting dunia ada world water council wwc dewan air dunia organisasi internasional dedikasi atas isu isu air global world water forum selenggara tiga tahun hingga kini selenggara langsung banyak kali sejak tahun tahun pertama kali forum internasional sektor air sebut selenggara advertisement scroll to continue with content saat marrakesh maroko jadi tuan rumah selenggara world water forum pertama tahun bal indonesia pilih jadi tuan rumah selenggara world water forum puluh kutip situs resmi wwf ikut sejarah daftar tuan rumah selenggara world water forum masa masa sejak tahun wwf marrakech maroko maret forum hadir serta tuju atas krisis air global forum hasil deklarasi marrakesh beri mandat dewan air dunia wwc kembang visi air dunia abad deklarasi tanda langkah penting selesai krisis air visi kemudian terbit tahun presentasi wwf wwf den haag belanda maret forum hasil diskusi signifikan visi air dunia rencana aksi kait salah satu hasil usul bentuk tim pantau akan laku survei upaya upaya komunitas air global lapor rencana aksi konkret wwf wwf kyoto jepang maret forum selenggara kyoto shiga osaka jepang tahun lebih serta forum temu delegasi resmi negara organisasi internasional forum hasil publikasi lapor aksi air dunia analis aksi lapang tampil upaya global atas tantang air wwf kota meksiko meksiko maret forum fokus isu isu kelola tata kelola air penting masuk tingkat akses air sanitasi tingkat mekanisme biaya bina kerja sama internasional hasil main peran penting dorong dialog tindak bidang bidang atas tantang air global efektif wwf istanbul turki maret forum tanda beberapa baru pertama kali jadi temu hearings of state libat lebih anggota parlemen kembang nyata menteri pandu air lebih serta ikut forum lebih organisasi kontribusi forum selenggara tema tampil lapor regional cakup panel tingkat tinggi wwf marseille prancis maret forum usung tema time for solutions saat cari solusi forum tampil desa solusi platform solusi sorot dekat dekat ada masalah air sanitasi prancis tuan rumah hasil dapat lebih forum sebut kemudian investasi dukung proyek proyek lapang wwf daegu gyeongbuk korea selatan april forum tanda fase baru dalam upaya global bangun lanjut forum tuju implementasi solusi forum belum fasilitas diskusi isu isu kait air dorong kerja sama antara perintah organisasi usaha bagai janji ditandatangani temu bilateral multilateral cipta peluang kerja sama wwf brasilia brasil maret forum tanda tonggak sejarah signifikan pertama kali acara ada bahan bumi selatan tema sharing water bagi air forum tuju atas tantang air global dorong solusi inovatif wwf dakar senegal maret forum temu pimpin dunia praktisi masyarakat sipil bahas tantang air sanitasi tema water security for peace and development aman air damai bangun forum tuju promosi aksi kolektif kerja sama solusi padu atas masalah air desak acara aku tingkat dampak variabilitas ubah iklim wwf bal indonesia mei forum angkat tema water for shared prosperity air untuk makmur sama forum akan langsung tanggal mei selenggara yang diri tiga tahap proses thematic process regional process political process")

            // Tokenisasi dan padding teks baru
            val newSequences = tokenizer.textsToSequences(inputTextList)
            val maxLen = 30
            val newPadded = padSequences(newSequences, maxLen)

//            Log.d("HoaxDetection", "maxWordCount: $maxWordCount")
//            Log.d("HoaxDetection", "maxWord: $maxWord")
//
            Log.d("BiasDetection", "newSequences: ${newSequences.contentDeepToString()}")
            Log.d("BiasDetection", "newPadded: ${newPadded.contentDeepToString()}")

            // Konversi data input menjadi tipe float32
            val floatArray = convertToFloat32Buffer(newPadded)
            val inputBuffer = ByteBuffer.allocateDirect(floatArray.size * 4).order(ByteOrder.nativeOrder())
            inputBuffer.asFloatBuffer().put(floatArray)

            // Mendapatkan output buffer untuk hasil prediksi
            val outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())

            // Mengatur tensor input dengan data yang dipadatkan
            interpreter.run(inputBuffer, outputBuffer)

            // Mendapatkan hasil prediksi dari tensor output
            outputBuffer.rewind()
            val predictions = FloatArray(1)
            outputBuffer.asFloatBuffer().get(predictions)
            val predictedLabels = predictions.map { if (it > 0.5) 1 else 0 }

            // Menampilkan hasil prediksi
            for ((index, text) in inputTextList.withIndex()) {
                Log.d("BiasDetection", "Text: $text")
                Log.d("BiasDetection", "Prediction: ${predictions[index]}")
                Log.d("BiasDetection", "Predicted Label: ${if (predictedLabels[index] == 1) "Hoax" else "Not Hoax"}")

                val predictedLabel = if (predictedLabels[0] == 1) "Bias" else "Tidak Bias"

                // Membuat Bundle untuk mengirim data ke fragment lain
                val bundle = Bundle()
                bundle.putString("predictedLabel", predictedLabel)
                bundle.putFloat("accuracy", predictions[index])

                // Navigasi ke fragment hasil dengan argumen
                Navigation.findNavController(it).navigate(R.id.action_biasFragment_to_biasResultFragment, bundle)
            }
        }
    }

    private fun readTokenizerFromAssets(filePath: String): Tokenizer {
        val assetManager = requireContext().assets
        val inputStream = assetManager.open(filePath)
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        return Tokenizer(jsonObject.getJSONObject("config"))
    }

    // Fungsi untuk membaca model file dari assets
    private fun loadModelFileFromAssets(path: String): ByteBuffer {
        val assetManager = requireContext().assets
        val inputStream = assetManager.open(path)
        val byteArray = inputStream.readBytes()
        val byteBuffer = ByteBuffer.allocateDirect(byteArray.size)
        byteBuffer.order(ByteOrder.nativeOrder())
        byteBuffer.put(byteArray)
        byteBuffer.flip()
        return byteBuffer
    }

    // Fungsi untuk konversi data input menjadi tipe float32
    private fun convertToFloat32Buffer(input: Array<IntArray>): FloatArray {
        return input.flatMap { it.toList() }.map { it.toFloat() }.toFloatArray()
    }

    // Fungsi untuk padding sequences
    private fun padSequences(sequences: Array<IntArray>, maxLen: Int): Array<IntArray> {
        return sequences.map { seq ->
            if (seq.size >= maxLen) {
                seq.takeLast(maxLen).toIntArray()
            } else {
                val padding = IntArray(maxLen - seq.size)
                padding + seq
            }
        }.toTypedArray()
    }

    class Tokenizer(jsonObject: JSONObject) {
        private val wordIndex: Map<String, Int>

        init {
            // Uraikan string JSON menjadi objek JSON
            val wordIndexString = jsonObject.getString("word_index")
            val wordIndexObject = JSONObject(wordIndexString)
            wordIndex = jsonToMap(wordIndexObject)
        }

        fun textsToSequences(texts: List<String>): Array<IntArray> {
            val sequences = texts.map { text ->
                val sequence = mutableListOf<Int>()
                val logBuilder = StringBuilder()

                text.split(" ").forEach { word ->
                    // Remove commas, periods, parentheses, and convert to lowercase
                    var cleanedWord = word.replace("[,().]".toRegex(), "").lowercase()

                    // Split by hyphens, slashes, and transitions between letters and digits
                    val subWords = cleanedWord.split("[-/]|(?<=[a-zA-Z])(?=[0-9])|(?<=[0-9])(?=[a-zA-Z])|(?<=[a-zA-Z])(?=[^a-zA-Z])|(?<=[^a-zA-Z])(?=[a-zA-Z])".toRegex()).toMutableList()

                    // Check for 'com' at the end of the word and split it
                    if (subWords.isNotEmpty() && subWords.last().endsWith("com") && subWords.last() != "com") {
                        val lastWord = subWords.removeAt(subWords.size - 1)
                        val beforeCom = lastWord.substring(0, lastWord.length - 3)
                        subWords.add(beforeCom)
                        subWords.add("com")
                    }

                    subWords.forEach { subWord ->
                        if (subWord.isNotBlank() && subWord.any { it.isLetterOrDigit() }) {
                            val index = wordIndex[subWord] ?: 1
                            sequence.add(index)
                            logBuilder.append("Word: $subWord, Sequence: $index\n")
                        }
                    }
                }

                // Log the accumulated information for the current text
                Log.d("BiasDetection", logBuilder.toString())

                sequence.toIntArray()
            }.toTypedArray()

            return sequences
        }



        // Fungsi untuk mengonversi JSONObject ke Map
        private fun jsonToMap(jsonObject: JSONObject): Map<String, Int> {
            val map = mutableMapOf<String, Int>()
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = jsonObject.getInt(key)
            }
            return map
        }
    }



}