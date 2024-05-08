package com.example.contact_app;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactAdapter.ContactClicked {

    FloatingActionButton fabAdd, fabRecycleBin;
    TextView tvName;
    ImageView ivCall, ivUrl, ivAddress, ivProfile;
    ListView lvContact;
    FragmentManager manager;
    Fragment listFrag, contentFrag;
    Contact contact;
    RelativeLayout portrait, landscape;

    ContactAdapter adapter;

    ArrayList<Contact> recycleBinContacts = new ArrayList<>(); // Recycle Bin storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle saved_state = null;
        super.onCreate(saved_state);
        setContentView(R.layout.activity_main);

        init();

        fabRecycleBin = findViewById(R.id.fabRecycleBin); // Ensure this is in your layout
        fabRecycleBin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RecycleBinActivity.class);
            intent.putExtra("recycleBinContacts", recycleBinContacts); // Pass Recycle Bin contacts
            startActivity(intent);
        });

        controlOrientation();

        ivCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact.getPhone()));
            startActivity(intent);
        });

        ivUrl.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(contact.getUrl()));
            startActivity(intent);
        });

        ivAddress.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:(0,0)?q=" + contact.getAddress()));
            startActivity(intent);
        });

        fabAdd.setOnClickListener(v -> {
            showContactAddDialog();
        });
    }

    private void showContactAddDialog() {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(MainActivity.this);
        editDialog.setTitle("Add New Contact");
        View view = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.contact_edit_form, null, false);

        EditText etName = view.findViewById(R.id.etName);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etUrl = view.findViewById(R.id.etUrl);
        EditText etAddress = view.findViewById(R.id.etAddress);

        editDialog.setView(view);

        editDialog.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String url = etUrl.getText().toString().trim();

            Contact newContact = new Contact(name, phone, url, address);
            MyApplication.contacts.add(newContact);
            adapter.notifyDataSetChanged();
        });

        editDialog.setNegativeButton("Cancel", null);

        editDialog.show();
    }

    private void controlOrientation() {
        portrait = findViewById(R.id.portrait_mode);
        landscape = findViewById(R.id.landscape_mode);
        if (portrait != null) {
            manager.beginTransaction()
                    .hide(contentFrag)
                    .show(listFrag)
                    .commit();
        } else {
            manager.beginTransaction()
                    .show(contentFrag)
                    .show(listFrag)
                    .commit();
        }
    }

    private void init() {
        fabAdd = findViewById(R.id.fabAdd);

        manager = getSupportFragmentManager();
        listFrag = manager.findFragmentById(R.id.listfrag);
        contentFrag = manager.findFragmentById(R.id.contentfrag);

        tvName = findViewById(R.id.tvName);
        ivCall = findViewById(R.id.ivCall);
        ivAddress = findViewById(R.id.ivAddress);
        ivUrl = findViewById(R.id.ivUrl);
        ivProfile = findViewById(R.id.ivProfilePic);

        lvContact = findViewById(R.id.lvContacts);

        adapter = new ContactAdapter(MainActivity.this, MyApplication.contacts);
        lvContact.setAdapter(adapter);

        hideContent(); // Hide content initially
    }

    private void hideContent() {
        ivAddress.setVisibility(View.GONE);
        ivCall.setVisibility(View.GONE);
        ivUrl.setVisibility(View.GONE);
        ivProfile.setVisibility(View.GONE);

        tvName.setText("EMPTY");
    }

    private void unhideContent() {
        ivAddress.setVisibility(View.VISIBLE);
        ivCall.setVisibility(View.VISIBLE);
        ivUrl.setVisibility(View.VISIBLE);
        ivProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContactOnContentF(int position) {
        contact = MyApplication.contacts.get(position);
        tvName.setText(contact.getName());
        unhideContent();

        if (portrait != null) {
            manager.beginTransaction()
                    .show(contentFrag)
                    .hide(listFrag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void moveToRecycleBin(int position) {
        // Move contact to Recycle Bin
        recycleBinContacts.add(MyApplication.contacts.remove(position));
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact moved to Recycle Bin.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteContact(int position) {
        // Permanently delete the contact
        MyApplication.contacts.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Contact permanently deleted.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateContact(Contact updatedContact, int position) {
        MyApplication.contacts.set(position, updatedContact);
        adapter.notifyDataSetChanged();
    }
}
