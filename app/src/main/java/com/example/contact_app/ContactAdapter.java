package com.example.contact_app;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    public interface ContactClicked {
        void showContactOnContentF(int position);
        void moveToRecycleBin(int position); // New method for Recycle Bin
        void deleteContact(int position); // Permanent delete
        void updateContact(Contact updatedContact, int position); // Update contact
    }

    ContactClicked parentActivity;

    public ContactAdapter(Context c, ArrayList<Contact> data) {
        super(c, 0, data);
        parentActivity = (ContactClicked) c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_contact_item_design, parent, false);
        }

        TextView tvName = v.findViewById(R.id.tvName);
        Contact contact = getItem(position);
        tvName.setText(contact.getName());

        v.setOnClickListener(view -> {
            parentActivity.showContactOnContentF(position);
        });

        v.setOnLongClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(parent.getContext());
            dialog.setTitle("Contact Options");
            dialog.setMessage("Do you want to update, delete, or move this contact to the recycle bin?");

            dialog.setPositiveButton("Move to Recycle Bin", (dialogInterface, which) -> {
                parentActivity.moveToRecycleBin(position); // Move to Recycle Bin
            });

            dialog.setNegativeButton("Delete Permanently", (dialogInterface, which) -> {
                parentActivity.deleteContact(position); // Permanent delete
            });

            dialog.setNeutralButton("Update", (dialogInterface, which) -> {
                showContactUpdateDialog(parentActivity, position, contact);
            });

            dialog.show();
            return true; // Event handled
        });

        return v;
    }

    private void showContactUpdateDialog(ContactClicked listener, int position, Contact contact) {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(getContext());
        editDialog.setTitle("Update Contact");
        View updateView = LayoutInflater.from(getContext())
                .inflate(R.layout.contact_edit_form, null, false);

        EditText etName = updateView.findViewById(R.id.etName);
        EditText etPhone = updateView.findViewById(R.id.etPhone);
        EditText etUrl = updateView.findViewById(R.id.etUrl);
        EditText etAddress = updateView.findViewById(R.id.etAddress);

        etName.setText(contact.getName());
        etPhone.setText(contact.getPhone());
        etUrl.setText(contact.getUrl());
        etAddress.setText(contact.getAddress());

        editDialog.setView(updateView);

        editDialog.setPositiveButton("Save", (dialogInterface, which) -> {
            contact.setName(etName.getText().toString());
            contact.setPhone(etPhone.getText().toString());
            contact.setUrl(etUrl.getText().toString());
            contact.setAddress(etAddress.getText().toString());
            listener.updateContact(contact, position);
        });

        editDialog.setNegativeButton("Cancel", null);

        editDialog.show();
    }
}
