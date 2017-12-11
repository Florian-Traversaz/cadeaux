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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import fr.kutussu.gricha.giftdistribution.model.Gender;
import fr.kutussu.gricha.giftdistribution.model.Player;
import fr.kutussu.gricha.giftdistribution.model.Project;
import fr.kutussu.gricha.giftdistribution.model.Rule;

/**
 * Created by a555917 on 17/02/2017.
 */
public class RulesManagementActivity extends AppCompatActivity {

    Long projectId;
    Context context = this;
    Spinner spinnerUser1;
    Spinner spinnerUser2;
    AlertDialog alertDialog;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rules_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_add_rules);
        myToolbar.setTitle("Rules Management");
        setSupportActionBar(myToolbar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            projectId = (Long) extras.get("projectId");
            //The key argument here must match that used in the other activity
            SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
            Cursor resultSet = mydatabase.rawQuery("Select r.id,u1.id,u1.name,u2.id,u2.name from rules r,users u1,users u2 where r.project_id = '" + projectId + "' and r.player1_id=u1.id and r.player2_id=u2.id", null);
            resultSet.moveToFirst();
            ArrayList<Rule> rulesList = new ArrayList<>();

            while (!resultSet.isAfterLast()) {
                Long id = resultSet.getLong(0);
                Long player1_id = resultSet.getLong(1);
                String player1_name = resultSet.getString(2);
                Player player1 = new Player(player1_id, player1_name, null, null);
                Long player2_id = resultSet.getLong(3);
                String player2_name = resultSet.getString(4);
                Player player2 = new Player(player2_id, player2_name, null, null);
                rulesList.add(new Rule(id, player1, player2));
                resultSet.moveToNext();
            }

            adapter = new ArrayAdapter<Rule>(this, R.layout.list_project_text_view,
                    (List<Rule>) rulesList);
            ListView listView = (ListView) findViewById(R.id.rules_list_view);
            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(new RulesManagementActivity().ItemList());
            listView.setOnItemLongClickListener(new RulesManagementActivity.ItemDeletion());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_rule_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_rule_item:
                openAddRuleDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddRuleDialog() {

        LayoutInflater li = LayoutInflater.from(context);
        View dialogUserCreationView = li.inflate(R.layout.add_rule_dialog_layout, null);

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
                final SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
                Cursor resultSet = mydatabase.rawQuery("Select * from users where project_id = '" + projectId + "' order by name asc", null);
                resultSet.moveToFirst();
                ArrayList<Player> playerList = new ArrayList<>();

                while (!resultSet.isAfterLast()) {
                    Long id = resultSet.getLong(0);
                    String name = resultSet.getString(1);
                    Gender gender = Gender.valueOf(resultSet.getString(2));
                    String mail = resultSet.getString(3);
                    Long projectId = resultSet.getLong(4);
                    playerList.add(new Player(id, name, mail, gender, projectId));
                    resultSet.moveToNext();
                }

                final SpinnerAdapter adapterUsers = new ArrayAdapter<Player>(alertDialog.getContext(), R.layout.list_project_text_view, (List<Player>) playerList);

                spinnerUser1 = (Spinner) alertDialog.findViewById(R.id.player1_rule_spinner);
                spinnerUser1.setAdapter(adapterUsers);

                spinnerUser2 = (Spinner) alertDialog.findViewById(R.id.player2_rule_spinner);
                spinnerUser2.setAdapter(adapterUsers);


                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Player player1 = (Player) spinnerUser1.getSelectedItem();
                        Player player2 = (Player) spinnerUser2.getSelectedItem();
                        Rule rule = new Rule(player1, player2);
                        if (DatabaseUtils.checkRuleAlreadyExists(mydatabase, rule, projectId) || player1.getId() == player2.getId()) {
                            Toast.makeText(((Dialog) alertDialog).getContext(), "Cette regle existe déjà", Toast.LENGTH_LONG).show();
                        }
                        else{
                            DatabaseUtils.addRule(mydatabase, rule, projectId);
                            adapter.add(rule);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        // show it
        alertDialog.show();
    }


    class ItemDeletion implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            final ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
            Rule rule = (Rule) adapter.getItem(position);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                    parent.getContext());
            alertDialogBuilder.setTitle("Suppression regle : " + rule.toString());
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer cette règle: " + rule.toString() + "? ");
            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.cancel();
                }
            });
            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Rule rule = (Rule) adapter.getItem(position);
                    SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
                    Assert.assertTrue(DatabaseUtils.deleteRule(mydatabase, rule));

                    adapter.remove(rule);
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
