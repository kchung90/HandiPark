package ca.bcit.handipark.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ca.bcit.handipark.CardViewAdapter;
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

    private PageViewModel pageViewModel;
//    private ArrayList<Card> cardArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

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


//        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        OkHttpClient client = new OkHttpClient();
//        String url = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=disability-parking&facet=description&facet=notes&facet=geo_local_area";
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                JSONObject json = new JSONObject(myResponse);
//                                JSONArray jsonArray = json.getJSONArray("records");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject record = jsonArray.getJSONObject(i);
//                                    JSONObject fields = record.getJSONObject("fields");
//                                    String location = fields.getString("location");
//                                    int space = fields.getInt("spaces");
//                                    String notes = fields.getString("notes");
//                                    cardArrayList.add(new Card(location, space, notes));
//                                }
//
//                                adapter = new CardViewAdapter(cardArrayList);
//                                recyclerView.setAdapter(adapter);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                }
//            }
//        });

        return root;
    }

}