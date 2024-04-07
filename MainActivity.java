package softwood.pm.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;


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
    public void runBackgroundTask(String type){
        BackgroundTask task = new BackgroundTask(type);
        Thread thread = new Thread (task);
        thread.start();
    }
    private class BackgroundTask implements Runnable {
        private String type, error;
        Data data;
        public BackgroundTask ( String type){
            this.type = type;
        }
        @Override
        public void run(){
            try {
                data = getData(type);
                handler.post (() -> tv_Result.setText (data.getType().toString())); // Update UI from handler
            }catch (Exception e){
                e.printStackTrace();
                error = e.getMessage();
                handler.post(() -> tv_Result.setText(error)); // Update UI from handler
            }
        }
    }
    public Data getData(String type){
        double Latitude=0.0, Longitude=0.0;

        return new Data(Latitude, Longitude, type);
    }
}
