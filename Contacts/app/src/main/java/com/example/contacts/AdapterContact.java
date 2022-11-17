package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder>{

    Context context;
    ArrayList<ModelContact> contactList;
    DBHelper dbHelper;

    public AdapterContact(Context context, ArrayList<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_contact, parent, false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ModelContact modelContact =  contactList.get(position);

        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();
        String phone = modelContact.getPhone();
        String email = modelContact.getEmail();
        String note = modelContact.getNote();
        String added_time = modelContact.getAddedtime();
        String updated_time = modelContact.getUpdatedtime();

        holder.Contactname.setText(name);
        if(image.equals("")){
            holder.Contactimage.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            holder.Contactimage.setImageURI(Uri.parse(image));
        }
        holder.Contactdial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailContact.class);
                intent.putExtra("ContactId", id);
                context.startActivity(intent);
            }
        });

        holder.contact_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent to move addedit activity to upadte data
                Intent intent = new Intent(context, AddEditContact.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("IMAGE", image);
                intent.putExtra("PHONE", phone);
                intent.putExtra("EMAIL", email);
                intent.putExtra("NOTE", note);
                intent.putExtra("ADDED_TIME", added_time);
                intent.putExtra("UPDATED_TIME", updated_time);

                //boolean data to define it is for edit purpose
                intent.putExtra("IsEditMode", true);
                context.startActivity(intent);
            }
        });

        holder.contact_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteContact(id);

                //refresh data by calling resume state of Main Activity
                ((MainActivity)context).onResume();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

         ImageView Contactimage, Contactdial;
         TextView Contactname, contact_edit, contact_delete;
         RelativeLayout relativeLayout;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            Contactimage = itemView.findViewById(R.id.contactimage);
            Contactdial = itemView.findViewById(R.id.contact_dial_number);
            Contactname = itemView.findViewById(R.id.contact_name);
            contact_edit = itemView.findViewById(R.id.contact_edit);
            contact_delete = itemView.findViewById(R.id.contact_delete);
            relativeLayout = itemView.findViewById(R.id.mainLlayout);

        }
    }
     }

