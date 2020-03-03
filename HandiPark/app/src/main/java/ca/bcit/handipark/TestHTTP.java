package ca.bcit.handipark;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestHTTP extends AppCompatActivity {

    private TextView testViewRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_test);

        testViewRes = findViewById(R.id.text_view_result);

        OkHttpClient client = new OkHttpClient();

//        String base_url = "https://jsonplaceholder.typicode.com/todos";
//        String id_url = "/4";
//        String url = base_url + id_url;


        String url = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=disability-parking&facet=description&facet=notes&facet=geo_local_area";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    TestHTTP.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);
                                testViewRes.setText(json.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //testViewRes.setText(myResponse);
                        }
                    });
                }
            }
        });
        testViewRes.setMovementMethod(new ScrollingMovementMethod());
    }
}
