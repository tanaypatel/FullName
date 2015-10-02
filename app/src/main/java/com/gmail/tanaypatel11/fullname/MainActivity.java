package com.gmail.tanaypatel11.fullname;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText fName, lName;

    public TextView txtFullName;

    public RadioGroup radioGroup;
    public RadioButton radFirstName, radLastName;

    List<String> data;

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fName = (EditText) findViewById(R.id.EditFirstName);
        lName = (EditText) findViewById(R.id.EditLastName);

        txtFullName = (TextView) findViewById(R.id.FullName);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radFirstName = (RadioButton) findViewById(R.id.fname);
        radLastName = (RadioButton) findViewById(R.id.lname);

        parseJSON();

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, data);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createName(View button) {
        //get selected radio button id
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find which radioButton is checked by id
        if(selectedId == radFirstName.getId()) {

            createFullName("FirstName");

        } else if(selectedId == radLastName.getId()) {

            createFullName("LastName");
        }
        else{
            Toast.makeText(getApplicationContext(), "Please select your full name display preference above",
                    Toast.LENGTH_SHORT).show();

        }




    }

    public void createFullName(String pref){
        //get the first name
        String firstName = fName.getText().toString();

        //get last name
        String lastName = lName.getText().toString();

        if(pref.equals("FirstName")) {
            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter both first and last names to proceed.",
                        Toast.LENGTH_SHORT).show();
            } else {
                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
                lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
                txtFullName.setText(firstName + " " + lastName);
            }
        }
        else{
            if(firstName.isEmpty() || lastName.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please enter both first and last names to proceed.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
                lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
                txtFullName.setText(lastName + " " + firstName);
            }
        }

    }


    public void parseJSON(){
        //Get Data From Text Resource File Contains Json Data.
        InputStream inputStream = getResources().openRawResource(R.raw.people);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Text Data", byteArrayOutputStream.toString());
        try {
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());
            //JSONObject jObjectResult = jObject.getJSONObject("Categories");
            JSONArray jArray = jObject.getJSONArray("people");
            String firstName = "";
            String lastName = "";
            data = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                firstName = jArray.getJSONObject(i).getString("first_name");
                lastName = jArray.getJSONObject(i).getString("last_name");
                Log.v("first_name", firstName);
                Log.v("last_name", lastName);
                //data.add(new String { firstName, lastName });
                data.add(firstName + " " + lastName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
