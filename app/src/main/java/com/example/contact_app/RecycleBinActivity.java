package com.example.contact_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecycleBinActivity extends AppCompatActivity {
    private ArrayList<Contact> recycleBinContacts;
    private ArrayAdapter<Contact> recycleBinAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        // Initialize the ArrayList to avoid NullPointerException
        recycleBinContacts = new ArrayList<>();

        // Get the data passed from the MainActivity
        if (getIntent() != null && getIntent().hasExtra("recycleBinContacts")) {
            // Retrieve the list passed from MainActivity
            recycleBinContacts = (ArrayList<Contact>) getIntent().getSerializableExtra("recycleBinContacts");
        }

        ListView listView = findViewById(R.id.recycle_bin_list);

        // Set up the adapter
        recycleBinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recycleBinContacts);
        listView.setAdapter(recycleBinAdapter);

        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Restore or Permanently Delete");
            dialog.setMessage("Would you like to restore or permanently delete this contact?");

            dialog.setPositiveButton("Restore", (dialogInterface, i) -> {
                restoreContact(position);
            });

            dialog.setNegativeButton("Permanently Delete", (dialogInterface, i) -> {
                permanentlyDeleteContact(position);
            });

            dialog.show();
            return true; // Event handled
        });
    }

    private void restoreContact(int position) {
        Contact contact = recycleBinContacts.remove(position);
        // Logic to restore the contact to the main contact list
        MyApplication.contacts.add(contact); // Adding it back to the main list
        recycleBinAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact restored.", Toast.LENGTH_SHORT).show();
    }

    private void permanentlyDeleteContact(int position) {
        recycleBinContacts.remove(position);
        recycleBinAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact permanently deleted.", Toast.LENGTH_SHORT).show();
    }
}
