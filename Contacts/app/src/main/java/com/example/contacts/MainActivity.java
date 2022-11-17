package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView contactRv;
    DBHelper dbHelper;
    private AdapterContact adapterContact;

    //sort Category
    String sortByNewest = Constants.C_ADDED_TIME + " DESC";
    String sortByOldest = Constants.C_ADDED_TIME + " ASC";
    String sortByNameAsc = Constants.C_NAME + " ASC";
    String sortByNameDesc = Constants.C_NAME + " DESC";

    //current sort set
    String currentSort = sortByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        contactRv = findViewById(R.id.contactRv);
        contactRv.setHasFixedSize(true);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditContact.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });
        loadData(currentSort);
    }

    private void loadData(String currentSort) {
        adapterContact = new AdapterContact(this, dbHelper.getAllData(currentSort));
        contactRv.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(currentSort);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        //get search view
        MenuItem item = menu.findItem(R.id.search_contact);
        SearchView searchView = (SearchView) item.getActionView();
        //set max width of search view
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContact(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return true;
            }
        });
        return true;
    }

    private void searchContact(String query) {

        adapterContact = new AdapterContact(this, dbHelper.getsearchContacts(query));
        contactRv.setAdapter(adapterContact);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.deleteall:
                dbHelper.deleteAllContacts();
                onResume();
                break;
            case R.id.sortcontact:
                sortDialog();
                break;
        }
        return true;
    }

    private void sortDialog() {
        String option[] = {"SortByNewest", "SortByOldest", "SortByNameASC", "SortByNameDESC"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    loadData(sortByNewest);
                }else if(which==1){
                    loadData(sortByOldest);
                }else if(which==2){
                    loadData(sortByNameAsc);
                }else if(which==3){
                    loadData(sortByNameDesc);
                }
            }
        });
        builder.create().show();
    }
}