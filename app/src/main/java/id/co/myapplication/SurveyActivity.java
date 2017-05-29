package id.co.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {
    static String TAG = "SurveyActivity";
    private Map<String, String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Intent intent = getIntent();
        String name = intent.getStringExtra("nama");
        String id = intent.getStringExtra("id");
        String lat = intent.getStringExtra("lat"); // Jangan lupa ubah ke double
        String lng = intent.getStringExtra("lng");

        data = new HashMap<String, String>();

        data.put("location_id", id);

        TextView locationName = (TextView) findViewById(R.id.location_name);
        locationName.setText(name);

        Button submitButton = (Button) findViewById(R.id.submit);
        final RadioGroup qualifiedStatus = (RadioGroup) findViewById(R.id.qualified_status);
        final RadioGroup verifiedStatus = (RadioGroup) findViewById(R.id.verified_status);
        final EditText info = (EditText) findViewById(R.id.info);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedQualStats = qualifiedStatus.getCheckedRadioButtonId();
                int selectedVerStats = verifiedStatus.getCheckedRadioButtonId();

                RadioButton qualStats = (RadioButton) findViewById(selectedQualStats);
                RadioButton verStats = (RadioButton) findViewById(selectedVerStats);

                int qualified;
                int verfied;

                if (qualStats.getText().equals("Yes"))
                    qualified = 1;
                else qualified = 0;
                if (verStats.getText().equals("Yes"))
                    verfied = 1;
                else verfied = 0;
                String information = String.valueOf(info.getText());

                data.put("qualified", String.valueOf(qualified));
                data.put("verified", String.valueOf(verfied));
                data.put("information", String.valueOf(information));

                uploadDataSurvey(data, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if(result.equals("success")){
                            Toast.makeText(SurveyActivity.this, "Upload data successful", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                            Toast.makeText(SurveyActivity.this, "Upload data failed. Try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void uploadDataSurvey(final Map<String, String> data, final VolleyCallback callback){
        boolean final_status;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.address+"uploadsurvey",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location_id", data.get("location_id"));
                params.put("qualified", data.get("qualified"));
                params.put("verified", data.get("verified"));
                params.put("information", data.get("information"));
                params.put("user_id", "5");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SurveyActivity.this);
        requestQueue.add(stringRequest);
    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }
}
