package fr.lla_sio.androidfootregion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    // paramêtres de connexion à transmettre par intent à l'activité suivante
    String userLogin = "";
    String userPwd = "";
    private static final String TAG_USER_LOGIN = "login";
    private static final String TAG_USER_PWD = "pwd";

    // contrôles de la vue (layout) correspondante
    TextView txtDate;
    TextView txtLibelle;
    TextView txtDiscussion;
    TextView txtEmail;

    // tableau JSON des détails de l'élément
    JSONArray item = null;

    // noms des noeuds JSON
    private static final String TAG_TASK = "messages_emis";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_DISCUSSION = "discussion";
    private static final String TAG_EMAIL = "email";

    // variables associées aux noeuds JSON
    String aid = "1";
    String date = "";
    String libelle = "";
    String discussion = "";
    String email = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

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
            // Toast.makeText(MessageActivity.this, "onPreExecute aid : "+aid, Toast.LENGTH_LONG).show();

            pDialog2 = new ProgressDialog(MessageActivity.this);
            pDialog2.setMessage("Chargement des détails de l'élément. Veuillez attendre...");
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();

            apiUrl = "http://" + getString(R.string.pref_default_api_url_loc) + "/index.php";
            // apiUrl = "http://" + getString(R.string.pref_default_api_url_dist) + "/index.php";

            // Toast.makeText(MessageActivity.this, "URL de l'API : " + apiUrl, Toast.LENGTH_LONG).show();
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
                    date = obj.getString(TAG_DATE);
                    libelle = obj.getString(TAG_LIBELLE);
                    discussion = obj.getString(TAG_DISCUSSION);
                    email = obj.getString(TAG_EMAIL);

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
                    txtDate = (TextView) findViewById(R.id.date);
                    txtLibelle = (TextView) findViewById(R.id.libelle);
                    txtDiscussion = (TextView) findViewById(R.id.discussion);
                    txtEmail = (TextView) findViewById(R.id.email);

                    // affiche les données de l'élément dans les TextView :
                    txtDate.setText(date);
                    txtLibelle.setText(libelle);
                    txtDiscussion.setText(discussion);
                    txtEmail.setText(email);
                }
            });
        }
    }
}
