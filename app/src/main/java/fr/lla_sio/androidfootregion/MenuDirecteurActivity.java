package fr.lla_sio.androidfootregion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuDirecteurActivity extends AppCompatActivity {

    // paramètres de connexion à transmettre par intent à l'activité suivante
    String userLogin = "";
    String userPwd = "";
    private static final String TAG_USER_LOGIN = "login";
    private static final String TAG_USER_PWD = "pwd";

    // contrôles de la vue (layout) correspondante
    TextView txtUserName;
    Button btnViewMatchs;
    Button btnViewMesClubs;
    Button btnViewDiscussions;


    String userName = "";
    String userGroup = "";
    private static final String TAG_USER_NAME = "name";
    private static final String TAG_USER_GROUP = "frgroup";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_directeur);

        // récupération de l'intent de la vue courante
        Intent in = getIntent();
        // obtention des paramêtres du user connecté à partir de l'intent
        userName = in.getStringExtra(TAG_USER_NAME);
        userGroup = in.getStringExtra(TAG_USER_GROUP);
        userLogin = in.getStringExtra(TAG_USER_LOGIN);
        userPwd = in.getStringExtra(TAG_USER_PWD);

        // déclaration des textes et boutons
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(userName + " (" + userGroup + ")");
        btnViewMatchs = (Button) findViewById(R.id.btnViewMatchs);
        btnViewMesClubs = (Button) findViewById(R.id.btnViewMesClubs);
        btnViewDiscussions = (Button) findViewById(R.id.btnViewDiscussions);

        // événement de clic sur le bouton de visualisation des discussions :
        btnViewDiscussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), DiscussionsActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

        // événement de clic sur le bouton de visualisation des matchs :
        btnViewMatchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MatchsActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

        // événement de clic sur le bouton de visualisation de mes clubs :
        btnViewMesClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MesClubsActivity.class);
                in.putExtra(TAG_USER_LOGIN, userLogin);
                in.putExtra(TAG_USER_PWD, userPwd);
                startActivity(in);
            }
        });

    }
}

