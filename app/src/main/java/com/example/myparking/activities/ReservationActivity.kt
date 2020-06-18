package com.example.myparking.activities

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer

import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.braintreepayments.api.dropin.DropInRequest
import com.example.myparking.R
import com.example.myparking.adapters.DurationAdapter
import com.example.myparking.adapters.MyAdapter
import com.example.myparking.databinding.ActivityReservationBinding
import com.example.myparking.models.*
import com.example.myparking.repositories.ReservationListRepository
import com.example.myparking.models.ReservationRequest

import com.example.myparking.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_reservation.*

import java.util.*

import kotlinx.android.synthetic.main.reservation_dialog.view.*
import java.text.SimpleDateFormat
import com.braintreepayments.api.dropin.DropInActivity
import android.R.attr.data
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.braintreepayments.api.dropin.DropInResult
import com.example.myparking.utils.InjectorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservationActivity : Fragment() {

    private val BRAINTREE_REQUEST_CODE = 11
    private lateinit var binding: ActivityReservationBinding
    private lateinit var currentParking: Parking
    private lateinit var dateEntree: String
    private lateinit var dateSortie: String
    private lateinit var mDialogSuccuess : View
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parking = arguments?.getParcelable<Parking>("parking") as Parking
        currentParking = parking

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_reservation, container, false)
        binding.lifecycleOwner = activity
        binding.parking = currentParking
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idDriver = PreferenceManager(context!!).checkDriverProfile()
        //initTarifsLists()
        initDurationsList()
        confirm_btn.setOnClickListener {
            // create Reservation

            Log.d("dateentree", dateEntree)
            Log.d("datesortie", dateSortie)


            mDialogSuccuess = LayoutInflater.from(context!!).inflate(R.layout.reservation_dialog, null)
            onBraintreeSubmit(view)

            /* here create res*/

            /*  val mBuilder = AlertDialog.Builder(this)
                  .setView(mDialogSuccuess)

              val mAlertDialog = mBuilder.show()
              mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent) */
            /*  mDialogView.see_details_btn.setOnClickListener {

                  // real data of reseravtion later
                  // val reservation = Reservation("DZ - 12458647", "Lun, déc 16 12.40", " Lun, déc 16 13.40", currentParking)
                  val reservations = ReservationListRepository.getInstance().getReservations()
                  val reservation =
                      reservations.find { res -> res.parking.idParking == currentParking.idParking }
                  val reservationDetailsActivity =
                      ReservationDetailsActivity.newIntent(this, reservation!!)
                  startActivity(reservationDetailsActivity)
                  finish()

              }*/

        }
    }

    fun onBraintreeSubmit(v:View) {
        val dropInRequest = DropInRequest()
            .clientToken(PreferenceManager(context!!).getBrainTreeToken())
        startActivityForResult(dropInRequest.getIntent(context!!), BRAINTREE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result = data?.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT) as Any
                // use the result to update your UI and send the payment method nonce to your server
                Log.d("nonce", result.toString())
                InjectorUtils.provideReservationService().checkout(PaimentMethod(result.toString())).enqueue(object:
                    Callback<Any> {
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.code()==200) {
                            val idDriver = PreferenceManager(context!!).checkDriverProfile()
                            val date = Calendar.getInstance().time
                            val reservationRequest = ReservationRequest(
                                dateEntree,
                                dateSortie,
                                currentParking.idParking.toString(),
                                idDriver,
                                "1",
                                PaiementInstance("89", "5846", df.format(date))
                            )
                            showLoading()
                            ReservationListRepository.getInstance().getCreatedReservation(reservationRequest)
                                .observe(this@ReservationActivity, Observer<Reservation> {res ->
                                    if (res!=null) {
                                        hideLoading()
                                        val mBuilder = AlertDialog.Builder(context!!)
                                            .setView(mDialogSuccuess)
                                        val mAlertDialog = mBuilder.show()
                                        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                        mDialogSuccuess.see_details_btn.setOnClickListener {

                                            goToReservationDetails(res)

                                        }
                                    }

                                })
                        }
                    }

                })
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                val error = data?.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
            }
        }
    }

    fun goToReservationDetails(reservation: Reservation) {
        val navController = Navigation.findNavController(binding.root)
        val bundle = bundleOf("reservation" to reservation)
        navController.navigate(R.id.action_reservationActivity_to_reservationDetailsActivity,bundle)
    }



    fun initDurationsList() {

        val c = Calendar.getInstance()
        var hourIN = c.get(Calendar.HOUR_OF_DAY)
        var minuteIN = c.get(Calendar.MINUTE)
        dateEntree = df.format(c.time)
        c.add(Calendar.HOUR_OF_DAY, 2)
        var hourOUT = c.get(Calendar.HOUR_OF_DAY)
        var minuteOUT = c.get(Calendar.MINUTE)
        dateSortie = df.format(c.time)
        c.add(Calendar.HOUR_OF_DAY, -2)

        val IN = Duration(
            "Entrée",
            String.format("%02d:%02d", hourIN, minuteIN),
            R.drawable.ic_hourglass_full,
            "IN",
            true
        )
        val OUT = Duration(
            "Sortie",
            String.format("%02d:%02d", hourOUT, minuteOUT),
            R.drawable.ic_hourglass_empty,
            "OUT",
            true
        )
        val TOTAL = Duration("Temps total", "2 heures", R.drawable.ic_timer, "TIME", false)

        val recyclerView = duration_container
        recyclerView.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        val list = arrayListOf(
            IN,
            OUT,
            TOTAL
        )



        var adapter = DurationAdapter(list, object : MyAdapter.ItemAdapterListener<Duration> {
            override fun onItemClicked(item: Duration) {
            }

        })
        val listener = object : MyAdapter.ItemAdapterListener<Duration> {

            override fun onItemClicked(item: Duration) {
                val c = Calendar.getInstance().time

                when (item.TAG) {
                    "IN" -> {
                        TimePickerDialog(
                            context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                hourIN = hourOfDay
                                minuteIN = minute
                                IN.text2 = String.format("%02d:%02d", hourOfDay, minute)
                                updateTotal()
                                adapter.notifyDataSetChanged()
                                c.hours = hourOfDay
                                c.minutes = minute
                                dateEntree = df.format(c)
                            }, hourIN, minuteIN, true
                        ).show()
                    }
                    "OUT" -> {
                        TimePickerDialog(
                            context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                hourOUT = hourOfDay
                                minuteOUT = minute
                                OUT.text2 = String.format("%02d:%02d", hourOfDay, minute)
                                updateTotal()
                                adapter.notifyDataSetChanged()
                                c.hours = hourOfDay
                                c.minutes = minute
                                dateSortie = df.format(c)
                            }, hourOUT, minuteOUT, true
                        ).show()
                    }
                }
            }

            fun updateTotal() {
                val c1 = Calendar.getInstance()
                val c2 = Calendar.getInstance()
                c1.set(Calendar.HOUR_OF_DAY, hourIN)
                c1.set(Calendar.MINUTE, minuteIN)
                c2.set(Calendar.HOUR_OF_DAY, hourOUT)
                c2.set(Calendar.MINUTE, minuteOUT)
                val diff = c2.time.time - c1.time.time
                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                TOTAL.text2 = String.format("%02d heures", hours)
            }
        }
        adapter = DurationAdapter(list, listener)
        recyclerView.adapter = adapter
    }

    fun showLoading() {
        reserv_loading.visibility=VISIBLE
    }
    fun hideLoading() {
        reserv_loading.visibility= INVISIBLE
    }


}
