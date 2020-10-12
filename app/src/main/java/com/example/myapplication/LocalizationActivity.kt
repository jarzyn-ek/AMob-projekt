package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.opengl.Visibility
import android.os.*
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.beust.klaxon.Klaxon
import com.facebook.CallbackManager
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.localization_view.*
import kotlinx.android.synthetic.main.localization_view.view.*
import kotlinx.android.synthetic.main.timer.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.*
import java.io.IOException
import java.util.*
import org.json.JSONObject
import kotlin.time.seconds


class LocalizationActivity : AppCompatActivity() {

    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010
    var callbackManager = CallbackManager.Factory.create()
    var requestCode = 0
    var resultCode = 0
    var data = null
    var timeLeft: Int = 0;
    lateinit var st: String

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.localization_view)
        setContentView(R.layout.timer)

        stopView.visibility = View.INVISIBLE

        this.st = getIntent().getStringExtra("skinType")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    fun getLastLocation() {

        if (CheckPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        val client = OkHttpClient()

                        val request: Request = Request.Builder()
                            .header("x-access-token", "386edd84117fe3faf68d72e2811b8032")
                            .url(
                                "https://api.openuv.io/api/v1/uv?lat=" + (location.latitude
                                    ?: "") + "&lng=" + (location.longitude
                                    ?: "") + "&dt=2020-08-27T10%3A50%3A52.283Z"
                            )
                            .build()

                        Thread(Runnable {
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    // Handle this
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    val responseBody = response.body()?.string();
                                    System.out.println(responseBody.toString())
                                    val result = Klaxon().parse<OpenUVResponse>(responseBody.toString())

                                    this@LocalizationActivity.runOnUiThread(java.lang.Runnable {
                                        timeLeft = result?.result?.safe_exposure_time?.st1?.times(60)?.times(1000) ?: 0;
                                        timeLeft = 100000;

                                        val timer = object: CountDownTimer(timeLeft.toLong(), 100) {
                                            override fun onTick(millisUntilFinished: Long) {
                                                val hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
                                                val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                                                val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                                                val padHours = hours.toString().padStart(2, '0')
                                                val padMinutes = minutes.toString().padStart(2, '0')
                                                val padSeconds = seconds.toString().padStart(2, '0')

                                                timerTextView.text = "$padHours:$padMinutes:$padSeconds"
                                            }
                                            override fun onFinish() {
                                                timerTextView.text = "STOP!"
                                                stopView.visibility = View.VISIBLE
                                            }
                                        }
                                        timer.start()
                                    })
                                }
                            })
                        }).start()
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        } else {
            RequestPermission()
        }
    }


    fun NewLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

        }
    }

    private fun CheckPermission(): Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false

    }

    fun RequestPermission() {
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled(): Boolean {
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the Permission")
            }
        }
    }


}