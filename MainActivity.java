package softwood.pm.async;

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
    private Button button;
    private EditText time;
    private TextView finalResult;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        time = (EditText) findViewById(R.id.in_time);
        button = (Button) findViewById(R.id.btn_run);
        finalResult = (TextView) findViewById(R.id.tv_result);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                runBackgroundTask();
            }
        });
    }
    public void runBackgroundTask(){
        String sleep = time.getText().toString();
        BackgroundTask task = new BackgroundTask(Integer.parseInt(sleep));
        Thread thread = new Thread (task);
        thread.start();
    }
    private class BackgroundTask implements Runnable {
        private int time;
        private String result;

        public BackgroundTask (int time){
            this.time = time;
        }
        @Override
        public void run(){
            try {
                Thread.sleep(time * 1000);
                result = "Slept for " + time + " seconds";
                handler.post (() -> finalResult.setText (result)); // Update UI from handler
            }catch (InterruptedException e){
                e.printStackTrace();
                result = e.getMessage();
                handler.post(() -> finalResult.setText(result)); // Update UI from handler
            }

        }
    }
}
