package com.example.juego_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_BestScore;
    private MediaPlayer mp;

    int random_number = (int) (Math.random() * 10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView)findViewById(R.id.imageView_Personaje);
        tv_BestScore = (TextView)findViewById(R.id.textView_BestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;

        if (random_number == 0 || random_number == 10){
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (random_number == 1 || random_number == 9){
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (random_number == 2 || random_number == 8){
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (random_number == 3 || random_number == 7){
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (random_number == 4 || random_number == 5 || random_number == 6){
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();    //Create opening write mode of the database

        Cursor consulta = BD.rawQuery(
                "SELECT * FROM puntaje WHERE score = (SELECT MAX(score) FROM puntaje)", null);
        if (consulta.moveToFirst()){ //If there is something in the query above
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_BestScore.setText("Record: " + temp_score + " de " + temp_nombre);
            BD.close();
        } else {
            BD.close();
        }

        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);

    }

    /**
     * Play.
     *
     * @param view the view
     */
//Play button method
    public void Play(View view){
        String nombre = et_nombre.getText().toString();

        if (!nombre.equals("")){
            mp.stop();
            mp.release();   //destroy what is saved in MediaPlayer

            Intent intent = new Intent(this, MainActivity2_Nivel1.class);

            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();

            //Open the keyboard to start typing in the EditText
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    //Every time you press the arrow on the phone we tell you not to do anything
    @Override
    public void onBackPressed(){

    }
}