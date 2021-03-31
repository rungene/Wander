package com.rungene.android.wander

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    //Create a TAG class variable above the onCreate() method. This will be used for logging purposes.
    private val TAG = MapsActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        //These coordinates represent the latitude and longitude of the Googleplex.
        val latitude = 37.422160
        val longitude = -122.084270


    /*    The zoom level controls how zoomed in you are on the map. The following list gives you an
        idea of what level of detail each level of zoom shows:

        1: World
        5: Landmass/continent
        10: City
        15: Streets
        20: Buildings*/
        val zoomLevel = 15f

        //Create a new LatLng object called home.
        val homeLatLng = LatLng(latitude, longitude)

        /*  Move the camera to home by calling the moveCamera() function on the GoogleMap object and
     pass in a CameraUpdate object using CameraUpdateFactory.newLatLngZoom().*/
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))

        //Add a marker to the map at your home.
        map.addMarker(MarkerOptions().position(homeLatLng))

        //call setMapLongClick(). Pass in map.
        setMapLongClick(map)

        //Call setPoiClick() at the end of onMapReady(). Pass in map
        setPoiClick(map)

        //Call the setMapStyle(). passing in your GoogleMap object.
        setMapStyle(map)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId){
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

   /* Create a method stub in MapsActivity called setMapLongClick() that takes a GoogleMap as
    an argument. Attach a long click listener to the map object.*/

    private fun setMapLongClick(map:GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // title of the marker to “Dropped Pin” and set the marker’s snippet to the snippet
            // you just created.
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                    Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    latLng.latitude,
                    latLng.longitude
            )

            map.addMarker(
                    MarkerOptions()
                            .position(latLng)
                            .title(getString(R.string.dropped_pin))
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    //By default, points of interest (POIs) appear on the map along with their corresponding icons.
    //When the map type is set to normal, business POIs also appear on the map. Business POIs
    // represent businesses such as shops, restaurants, and hotels.
    //add an OnPoiClickListener to the map. This click-listener places a marker on the map
    //immediately when the user clicks on a POI.
    private fun setPoiClick(map: GoogleMap) {
        //setOnPoiClickListener function, call showInfoWindow() on poiMarker to immediately
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                    MarkerOptions()
                            .position(poi.latLng)
                            .title(poi.name)
            )

        // show the info window.
            poiMarker.showInfoWindow()

        }
    }

    /*To set the JSON style to the map, call setMapStyle() on the GoogleMap object. Pass in a
    MapStyleOptions object, which loads the JSON file. The setMapStyle() method returns a boolean
    indicating the success of the styling.*/
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.map_style
                    )
            )

            if (!success){
                Log.e(TAG,"Style parsing failed")
            }
        }catch (e: Resources.NotFoundException){
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
}