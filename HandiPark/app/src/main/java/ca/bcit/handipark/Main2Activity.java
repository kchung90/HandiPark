package ca.bcit.handipark;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import ca.bcit.handipark.ui.main.SectionsPagerAdapter;

public class Main2Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView list;
    SearchView editsearch;
    ArrayAdapter<String> adapter;
    String[] animalNameList;
    ArrayList<String> arraylist = new ArrayList<>();
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.search,
                R.drawable.favorite,
                R.drawable.history
        };
        for(int i=0; i<tabs.getTabCount(); i++){
            if(tabs.getTabAt(i) != null){
                Objects.requireNonNull(tabs.getTabAt(i)).setIcon(tabIcons[i]);
            }
        }

        animalNameList = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};

        list = (ListView) findViewById(R.id.listview);
        list.setVisibility(View.INVISIBLE);

        Collections.addAll(arraylist, animalNameList);

        adapter = new ArrayAdapter<>(Main2Activity.this, android.R.layout.simple_selectable_list_item, arraylist);

        list.setAdapter(adapter);

        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(Main2Activity.this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() < 1){
            viewPager.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            viewPager.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
            adapter.getFilter().filter(query);
        }
        editsearch.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() < 1){
            viewPager.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            viewPager.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
            adapter.getFilter().filter(newText);
        }
        return false;
    }
}