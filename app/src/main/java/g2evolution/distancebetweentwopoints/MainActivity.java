package g2evolution.distancebetweentwopoints;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    LocationManager locationManager;
    String Result, Message, vlat, vlon;
    LatLng latLng;
    //GoogleMap map;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;


    String late, longe;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration, tvDistanceDuration2;

    String sharedlatitude, sharedlongitude;

    String strlatitude, strlongitude;
    String strlatitudedest, strlongitudedest;
    private Handler handler = new Handler();

    Context context;



    String strdriverid, strdrivername, strusertype;

    String regid, regname, regmobile, regemail;

    String Resultname, Resultmobile, Resultstreet, Resultlandmark, Resultcountry, Resultstate, Resultcity, Resultpincode;

    Handler mHandler;

    private Runnable runnable = new Runnable() {

        public void run() {


            Log.e("testing", "testvendorlocation====================");
            //openProfile();
            //VendorLocation();

            handler.postDelayed(this, 10000);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;
        //Shared preferences for registration and get data for description


        sharedlatitude = "15.527214";
        sharedlongitude = "77.634407";




        Log.e("testing", "register id home==" + regid);
        Log.e("testing", "register id regname==" + regname);
        Log.e("testing", "register id regmobile==" + regmobile);
        Log.e("testing", "register id regemail==" + regemail);

        Log.e("testing", "sharedlatitude==" + sharedlatitude);
        Log.e("testing", "sharedlongitude==" + sharedlongitude);
        // Activity_VendorLocation.this.mHandler = new Handler();
        //handler.postDelayed(runnable, 10000);
        //Activity_VendorLocation.this.mHandler.postDelayed(m_Runnable, 10000);
        // strlatitudedest = "13.118893";
        //strlongitudedest = "77.861652";




        tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
        tvDistanceDuration2 = (TextView) findViewById(R.id.tv_distance_time2);




        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // Getting Map for the SupportMapFragment
        // map = fm.getMap();


        // Enable MyLocation Button in the Map

        // map.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return;
        //map = fm.getMap();
        //map.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //To request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#reque stPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);



    }



    String origin = "12.9564192,77.6950672";
    String dest = "12.9564192,77.6950672";

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        Log.e("testing", "origin=" + origin);
        Log.e("testing", "dest=" + dest);


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

  /*  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
*/

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            tvDistanceDuration.setText("Distance : " + distance);
            tvDistanceDuration2.setText("Duration : " + duration);

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }



    @Override
    public void onLocationChanged(Location location) {

        //To clear map data
        mGoogleMap.clear();

        //To hold location
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("testing", "Latitude" + location.getLatitude());
        Log.e("testing", "Longitude" + location.getLongitude());


        Log.e("testing", "Map Location" + latLng);

        Double dlat = location.getLatitude();
        Double dlon = location.getLongitude();

        strlatitude = String.valueOf(dlat);
        strlongitude = String.valueOf(dlon);


        late = strlatitude;
        longe = strlongitude;
        vlat = "12.987510";
        vlon = "77.737672";
        openProfile();
        //VendorLocation();
      /*  double latitud = Double.parseDouble(late);
        double longitud = Double.parseDouble(longe);
        //  LatLng origin = markerPoints.get(0);
        //LatLng dest = markerPoints.get(1);
        //  LatLng origin = new LatLng(latitude ,longitude);
        LatLng origin = new LatLng(latitud ,longitud);


        String lates = vlat;
        String longes = vlon;
        double latitudes = Double.parseDouble(lates);
        double longitudes = Double.parseDouble(longes);
        LatLng dest = new LatLng(latitudes ,longitudes);
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();



        //To create marker in map
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Location");


        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locmap));
        //adding marker to the map
        map.addMarker(markerOptions);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(dest);
        markerOptions2.title("Vendor Location");


        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationvendor));
        //adding marker to the map
        map.addMarker(markerOptions2);
        //opening position with some zoom level in the map
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(dest, 17.0f));
        downloadTask.execute(url);*/

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /*//-------------------------------------------connecting database for validating id--------------------------------------------------
    private void VendorLocation() {

        Log.e("testing", " lat after = " + strlatitude + " lon after = " + strlongitude);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, End_Urls.STATUSVENDOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("testing", "json response111==" + response);

                        try {


                            JSONObject jsonArray1 = new JSONObject(response);
                            Result = jsonArray1.getString("status");
                            Message = jsonArray1.getString("response");
                            vlat = jsonArray1.getString("latitude");
                            vlon = jsonArray1.getString("longitude");

                            //Username = jsonArray1.getString("username");
                            //ID = jsonArray1.getString("id");
                            //Email = jsonArray1.getString("email");
                            //Mobile = jsonArray1.getString("mobile");

                            //Username = jsonArray1.getString("username");
                            Log.e("testing", "Result == " + Result);
                            Log.e("testing", "Message == " + Message);


                            //}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("testing", "json response222==" + response);

                        if (Result.equals("yes")) {

                            Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_LONG).show();
                            Log.e("testing", "Message111==" + Message);

                            openProfile();
                        } else {

                            Log.e("testing", "json response == " + response);
                            Toast.makeText(Activity_VendorLocation.this, Message, Toast.LENGTH_LONG).show();


                        }

                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("testing", "error response == " + error);
                        Toast.makeText(Activity_VendorLocation.this, "No network connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();


                map.put(End_Urls.STATUSVENDOR_MemberID, regid);


                Log.e("tesing", "map value" + map);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

    private void openProfile() {

        double latitud = Double.parseDouble(late);
        double longitud = Double.parseDouble(longe);
        LatLng origin = new LatLng(latitud, longitud);
       /* String lates = vlat;
        String longes = vlon;*/
        String lates = sharedlatitude;
        String longes = sharedlongitude;
        double latitudes = Double.parseDouble(lates);
        double longitudes = Double.parseDouble(longes);
        LatLng dest = new LatLng(latitudes, longitudes);
        String url = getDirectionsUrl(dest, origin);

        DownloadTask downloadTask = new DownloadTask();


        //To create marker in map
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Location");


        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locmap));
        //adding marker to the map
        mGoogleMap.addMarker(markerOptions);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(dest);
        markerOptions2.title("Driver Location");


        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.map));
        //adding marker to the map
        mGoogleMap.addMarker(markerOptions2);
        //opening position with some zoom level in the map
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dest, 17.0f));
        downloadTask.execute(url);


    }

  /*  @Override
    public void onBackPressed() {
        if (locationManager != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }locationManager.removeUpdates(this);

        handler.removeCallbacks(runnable);
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
*/
    //-------------------------------------------connecting database for validating id--------------------------------------------------


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}