package com.example.juego_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The type Main activity 2 nivel 3.
 */
public class MainActivity2_nivel3 extends AppCompatActivity {

    private TextView tv_name, tv_score;
    private ImageView iv_AOne, iv_ATwo, iv_lives;
    private EditText et_answer;
    private MediaPlayer mp, mp_great, mp_bad;
    int score, randomNum_one, randomNum_two, result, lives = 3;
    String player_name, string_score, string_lives;
    String numbers[] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_nivel3);

        Toast.makeText(this, "Nivel 3 - Restas", Toast.LENGTH_SHORT).show();

        tv_name = (TextView)findViewById(R.id.textView_nombre);
        tv_score = (TextView)findViewById(R.id.textView_score);
        iv_lives = (ImageView)findViewById(R.id.imageView_vidas);
        iv_AOne = (ImageView)findViewById(R.id.imageView_NumUno);
        iv_ATwo = (ImageView)findViewById(R.id.imageView_NumDos);
        et_answer = (EditText)findViewById(R.id.editText_resultado);

        //We bring the name of the player from the previous Activity
        player_name = getIntent().getStringExtra("jugador");
        tv_name.setText("Jugador: " + player_name);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        string_lives = getIntent().getStringExtra("vidas");
        lives = Integer.parseInt(string_lives);
        if (lives == 3){
            iv_lives.setImageResource(R.drawable.tresvidas);
        }
        if (lives == 2){
            iv_lives.setImageResource(R.drawable.dosvidas);
        }
        if (lives == 1){
            iv_lives.setImageResource(R.drawable.unavida);
        }

        //We put the icon in the ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //We adjust the background music
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);
        //We also carry the sound of hit and miss
        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        RandomNum();

    }

    /**
     * Compare.
     *
     * @param view the view
     */
    public void Compare(View view){
        String answer = et_answer.getText().toString();

        if (!answer.equals("")){

            int player_answer = Integer.parseInt(answer);

            if (result == player_answer){

                mp_great.start();
                score++;
                tv_score.setText("Score: " + score);
                et_answer.setText("");
                BaseDeDatos();

            } else {

                mp_bad.start();
                lives--;
                BaseDeDatos();

                switch (lives){
                    case 3:
                        iv_lives.setImageResource(R.drawable.tresvidas);
                        break;

                    case 2:
                        Toast.makeText(this, "Te quedan 2 manzanas", Toast.LENGTH_LONG).show();
                        iv_lives.setImageResource(R.drawable.dosvidas);
                        break;

                    case 1:
                        Toast.makeText(this, "Te queda 1 manzana", Toast.LENGTH_LONG).show();
                        iv_lives.setImageResource(R.drawable.unavida);
                        break;

                    case 0:
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }

                et_answer.setText("");
            }

            RandomNum();

        } else {
            Toast.makeText(this, "Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Random Num.
     */
    public void RandomNum(){
        if(score <=29){

            randomNum_one = (int) (Math.random() * 10);
            randomNum_two = (int) (Math.random() * 10);

            result = randomNum_one - randomNum_two;

            if (result >= 0){
                for (int i = 0; i< numbers.length; i++){
                    int id = getResources().getIdentifier(numbers[i], "drawable", getOpPackageName());

                    if (randomNum_one == i){
                        iv_AOne.setImageResource(id);
                    }

                    if (randomNum_two == i){
                        iv_ATwo.setImageResource(id);
                    }
                }
            } else {
                RandomNum();
            }

        } else {
            Intent intent = new Intent(this, MainActivity2_nivel4.class);

            string_score = String.valueOf(score);
            string_lives = String.valueOf(lives);

            intent.putExtra("jugador", player_name);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_lives);

            startActivity(intent);
            finish();
            mp.stop();
            mp.release();
        }
    }

    /**
     * Base de datos.
     */
    public void BaseDeDatos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery("SELECT * FROM puntaje WHERE score = (SELECT MAX(score) FROM puntaje)", null);

        if (consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_score);

            if (score > bestScore){
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", player_name);
                modificacion.put("score", score);

                BD.update("puntaje", modificacion, "score=" + bestScore, null);
            }
            BD.close();

        } else {
            ContentValues insertar = new ContentValues();

            insertar.put("nombre", player_name);
            insertar.put("score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }

    @Override
    public void onBackPressed(){

    }
}