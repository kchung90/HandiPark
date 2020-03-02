package ca.bcit.handipark.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.bcit.handipark.Main2Activity;
import ca.bcit.handipark.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    ListView listBody;
    String testJSON = "{\"fields\": [{\"description\": \"Designated meter parking space\", \"notes\": \"No stopping accessible zone\", \"spaces\": 1, \"geom\": {\"type\": \"Point\", \"coordinates\": [-123.069865, 49.27218]}, \"location\": \"North Side 1600 Kitchener St\", \"geo_local_area\": \"Grandview-Woodland\"}, {\"description\": \"Designated meter parking space\", \"notes\": \"No stopping accessible zone\", \"spaces\": 1, \"geom\": {\"type\": \"Point\", \"coordinates\": [-123.101146, 49.262189]}, \"location\": \"West Side  2500 Main St\", \"geo_local_area\": \"Mount Pleasant\"}, {\"description\": \"Designated meter parking space\", \"notes\": \"No stopping accessible zone\", \"spaces\": 1, \"geom\": {\"type\": \"Point\", \"coordinates\": [-123.161686, 49.234602]}, \"location\": \"South Side 2400 W 41st Av\", \"geo_local_area\": \"Arbutus-Ridge\"}]}";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main2, container, false);

        listBody = (ListView) root.findViewById(R.id.listViewBody);

        OkHttpClient client = new OkHttpClient();
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);
                                JSONArray jsonArray = json.getJSONArray("records");

                                ArrayList<String> arrayList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject record = jsonArray.getJSONObject(i);
                                    JSONObject fields = record.getJSONObject("fields");

                                    arrayList.add(fields.toString());
                                }


                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                                listBody.setAdapter(arrayAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        return root;
    }
}