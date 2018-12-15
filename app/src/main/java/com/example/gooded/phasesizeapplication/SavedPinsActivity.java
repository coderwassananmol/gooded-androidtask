package com.example.gooded.phasesizeapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class SavedPinsActivity extends AppCompatActivity {

    private PinsViewModel pinsViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_pins);

        recyclerView = findViewById(R.id.recyclerview);
        final PinsAdapter adapter = new PinsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), (view, position) -> {
            // TODO Handle item click
            double geoLocationCoords[] = adapter.getGeoLocation(position);
            double latitude = geoLocationCoords[0];
            double longitude = geoLocationCoords[1];
            Log.e("@@@@@",latitude+","+longitude);
        }));
        pinsViewModel = ViewModelProviders.of(this).get(PinsViewModel.class);

        // Update the cached copy of the words in the adapter.
        pinsViewModel.getAllWords().observe(this, adapter::setPins);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Pins pin = new Pins(24,24);
            pinsViewModel.insert(pin);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}