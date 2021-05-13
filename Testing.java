package com.example.android.dancecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DanceMoves extends AppCompatActivity {
    ArrayList<DaneMoveObject> danceMoves = new ArrayList<DaneMoveObject>();
    ArrayList<DaneMoveObject> choreography = new ArrayList<DaneMoveObject>();
    ArrayList selectedDance = new ArrayList();
    LinearLayout lLayout;
    LinearLayout rLayout;
    CheckBox checkBox;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText name;
    private Button save;
    private Button cancel;
    private  Button createChor;
    private String myText;
    private Button createNewDance;
    private MqttClient mMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_moves);
        DaneMoveObject dance1  = new DaneMoveObject("Testing",1);
        danceMoves.add(dance1);
        DaneMoveObject dance2  = new DaneMoveObject("Dance 2",2);
        danceMoves.add(dance2);
        DaneMoveObject dance3  = new DaneMoveObject("Dance 3",3);
        danceMoves.add(dance3);
        DaneMoveObject dance4  = new DaneMoveObject("Dance 4",4);
        danceMoves.add(dance4);
        lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
        rLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
        createChor = findViewById(R.id.createChoreography);
        createNewDance = findViewById(R.id.createNewDanceMove);
        createNewDance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(DanceMoves.this);
                myDialog.setTitle("Name");

                final EditText name = new EditText(DanceMoves.this);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                myDialog.setView(name);

                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myText = name.getText().toString();
                        addNewDance(myText);
                    }
                });

                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        createChor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(DanceMoves.this);
                myDialog.setTitle("Name");

                final EditText name = new EditText(DanceMoves.this);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                myDialog.setView(name);

                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myText = name.getText().toString();
                        createChoreography(myText);
                    }
                });

                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        for (int i = 0; i < danceMoves.size(); i++) {
            DaneMoveObject dance = danceMoves.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(dance.getId());
            checkBox.setText(dance.getDanceName());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, dance));
            lLayout.addView(checkBox);
        }

        for (int i = 0; i < choreography.size(); i++) {
            DaneMoveObject dance = choreography.get(i);
            checkBox = new CheckBox(this);
            checkBox.setId(dance.getId());
            checkBox.setText(dance.getDanceName());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, dance));
            rLayout.addView(checkBox);
        }
    }

    View.OnClickListener getOnClickDoSomething(final CheckBox checkBox, final DaneMoveObject dance){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    selectedDance.add(dance.getDanceName());
                    TextView current = findViewById(R.id.currentDances);
                    current.setText(selectedDance.toString());
                }else{
                    selectedDance.remove(dance.getDanceName());
                    TextView current = findViewById(R.id.currentDances);
                    current.setText(selectedDance.toString());
                }
                Log.d("onClick: ", "CheckBox ID: " + checkBox.getId() + " Text: " + checkBox.getText().toString());
            }
        };
    }

    public void goToDrive(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createChoreography(String name){
        if(!selectedDance.isEmpty() && selectedDance.size() > 1 && selectedDance.size() < 101) {
            DaneMoveObject newChor = new DaneMoveObject(name, 11);
            choreography.add(newChor);
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_R);
            int id = 1;
            checkBox = new CheckBox(this);
            checkBox.setId(id);
            checkBox.setText(name);
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, newChor));
            lLayout.addView(checkBox);
            Toast.makeText(DanceMoves.this, myText + " was created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please select at least 2 moves but you can choose 100 :) ", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBackToDanceMenu(View view){
        Intent intent = new Intent(this, DanceMode.class);
        startActivity(intent);
    }

    public void makeCarDance(View view){
            mMqttClient.publish("smartcar/makeCarDance", "1", 1, null);
        }

    public void addNewDance(String name){
        if(!selectedDance.isEmpty() && selectedDance.size() > 1 && selectedDance.size() < 101) {
            DaneMoveObject newDance = new DaneMoveObject(name, 10);
            danceMoves.add(newDance);
            lLayout = (LinearLayout) findViewById(R.id.linear_Layout_Dance_L);
            checkBox = new CheckBox(this);
            int id = 1;
            checkBox.setId(id);
            checkBox.setText(name);
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox, newDance));
            lLayout.addView(checkBox);
            Toast.makeText(DanceMoves.this, myText + " was created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please select a dance move", Toast.LENGTH_SHORT).show();
        }
        }

        public void recordNewDance(View view){
            Intent intent = new Intent(this, NewMoves.class);
            startActivity(intent);
        }
    }
