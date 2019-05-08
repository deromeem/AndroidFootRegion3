package fr.lla_sio.androidfootregion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MatchActivity extends AppCompatActivity {

    // paramêtres de connexion à transmettre par intent à l'activité suivante
    String userLogin = "";
    String userPwd = "";
    private static final String TAG_USER_LOGIN = "login";
    private static final String TAG_USER_PWD = "pwd";

    // contrôles de la vue (layout) correspondante
    TextView txtNom;
    TextView txtDate;
    TextView txtScore_domicile;
    TextView txtScore_invite;
    TextView txtAdr_rue;
    TextView txtAdr_ville;
    TextView txtAdr_cp;
    TextView txtCoord_GPS;
    TextView txtEntraineur_initiateur;
    TextView txtEntraineur_invite;
    TextView txtEquipe_domicile;
    TextView txtEquipe_invite;
    TextView txtTournoi;
    TextView txtStatut;

    // tableau JSON des détails de l'élément
    JSONArray item = null;

    // noms des noeuds JSON
    private static final String TAG_TASK = "matchs";
    private static final String TAG_ID = "id";
    private static final String TAG_NOM = "nom";
    private static final String TAG_DATE = "date_heure";
    private static final String TAG_SCORE_DOMICILE = "score_domicile";
    private static final String TAG_SCORE_INVITE = "score_invite";
    private static final String TAG_ADR_RUE = "adr_rue";
    private static final String TAG_ADR_VILLE = "adr_ville";
    private static final String TAG_ADR_CP = "adr_cp";
    private static final String TAG_COORD_GPS = "coord_gps";
    private static final String TAG_ENTRAINEUR_INITIATEUR = "entraineur_initiateur";
    private static final String TAG_ENTRAINEUR_INVITE = "entraineur_invite";
    private static final String TAG_EQUIPE_DOMICILE = "equipe_domicile";
    private static final String TAG_EQUIPE_INVITE = "equipe_invite";
    private static final String TAG_TOURNOI = "tournoi";
    private static final String TAG_STATUT = "statut";


    // variables associées aux noeuds JSON
    String aid = "1";
    String nom = "";
    String date = "";
    String score_domicile = "0";
    String score_invite = "0";
    String adr_rue = "";
    String adr_ville = "";
    String adr_cp = "";
    String coord_gps = "";
    String entraineur_initiateur = "";
    String entraineur_invite = "";
    String equipe_domicile = "";
    String equipe_invite = "";
    String tournoi = "";
    String statut = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        // récupération de l'intent de la vue de détail :
        Intent in = getIntent();

        // obtention des paramêtres de connexion à partir de l'intent :
        userLogin = in.getStringExtra(TAG_USER_LOGIN);
        userPwd = in.getStringExtra(TAG_USER_PWD);

        // obtention de l'aid de l'élément à partir de l'intent :
        aid = in.getStringExtra(TAG_ID);
        // DEBUG : affichage temporaire de aid
        // Toast.makeText(OffreActivity.this, "Elément demandé aid : "+aid, Toast.LENGTH_LONG).show();

        // chargement de l'élément en fil d'exécution de fond (background thread) :
        new GetItem().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Tâche de fond pour charger les détails d'un élément par une requête HTTP :
     * */
    class GetItem extends AsyncTask<String, String, JSONObject> {

        // url pour obtenir un élément :
        String apiUrl = "";

        JSONParser jsonParser2 = new JSONParser();

        private ProgressDialog pDialog2;

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        // affiche une barre de progression avant d'activer la tâche de fond :
        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
            // DEBUG : affichage temporaire de aid
            // Toast.makeText(MatchActivity.this, "onPreExecute aid : "+aid, Toast.LENGTH_LONG).show();

            pDialog2 = new ProgressDialog(MatchActivity.this);
            pDialog2.setMessage("Chargement des détails de l'élément. Veuillez attendre...");
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();

            // apiUrl = "http://" + getString(R.string.pref_default_api_url_loc) + "/index.php";
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            apiUrl = "http://" + SP.getString("PREF_API_URL_LOC", getString(R.string.pref_default_api_url_loc)) + "/index.php";
            String prefAPI = SP.getString("PREF_API", "0");
            if (prefAPI.equals("1")) {
                apiUrl = "http://" + SP.getString("PREF_API_URL_DIST", getString(R.string.pref_default_api_url_dist)) + "/index.php";
            }
            // Toast.makeText(MatchActivity.this,"URL de l'API : " + apiUrl,Toast.LENGTH_LONG).show();
        }

        // obtention en tâche de fond de l'élément au format JSON par une requête HTTP :
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("login", userLogin);
                params.put("pwd", userPwd);
                params.put("task", TAG_TASK);
                params.put("id", aid);

                Log.d("request", "starting");

                // JSONObject json = jsonParser2.makeHttpRequest(apiUrl, "GET", params);
                JSONObject json = jsonParser2.makeHttpRequest(apiUrl, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

            // updating UI from Background Thread
            // runOnUiThread(new Runnable() {
        }

        // ferme la boite de dialogue à la terminaison de la tâche de fond :
        protected void onPostExecute(JSONObject json) {
            int success = 0;
            String message = "";

            if (pDialog2 != null && pDialog2.isShowing()) {
                pDialog2.dismiss();
            }

            if (json != null) {
                // Toast.makeText(MessageActivity.this, json.toString(), Toast.LENGTH_LONG).show();  // TEST/DEBUG
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (success == 1) {
                Log.d("Success!", message);
                // Details de l'élément reçu :
                try {
                    item = json.getJSONArray(TAG_TASK);
                    // obtention du premier objet à partir du tableau JSON
                    JSONObject obj = item.getJSONObject(0);

                    // enregistrement de chaque élément JSON dans une variable
                    nom = obj.getString(TAG_NOM);
                    date = obj.getString(TAG_DATE);
                    score_domicile = obj.getString(TAG_SCORE_DOMICILE);
                    score_invite = obj.getString(TAG_SCORE_INVITE);
                    adr_rue = obj.getString(TAG_ADR_RUE);
                    adr_ville = obj.getString(TAG_ADR_VILLE);
                    adr_cp = obj.getString(TAG_ADR_CP);
                    coord_gps = obj.getString(TAG_COORD_GPS);
                    entraineur_initiateur = obj.getString(TAG_ENTRAINEUR_INITIATEUR);
                    entraineur_invite = obj.getString(TAG_ENTRAINEUR_INVITE);
                    equipe_domicile = obj.getString(TAG_EQUIPE_DOMICILE);
                    equipe_invite = obj.getString(TAG_EQUIPE_INVITE);
                    tournoi = obj.getString(TAG_TOURNOI);
                    statut = obj.getString(TAG_STATUT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d("Failure", message);
            }

            // mise à jour de l'interface utilisateur (UI) depuis le thread principal :
            runOnUiThread(new Runnable() {
                public void run() {
                    // mise à jour des TextView avec les données JSON :
                    txtNom = (TextView) findViewById(R.id.nom);
                    txtDate = (TextView) findViewById(R.id.date);
                    txtScore_domicile = (TextView) findViewById(R.id.score_domicile);
                    txtScore_invite = (TextView) findViewById(R.id.score_invite);
                    txtAdr_rue = (TextView) findViewById(R.id.adr_rue);
                    txtAdr_ville = (TextView) findViewById(R.id.adr_ville);
                    txtAdr_cp = (TextView) findViewById(R.id.adr_cp);
                    txtCoord_GPS = (TextView) findViewById(R.id.coord_gps);
                    txtEntraineur_initiateur = (TextView) findViewById(R.id.entraineur_initiateur);
                    txtEntraineur_invite = (TextView) findViewById(R.id.entraineur_invite);
                    txtEquipe_domicile = (TextView) findViewById(R.id.equipe_domicile);
                    txtEquipe_invite = (TextView) findViewById(R.id.equipe_invite);
                    txtTournoi = (TextView) findViewById(R.id.tournoi);
                    txtStatut = (TextView) findViewById(R.id.statut);

                    // affiche les données de l'élément dans les TextView :
                    txtNom.setText(nom);
                    txtDate.setText(date);
                    txtScore_domicile.setText(score_domicile);
                    txtScore_invite.setText(score_invite);
                    txtAdr_rue.setText(adr_rue);
                    txtAdr_ville.setText(adr_ville);
                    txtAdr_cp.setText(adr_cp);
                    txtCoord_GPS.setText(coord_gps);
                    txtEntraineur_initiateur.setText(entraineur_initiateur);
                    txtEntraineur_invite.setText(entraineur_invite);
                    txtEquipe_domicile.setText(equipe_domicile);
                    txtEquipe_invite.setText(equipe_invite);
                    txtTournoi.setText(tournoi);
                    txtStatut.setText(statut);
                }
            });
        }
    }
}
