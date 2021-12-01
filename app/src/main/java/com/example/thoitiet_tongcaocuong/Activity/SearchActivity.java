package com.example.thoitiet_tongcaocuong.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

import com.example.thoitiet_tongcaocuong.Adapter.AdapterStringList;
import com.example.thoitiet_tongcaocuong.R;
import com.example.thoitiet_tongcaocuong.SQLHelper;
import com.example.thoitiet_tongcaocuong.SearchHistoryList;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    ListView lvSearch;
    Button btn_back;
    AdapterStringList adapterStringList;
    SQLHelper sqlHelper;
    List<SearchHistoryList> searchHistoryLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /*-----------------------------------------------------------------------------------------------------------------------------------------*/
        // custom actionbar
//        color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.design_default_color_primary)));
//        title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_title_action_bar);
        /*-----------------------------------------------------------------------------------------------------------------------------------------*/


        searchView = findViewById(R.id.svSearch);
        lvSearch = findViewById(R.id.lvSearch);
        btn_back = findViewById(R.id.btn_back);
        sqlHelper = new SQLHelper(this);

        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sqlHelper.onAddList(query);
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("cityname", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<SearchHistoryList> searchHistoryLists = sqlHelper.onGetList();
                SearchHistoryList searchHistoryList = searchHistoryLists.get(position);
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                String cityname = searchHistoryList.getString();
                intent.putExtra("cityname", cityname);
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//

        lvSearch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item = position;
                new AlertDialog.Builder(SearchActivity.this)
                        .setIcon(R.drawable.weather_screen_icon)
                        .setTitle("Clear search history")
                        .setMessage("Clear all ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sqlHelper.onDeleteAll(which_item);
                                searchHistoryLists.clear();
                                adapterStringList.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null).show();

                return true;
            }

        });

        List<SearchHistoryList> searchHistoryLists = sqlHelper.onGetList();
        adapterStringList = new AdapterStringList(searchHistoryLists);
        lvSearch.setAdapter(adapterStringList);

    }
}