package id.co.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private ListView Listmenu;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSurveyLocationData();

        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "provider enabled");
                mGoogleApiClient.connect();
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "connected");
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    private class menuAdapter extends ArrayAdapter<menuClass> {
        ArrayList<menuClass> menuArray;
        menuAdapter(Context context, ArrayList<menuClass> list) {
            super(context, 0, list);
            menuArray = list;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            menuClass menu = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_menu, parent, false);
            }
            // Lookup view for data population
            TextView nama = (TextView) convertView.findViewById(R.id.item_menu_nama);
            TextView subs = (TextView) convertView.findViewById(R.id.item_menu_subs);
            // Populate the data into the template view using the data object
            if (menu != null) {
                nama.setText(menu.nama);
                subs.setText(menu.subs);
                // Return the completed view to render on screen
            }
            return convertView;
        }

    }

    private void getSurveyLocationData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.address+"getlocation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "volley res: " + response);

                        List<Integer> id = new ArrayList<Integer>();
                        List<String> name = new ArrayList<String>();
                        List<Double> lat = new ArrayList<Double>();
                        List<Double> lng = new ArrayList<Double>();

                        try {
                            JSONArray responseArray = new JSONArray(response);
                            for(int i=0; i<responseArray.length(); i++){
                                int locationId = responseArray.getJSONObject(i).getInt("id");
                                String locationName = responseArray.getJSONObject(i).getString("location_name");
                                double locationLatitude = responseArray.getJSONObject(i).getDouble("latitude");
                                double locationLongitude = responseArray.getJSONObject(i).getDouble("longitude");

                                id.add(locationId);
                                name.add(locationName);
                                lat.add(locationLatitude);
                                lng.add(locationLongitude);
                            }
                            Log.d(TAG, name.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Listmenu = (ListView) findViewById(R.id.list_menu);
                        String[] sub = getResources().getStringArray(R.array.subs);

                        menuAdapter adapter = new menuAdapter(MainActivity.this, menuClass.addAll(id, name, sub, lat, lng));
                        Listmenu.setAdapter(adapter);

                        Listmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                menuClass menu = (menuClass) parent.getItemAtPosition(position);

                                double dist = getDistance(menu.lat, menu.lng, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                                Log.d(TAG, String.valueOf(dist)+"m");

                                if(dist<=50.0){
                                    Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                                    intent.putExtra("id", String.valueOf(menu.id));
                                    intent.putExtra("nama", menu.nama);
                                    intent.putExtra("lat", menu.lat);
                                    intent.putExtra("lng", menu.lng);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Anda terlalu jauh dari lokasi", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        }){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2){
        double r = 6371000;
        double currLat1 = Math.toRadians(lat1);
        double currLat2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLng = Math.toRadians(lng2-lng1);

        double a = (Math.sin(deltaLat/2) * Math.sin(deltaLat/2)) +
                (Math.cos(currLat1) * Math.cos(currLat2) * Math.sin(deltaLng) * Math.sin(deltaLng));
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = r * c;

        Log.d("dist", String.valueOf(d));

        return d;
    }
}
