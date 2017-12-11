package fr.kutussu.gricha.giftdistribution;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Validator;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.kutussu.gricha.giftdistribution.Drawer.DrawingProcessor;
import fr.kutussu.gricha.giftdistribution.model.Rule;
import fr.kutussu.gricha.giftdistribution.model.Gender;
import fr.kutussu.gricha.giftdistribution.model.Player;
import fr.kutussu.gricha.giftdistribution.model.PlayerList;
import fr.kutussu.gricha.giftdistribution.model.Project;

public class ProjectDetail extends AppCompatActivity {

    final Context context = this;
    ArrayAdapter adapter = null;
    Long projectId = null;
    EditText usernameEditText;
    EditText mailEditText;
    RadioGroup genderGroup;
    RadioButton genderButton;
    String errorMessage;
    AlertDialog alertDialog;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_detail_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_project_management);
        myToolbar.setTitle("Project detail");
        setSupportActionBar(myToolbar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            projectId = (Long) extras.get("projectId");
            String value = extras.getString("projectName");
            //The key argument here must match that used in the other activity
            SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
            Cursor resultSet = mydatabase.rawQuery("Select * from users where project_id = '" + projectId + "' order by name asc", null);
            resultSet.moveToFirst();
            ArrayList<Player> playerList = new ArrayList<>();

            while (!resultSet.isAfterLast()) {
                Long id = resultSet.getLong(0);
                String name = resultSet.getString(1);
                Gender gender = Gender.valueOf(resultSet.getString(2));
                String mail = resultSet.getString(3);
                Long projectId = resultSet.getLong(4);
                Log.i("User List Activity", name);
                playerList.add(new Player(id, name, mail, gender,projectId));
                resultSet.moveToNext();
            }

            adapter = new ArrayAdapter<Player>(this, R.layout.list_project_text_view,
                    (List<Player>) playerList);
            ListView listView = (ListView) findViewById(R.id.user_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new ItemList());
            listView.setOnItemLongClickListener(new ItemDeletion());
        }

        Button drawButton = (Button) findViewById(R.id.draw_button);
        drawButton.setOnClickListener(clickListenerDrawButton);
    }

    private View.OnClickListener clickListenerDrawButton = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (adapter.getCount() < 2) {
                Toast.makeText(context, "Pas assez de participants", Toast.LENGTH_LONG).show();
                return;
            }


            SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
            Cursor resultSet = mydatabase.rawQuery("Select r.id,u1.id as user1_id,u1.name as user1_name,u2.id as user2_id,u2.name as user2_name from rules r,users u1,users u2 where r.project_id = " + projectId + " and r.player1_id=u1.id and r.player2_id=u2.id;", null);
            resultSet.moveToFirst();
            ArrayList<Rule> ruleList = new ArrayList<>();

            while (!resultSet.isAfterLast()) {
                Long id = resultSet.getLong(0);
                Player player1 = new Player(resultSet.getLong(1),resultSet.getString(2),null,null);
                Player player2 = new Player(resultSet.getLong(3),resultSet.getString(4),null,null);

                ruleList.add(new Rule(id, player1, player2));
                resultSet.moveToNext();
            }
            PlayerList playerList = new PlayerList();
            for (int i = 0; i < adapter.getCount(); i++) {
                playerList.addPlayer((Player) adapter.getItem(i));
            }

            DrawingProcessor processor = new DrawingProcessor(ruleList, playerList.getPlayerList());
            try {
                HashMap<Player, Player> drawResult =  processor.startDraw();
                ArrayList<String> resultString = new ArrayList<>();
                for(Map.Entry<Player, Player> entry : drawResult.entrySet()){
                    resultString.add(entry.getKey().getName() + " offre à " + entry.getValue().getName());
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("resultList",resultString);
                Intent resultActivityIntent = new Intent(view.getContext(), ResultActivity.class);
                resultActivityIntent.putExtras(bundle);
                startActivity(resultActivityIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.project_detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_user_item:
                openCreationUserDialog();
                return true;
            case R.id.delete_project_menu_item:
                SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
                Project project = new Project(projectId.intValue(), "");
                DatabaseUtils.deleteProjectAndItsUsers(mydatabase, project);
                Intent openProjectIntent = new Intent(context, OpenProject.class);
                startActivity(openProjectIntent);
                finish();
                return true;
            case R.id.add_rule_menu_item:
                Bundle bundle = new Bundle();
                bundle.putLong("projectId",projectId);
                Intent ruleManagementIntent = new Intent(context, RulesManagementActivity.class);
                ruleManagementIntent.putExtras(bundle);
                startActivity(ruleManagementIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCreationUserDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        View dialogUserCreationView = li.inflate(R.layout.user_creation_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(dialogUserCreationView);

        alertDialogBuilder
                .setCancelable(true)
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Ajouter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        usernameEditText = (EditText) alertDialog.findViewById(R.id.username_user_creation_id);
                        mailEditText = (EditText) alertDialog.findViewById(R.id.mail_usercreation_id);
                        genderGroup = (RadioGroup) alertDialog.findViewById(R.id.gender_radio_group_usercreation_id);

                        if (genderGroup.getCheckedRadioButtonId() != -1) {
                            int checkedItem = genderGroup.getCheckedRadioButtonId();
                            genderButton = (RadioButton) alertDialog.findViewById(checkedItem);
                        }
                        if (userCreationFormIsValid()) {
                            createUser();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(((Dialog) alertDialog).getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // show it
        alertDialog.show();
    }

    public boolean userCreationFormIsValid() {
        if (usernameEditText == null || !usernameEditText.getText().toString().matches("([A-z0-9 ]{2,30}$)")) {
            errorMessage = "please enter a correct username (alphanumeric 30char max)";
            return false;
        }
        if (mailEditText == null || !mailEditText.getText().toString().matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}")) {
            errorMessage = "please enter a valid mail address";
            return false;
        }

        if (genderGroup == null || genderGroup.getCheckedRadioButtonId() == -1) {
            errorMessage = "please select a gender";
            return false;
        }
        return true;
    }

    public void createUser() {
        String username = usernameEditText.getText().toString();
        String mail = mailEditText.getText().toString();
        Gender gender = Gender.valueOf(genderButton.getContentDescription().toString());

        Player newPlayer = new Player(username, mail, gender, projectId);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long projectId = (Long) extras.get("projectId");
            newPlayer = DatabaseUtils.addPlayer(openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null), newPlayer);
        }
        adapter.add(newPlayer);
        adapter.notifyDataSetChanged();

    }


    class ItemList implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Player player = (Player) parent.getAdapter().getItem(position);
            Log.i("OpenProject activity", player.getId().toString());
        }
    }

    class ItemDeletion implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            final ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
            Player player = (Player) adapter.getItem(position);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                    parent.getContext());
            alertDialogBuilder.setTitle("Suppression utilisateur : " + player.getName());
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer cet utilisateur " + player.getName() + "? ");
            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.cancel();
                }
            });
            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Player player = (Player) adapter.getItem(position);
                    SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
                    Assert.assertTrue(DatabaseUtils.deletePlayer(mydatabase, player));
                    Log.i("user deletion", player.getId().toString());
                    adapter.remove(player);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create();
            alertDialogBuilder.show();

            return true;
        }
    }
}
