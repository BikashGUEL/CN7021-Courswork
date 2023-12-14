package com.cityzen.cityzen.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cityzen.cityzen.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MapViewActivity extends AppCompatActivity implements LocationListener {

    private MapView mapView;
    private Button searchButton;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map_view);

        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this));

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        searchButton = findViewById(R.id.goButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findJourney();
            }
        });

        // Request location permissions
        requestLocationPermissions();

        // Set default map center and zoom level
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);

        // Try to get the last known location
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation); // Call onLocationChanged to handle zoom and marker
        }

        // Start location updates
       // startLocationUpdates();
    }

    private void findJourney() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.tfl.gov.uk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        String fromLocation = editTextFrom.getText().toString();
        String toLocation = editTextTo.getText().toString();

        Call<YourResponseType> call = apiService.getJourneyResults(fromLocation, toLocation, "79a3cc0f25384635b4d84ced636f5195");

        call.enqueue(new Callback<YourResponseType>() {
            @Override
            public void onResponse(Call<YourResponseType> call, Response<YourResponseType> response) {
                if (response.isSuccessful()) {
                    YourResponseType data = response.body();
                    textViewResult.setText(data.toString());
                } else {
                    textViewResult.setText("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<YourResponseType> call, Throwable t) {
                textViewResult.setText("Failed to make API call: " + t.getMessage());
            }
        });
    }
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        return null;
    }

    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update the map with the new location
        GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

        // Zoom in further (adjust the zoom level as needed)
        mapView.getController().setZoom(18.0);

        // Center the map on the current location
        mapView.getController().setCenter(currentLocation);

        // Add a marker to the current location with a custom icon
        Drawable customMarkerIcon = ContextCompat.getDrawable(this, R.drawable.marker);
        Marker currentLocationMarker = new Marker(mapView);
        currentLocationMarker.setIcon(customMarkerIcon);
        currentLocationMarker.setPosition(currentLocation);
        currentLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(currentLocationMarker);

        // Draw a polyline using all the provided coordinates
        List<GeoPoint> coordinates = new ArrayList<>();

        double[][] coordinatesArray = {
//                {51.51425046095, 0.05937310212},
//                {51.51438976619, 0.06027197333},
//                {51.51433509345}
//                {51.50989546163, 0.02522218216}
                {51.51425046095, 0.05937310212},
                {51.51438976619, 0.06027197333},
                {51.51433509345, 0.06031274454},
                {51.51435901421, 0.06048677730},
                {51.51436420434, 0.06070320321},
                {51.51558257697, 0.06045551923},
                {51.51562206158, 0.06025551676},
                {51.51574887227, 0.06020358646},
                {51.51576468798, 0.05981514322},
                {51.51581010496, 0.05876502633},
                {51.51582768524, 0.05827576905},
                {51.51591206240, 0.05705444585},
                {51.51598390671, 0.05603433992},
                {51.51603872827, 0.05505686223},
                {51.51603872827, 0.05505686223},
                {51.51610162074, 0.05393529702},
                {51.51616724388, 0.05245393910},
                {51.51616724388, 0.05245393910},
                {51.51619082849, 0.05192144278},
                {51.51613842629, 0.05183260765},
                {51.51619410402, 0.05173421660},
                {51.51628370074, 0.05021042642},
                {51.51623154898, 0.05010719099},
                {51.51627798786, 0.05002279617},
                {51.51630678250, 0.04940431423},
                {51.51632432382, 0.04891504692},
                {51.51637435376, 0.04811014244},
                {51.51632194863, 0.04802131124},
                {51.51636838603, 0.04793691408},
                {51.51640497291, 0.04711730307},
                {51.51640497291, 0.04711730307},
                {51.51641269147, 0.04694437700},
                {51.51645347095, 0.04615346784},
                {51.51640106432, 0.04606463869},
                {51.51645623479, 0.04599504420},
                {51.51650875616, 0.04504611189},
                {51.51656302693, 0.04399636177},
                {51.51660940493, 0.04327756610},
                {51.51660940493, 0.04327756610},
                {51.51664926263, 0.04265977041},
                {51.51652676578, 0.04194803181},
                {51.51650231604, 0.04180280410},
                {51.51666810920, 0.04106071867},
                {51.51671382587, 0.04050063680},
                {51.51671958861, 0.04016938429},
                {51.51673530769, 0.03983863617},
                {51.51673530769, 0.03983863617},
                {51.51677631981, 0.03897559917},
                {51.51678956189, 0.03873116108},
                {51.51682105011, 0.03795423771},
                {51.51689074620, 0.03653040951},
                {51.51691123991, 0.03607031816},
                {51.51691123991, 0.03607031816},
                {51.51697915608, 0.03454528292},
                {51.51707675808, 0.03410281152},
                {51.51690154477, 0.03227890072},
                {51.51650891562, 0.03210287554},
                {51.51639238514, 0.03207087989},
                {51.51639238514, 0.03207087989},
                {51.51575710443, 0.03189645461},
                {51.51563180251, 0.03186205182},
                {51.51523742611, 0.03178684866},
                {51.51472598495, 0.03172085042},
                {51.51453753342, 0.03169733007},
                {51.51453753342, 0.03169733007},
                {51.51436705255, 0.03167605280},
                {51.51421454368, 0.03165485382},
                {51.51402584219, 0.03164645715},
                {51.51370185495, 0.03166086591},
                {51.51348594661, 0.03166567122},
                {51.51320564085, 0.03175408520},
                {51.51302419098, 0.03184689035},
                {51.51302419098, 0.03184689035},
                {51.51302417867, 0.03184689664},
                {51.51273388894, 0.03199251323},
                {51.51226937549, 0.03233214305},
                {51.51196810276, 0.03259256114},
                {51.51158396042, 0.03296458130},
                {51.51134633579, 0.03318459021},
                {51.51102708967, 0.03344419722},
                {51.51031681166, 0.03389906847},
                {51.51031681166, 0.03389906847},
                {51.50995403156, 0.03413139178},
                {51.50977256758, 0.03422418747},
                {51.50994455406, 0.03260336889},
                {51.50995162027, 0.02950530282},
                {51.50995162027, 0.02950530282},
                {51.50995273036, 0.02901531291},
                {51.50995568039, 0.02780489301},
                {51.50993788102, 0.02623326953},
                {51.50989546163, 0.02522218216}};
        for (double[] coord : coordinatesArray) {
            coordinates.add(new GeoPoint(coord[0], coord[1]));
        }

        Polyline polyline = new Polyline();
        polyline.setPoints(coordinates);
        polyline.setColor(Color.BLUE);  // Set the color of the polyline
        mapView.getOverlayManager().add(polyline);


        // Add markers for initial and final coordinates
        addMarker(coordinates.get(0), "Initial", R.drawable.marker);
        addMarker(coordinates.get(coordinates.size() - 1), "Final", R.drawable.finalmarker);

        // Refresh the map view
        mapView.invalidate();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void addMarker(GeoPoint position, String title, int iconResourceId) {
        Drawable markerIcon = ContextCompat.getDrawable(this, iconResourceId);
        Marker marker = new Marker(mapView);
        marker.setIcon(markerIcon);
        marker.setPosition(position);
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
    }

    // Implement other LocationListener methods as needed

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDetach();
    }
}
