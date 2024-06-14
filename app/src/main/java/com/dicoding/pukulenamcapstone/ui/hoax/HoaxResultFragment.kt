package com.dicoding.pukulenamcapstone.ui.hoax

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.mahozad.android.PieChart
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import android.graphics.Typeface
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.adapter.RelatedNewsAdapter
import com.dicoding.pukulenamcapstone.data.RelatedNews
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class HoaxResultFragment : Fragment() {
    private lateinit var database: DatabaseReference

    private lateinit var relatedNewsRecyclerView: RecyclerView
    private lateinit var relatedNewsAdapter: RelatedNewsAdapter
    private val relatedNewsList = mutableListOf<RelatedNews>()

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

        val list_words: List<String> = listOf(
            "video", "jokowi", "prabowo", "indonesia", "polisi", "com", "kpk", "anies", "foto", "warga", "kompas", "rp",
            "presiden", "klaim", "narasi", "jakarta", "bogor", "gibran", "orang", "fakta", "cek", "korban", "covid", "akun",
            "anak", "tewas", "facebook", "dki", "vaksin", "informasi", "hoaks", "rumah", "tersangka", "pria", "terkait",
            "ditangkap", "mei", "ketua", "pdi", "negara", "tim", "berdasarkan", "wanita", "baswedan", "pilkada", "mobil",
            "unggahan", "beredar", "dpr", "juta", "polri", "sambo", "ganjar", "media", "gambar", "uang", "kpu", "meninggal",
            "syl", "partai", "tempo", "menteri", "tni", "ferdy", "anggota", "gempa", "bus", "dunia", "kecelakaan", "korupsi",
            "narkoba", "rusia", "resmi", "sidang", "artikel", "mk", "salah", "depok", "sosial", "kota", "hasil", "diduga",
            "israel", "haji", "koper", "ditemukan", "suara", "kementan", "ri", "detik", "stip", "viral", "kesehatan",
            "megawati", "pemerintah", "pelaku", "mahfud", "jalan", "pilpres", "banjir"
        )


        // Ambil argumen yang dikirimkan dari fragment sebelumnya
        val predictedLabel = arguments?.getString("predictedLabel")
        val accuracy = arguments?.getFloat("accuracy")
        val inputTitle = arguments?.getString("inputTitle") ?: ""

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
            holeRatio = 0.2f
            labelsFont = Typeface.createFromAsset(requireContext().assets, "fonts/gilroy_semibold.ttf")
            labelsOffset = 1f


            database = FirebaseDatabase.getInstance().reference

            relatedNewsRecyclerView = view.findViewById(R.id.rv_related)
            relatedNewsAdapter = RelatedNewsAdapter(relatedNewsList)
            relatedNewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            relatedNewsRecyclerView.adapter = relatedNewsAdapter


            Log.d("FirebaseData", "getting data")
            getFirstFiveData(inputTitle, list_words)
        }
    }
    private fun getFirstFiveData(inputTitle: String, listWords: List<String>) {
        val firestore = FirebaseFirestore.getInstance()
        val productsRef = firestore.collection("dataset-final")

        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar2)
        progressBar?.visibility = View.VISIBLE

        val queryWords = listWords.filter { word -> inputTitle.contains(word, ignoreCase = true) }

        // Ambil semua data yang mungkin mengandung query dalam berbagai kasus
        productsRef.get()
            .addOnSuccessListener { documents ->
                val filteredDocuments = documents.filter { document ->
                    val title = document.getString("title")?.lowercase() ?: ""
                    queryWords.any { word -> title.contains(word.lowercase()) }
                }.take(5)

                if (filteredDocuments.isEmpty()) {
                    // Jika tidak ada data yang ditemukan, lakukan pencarian ulang dengan query "demo"
                    productsRef.whereEqualTo("title", "demo").get()
                        .addOnSuccessListener { demoDocuments ->
                            relatedNewsList.clear()
                            for (document in demoDocuments) {
                                val isFake = document.getLong("is_fake")
                                val title = document.getString("title") ?: ""
                                relatedNewsList.add(RelatedNews(title, isFake))
                            }
                            relatedNewsAdapter.notifyDataSetChanged()
                            progressBar?.visibility = View.GONE
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FirebaseData", "Error getting demo documents: ", exception)
                            Toast.makeText(requireContext(), "Error getting demo data", Toast.LENGTH_SHORT).show()
                            progressBar?.visibility = View.GONE
                        }
                } else {
                    relatedNewsList.clear()
                    for (document in filteredDocuments) {
                        val isFake = document.getLong("is_fake")
                        val title = document.getString("title") ?: ""
                        relatedNewsList.add(RelatedNews(title, isFake))
                    }
                    relatedNewsAdapter.notifyDataSetChanged()
                    progressBar?.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseData", "Error getting documents: ", exception)
                Toast.makeText(requireContext(), "Error getting data", Toast.LENGTH_SHORT).show()
                progressBar?.visibility = View.GONE
            }
    }
}