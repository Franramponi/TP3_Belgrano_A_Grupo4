package com.edu.ort.tp3_belgrano_a_grupo4.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edu.ort.tp3_belgrano_a_grupo4.R
import com.edu.ort.tp3_belgrano_a_grupo4.adapters.OfferAdapter
import com.edu.ort.tp3_belgrano_a_grupo4.adapters.TrendingAdapter
import com.edu.ort.tp3_belgrano_a_grupo4.adapters.WeeklyFlightAdapter
import com.edu.ort.tp3_belgrano_a_grupo4.adapters.com.edu.ort.tp3_belgrano_a_grupo4.adapters.CustomSpinnerAdapter
import com.edu.ort.tp3_belgrano_a_grupo4.entities.Offer
import com.edu.ort.tp3_belgrano_a_grupo4.entities.TrendingDestination
import com.edu.ort.tp3_belgrano_a_grupo4.entities.WeeklyFlight
import com.google.android.material.textfield.TextInputLayout


class Search : Fragment() {
    private lateinit var viewSearch: View
    private lateinit var btnOneWay: Button
    private lateinit var btnRoundTrip: Button
    private lateinit var editTextDeparture: EditText
    private lateinit var editTextDepartureDate: EditText
    private lateinit var editTextArrival: EditText
    private lateinit var editTextArrivalDate: EditText
    private lateinit var textInputLayoutArrivalDate: TextInputLayout
    private lateinit var editTextClass: EditText
    private lateinit var editTextPassanger: EditText
    private lateinit var btnSearch: Button
    private lateinit var recOffer: RecyclerView

    private var offers: MutableList<Offer> = ArrayList()
    companion object {
        val BTN_ONE_DAY_ID = R.id.btnOneWay
        val BTN_ROUND_TRIP_ID = R.id.btnRoundTrip
        val INPUT_DEPARTURE_ID = R.id.et_departure
        val INPUT_DEPARTURE_DATE_ID = R.id.et_departure_date
        val INPUT_ARRIVAL_ID = R.id.et_arrival
        val INPUT_ARRIVAL_DATE_ID = R.id.et_arrival_date
        val TEXT_INPUT_LAYOUT_ARRIVAL_DATE_ID = R.id.textInputLayoutArrivalDate
        val TEXT_INPUT_CLASS_ID = R.id.et_class
        val TEXT_INPUT_PASSANGER_ID = R.id.et_passengers
        val BTN_SEARCH_ID = R.id.btnSearch
        val REC_OFFER_ID = R.id.rec_offers

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewSearch =  inflater.inflate(R.layout.fragment_search, container, false)


        initViews()
        initListeners()
        btnOneWay.isSelected = true
        updateButtonAppearance()
        loadList()

        return viewSearch
    }

    override fun onStart() {
        super.onStart()

        recOffer.setHasFixedSize(true)
        val layoutManagerOffer = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val offerAdapter = OfferAdapter(offers){ offer ->
            val action = SearchDirections.actionFragmentSearchToOffers()
            findNavController().navigate(action)
        }
        recOffer.layoutManager = layoutManagerOffer
        recOffer.adapter = offerAdapter

    }
    private fun initViews() {
        btnOneWay = viewSearch.findViewById(BTN_ONE_DAY_ID)
        btnRoundTrip = viewSearch.findViewById(BTN_ROUND_TRIP_ID)
        editTextDepartureDate = viewSearch.findViewById(INPUT_DEPARTURE_DATE_ID)
        editTextArrivalDate = viewSearch.findViewById(INPUT_ARRIVAL_DATE_ID)
        textInputLayoutArrivalDate = viewSearch.findViewById(TEXT_INPUT_LAYOUT_ARRIVAL_DATE_ID) // Utilizo este ID para ocultar el campo
        btnSearch = viewSearch.findViewById(BTN_SEARCH_ID)
        editTextDeparture = viewSearch.findViewById(INPUT_DEPARTURE_ID)
        editTextArrival = viewSearch.findViewById(INPUT_ARRIVAL_ID)
        recOffer = viewSearch.findViewById(REC_OFFER_ID)
        editTextPassanger =  viewSearch.findViewById(TEXT_INPUT_PASSANGER_ID)
        editTextClass =  viewSearch.findViewById(TEXT_INPUT_CLASS_ID)

        textInputLayoutArrivalDate.visibility = View.GONE
    }

    private fun initListeners() {
        btnOneWay.setOnClickListener {
            btnOneWay.isSelected = true
            btnRoundTrip.isSelected = false
            updateButtonAppearance()
            textInputLayoutArrivalDate.visibility = View.GONE
        }
        btnRoundTrip.setOnClickListener {
            btnOneWay.isSelected = false
            btnRoundTrip.isSelected = true
            updateButtonAppearance()
            textInputLayoutArrivalDate.visibility = View.VISIBLE
        }
        editTextDepartureDate.setOnClickListener { showDatePicker(editTextDepartureDate) }
        editTextArrivalDate.setOnClickListener { showDatePicker(editTextArrivalDate) }
        btnSearch.setOnClickListener { navigateToResults() }


        setupEditTextBehavior(editTextDeparture, getString(R.string.txt_departure))
        setupEditTextBehavior(editTextArrival, getString(R.string.txt_arrival))
        setupEditTextBehavior(editTextDepartureDate, getString(R.string.txt_date))
        setupEditTextBehavior(editTextArrivalDate, getString(R.string.txt_date))
        setupEditTextBehavior(editTextPassanger, getString(R.string.txt_passanger))
        setupEditTextBehavior(editTextClass, getString(R.string.txt_class))

    }
    private fun updateButtonAppearance() {
        // Método para actualizar la apariencia de los botones según su estado de selección
        if (btnOneWay.isSelected) {
            btnOneWay.setBackgroundResource(R.drawable.selected_button_background)
            btnOneWay.setTextColor(resources.getColor(android.R.color.white))
            btnRoundTrip.setBackgroundResource(R.drawable.unselected_button_background)
            btnRoundTrip.setTextColor(resources.getColor(android.R.color.darker_gray))
        } else {
            btnRoundTrip.setBackgroundResource(R.drawable.selected_button_background)
            btnRoundTrip.setTextColor(resources.getColor(android.R.color.white))
            btnOneWay.setBackgroundResource(R.drawable.unselected_button_background)
            btnOneWay.setTextColor(resources.getColor(android.R.color.darker_gray))
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            editText.setText(date)
        }, year, month, day)

        datePickerDialog.show()
    }




    private fun navigateToResults() {
        val action = SearchDirections.actionSearchToSearchResult()
        findNavController().navigate(action)
    }


    private fun setupEditTextBehavior(editText: EditText, defaultText: String) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (editText.text.toString() == defaultText) {
                    editText.setText("")
                }
            } else {
                if (editText.text.toString().isEmpty()) {
                    editText.setText(defaultText)
                }
            }
        }
    }

    private fun loadList() {
        offers.add(Offer("Mastercard", R.drawable.card_mastercard, 20))
        offers.add(Offer("Visa", R.drawable.card_visa, 25))
    }

}