package fr.lla_sio.androidfootregion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreenActivity extends AppCompatActivity {

    // paramètres de connexion à transmettre par intent à l'activité suivante
    String userLogin = "";
    String userPwd = "";
    private static final String TAG_USER_LOGIN = "login";
    private static final String TAG_USER_PWD = "pwd";

    // contrôles de la vue (layout) correspondante
    TextView txtUserName;
    Button btnViewMesMessages;
    Button btnViewJoueurs;
    Button btnViewMatchs;
    Button btnViewMesClubs;
    Button btnViewDiscussions;


    String userName = "";
    private static final String TAG_USER_NAME = "name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // récupération de l'intent de la vue courante
        Intent in = getIntent();
        // obtention des paramêtres du user connecté à partir de l'intent
        userName = in.getStringExtra(TAG_USER_NAME);
        userLogin = in.getStringExtra(TAG_USER_LOGIN);
        userPwd = in.getStringExtra(TAG_USER_PWD);

        // déclaration des textes et boutons
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(userName);
        btnViewMesMessages = (Button) findViewById(R.id.btnViewMesMessages);
        btnViewJoueurs = (Button) findViewById(R.id.btnViewJoueurs);
        btnViewMatchs = (Button) findViewById(R.id.btnViewMatchs);
        btnViewMesClubs = (Button) findViewById(R.id.btnViewMesClubs);
        btnViewDiscussions = (Button) findViewById(R.id.btnViewDiscussions);

        // événement de clic sur le bouton de visualisation des modèles
        btnViewMesMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MesMessagesActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

        // événement de clic sur le bouton de visualisation des commandes
        btnViewJoueurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), JoueursActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

        // événement de clic sur le bouton de visualisation des commandes
        btnViewMatchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MesMatchsActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

        // événement de clic sur le bouton de visualisation des commandes
        btnViewMesClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MesClubsActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

        btnViewDiscussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), DiscussionsActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

    }
}
