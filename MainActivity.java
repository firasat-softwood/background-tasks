package softwood.pm.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {

    //Create variables for the two buttons on screen.
    Button btn_InTime, btn_OutTime;
    TextView tv_Result;
    private static final String API_URL = "http://206.42.124.10:8000/api";
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Get the LayoutInflater instance
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_main);

        btn_InTime = (Button) findViewById(R.id.btn_InTime);
        btn_OutTime = (Button) findViewById(R.id.btn_OutTime);
        tv_Result = (TextView) findViewById(R.id.tv_Result);


        //Run this functions upon clicking the In Time Button.
        btn_InTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runBackgroundTask("InTime");
            }
        });

        //Run this functions upon clicking the Out Time Button.
        btn_OutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runBackgroundTask("OutTime");
            }
        });
    }

    public void runBackgroundTask(String type) {
        BackgroundTask task = new BackgroundTask(type);
        LocationHelper locationHelper = new LocationHelper(this); // Pass "this" as context
        task.setLocationHelper(locationHelper); // Optional: Pass LocationHelper to BackgroundTask
        Thread thread = new Thread(task);
        thread.start();
    }

    private class BackgroundTask implements Runnable {
        private String type, result;
        Data data;
        private LocationHelper locationHelper;

        public BackgroundTask(String type) {
            this.type = type;
        }

        // Add a setter method for LocationHelper
        public void setLocationHelper(LocationHelper locationHelper) {
            this.locationHelper = locationHelper;
        }

        @Override
        public void run() {
            try {
                //if (locationHelper != null) {
                    //Log.d("BackgroundTask", "Trying to get location for: " + type);
                    data = locationHelper.getCurrentLocationData(type);
                    Gson gson = new Gson();
                    String json = gson.toJson(data);
                    result = json.toString();
                    handler.post(() -> tv_Result.setText(result));
                //}
            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
                handler.post(() -> tv_Result.setText(result));
                //Log.e("BackgroundTask", "Error in BackgroundTask: " + result);
            }
        }
    }

    public class LocationHelper {

        private Context context;
        private LocationManager locationManager;

        public LocationHelper(MainActivity context) {
            this.context = context;
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        public Data getCurrentLocationData(String type) throws Exception {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);

                throw new Exception("Location permission required");
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            // Handle null location case (e.g., GPS disabled)
            if (location == null) {
                throw new Exception("Error, try again");
            }

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            return new Data(latitude, longitude, type);
        }
    }
}
