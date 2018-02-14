package com.jonbott.knownspies.Activities.SpyList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonbott.knownspies.Activities.Details.SpyDetailsActivity;
import com.jonbott.knownspies.Helpers.Constants;
import com.jonbott.knownspies.Helpers.Threading;
import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Database.Realm.Spy;
import com.jonbott.knownspies.ModelLayer.Enums.Source;
import com.jonbott.knownspies.ModelLayer.Translation.SpyTranslator;
import com.jonbott.knownspies.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpyListActivity extends AppCompatActivity {

    private static final String TAG = "SpyListActivity";

    private List<Spy> spies = new ArrayList<>();
    private RecyclerView recyclerView;

    SpyListPresenter spyListPresenter = new SpyListPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spy_list);

        attachUI();
        loadData();
    }

    //region Helper Methods
    private void attachUI() {
        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.spy_recycler_view);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        initializeListView();
    }


    private void loadData() {

        spyListPresenter.loadData(spies -> {

            this.spies = spies;
            SpyViewAdapter adapter = (SpyViewAdapter) recyclerView.getAdapter();
            adapter.spies = this.spies;
            adapter.notifyDataSetChanged();

        }, this::notifyDataReceived);
    }


    //region User Interaction

    private void rowTapped(int position) {
        Spy spy = spies.get(position);
        gotoSpyDetails(spy.id);
    }

    private void notifyDataReceived(Source source) {
        String message = String.format("Data from %s", source.name());
        Toast.makeText(SpyListActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    private void initializeListView() {
        SpyViewAdapter adapter = new SpyViewAdapter(spies, (v, position) -> rowTapped(position));
        recyclerView.setAdapter(adapter);
    }

    //endregion

    //region Navigation

    private void gotoSpyDetails(int spyId) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.spyIdKey, spyId);

        Intent intent = new Intent(SpyListActivity.this, SpyDetailsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    //endregion

}
