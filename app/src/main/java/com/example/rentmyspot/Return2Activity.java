package com.example.rentmyspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class Return2Activity extends AppCompatActivity {
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return2);

        // Retrieve the current user's username from the intent
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");

        displayRentedSeatings(currentUser);
    }

    private void displayRentedSeatings(String currentUser) {
        DBHelper dbHelper = new DBHelper(this);
        List<Seating> rentedSeatings = dbHelper.getAllRentedSeatings(currentUser);

        ListView rentedSeatingsListView = findViewById(R.id.rentlist);
        ArrayAdapter<Seating> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rentedSeatings);
        rentedSeatingsListView.setAdapter(adapter);

        rentedSeatingsListView.setOnItemClickListener((parent, view, position, id) -> {
            Seating selectedSeating = rentedSeatings.get(position);
            boolean isRemoved = dbHelper.removeRentedSeating(selectedSeating);

            if (isRemoved) {
                Toast.makeText(this, "Seating returned successfully!", Toast.LENGTH_SHORT).show();
                rentedSeatings.remove(position);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error returning seating.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}