package dev.milos.permissionsexample;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    ListView listContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        init();

    }

    private void init(){
        listContacts = (ListView) findViewById(R.id.listContacts);
        showContacts();

    }

    private void showContacts() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);

        }else{

            List<String> contacts = getContactNames();
            Collections.sort(contacts);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            listContacts.setAdapter(adapter);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showContacts();
            }else{
                Toast.makeText(this, "Morate potvrditi dozvolu za prikaz kontakata.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<String> getContactNames() {
        List<String> contacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);

        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return contacts;

    }

}
