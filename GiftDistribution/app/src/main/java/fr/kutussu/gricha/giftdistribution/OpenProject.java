package fr.kutussu.gricha.giftdistribution;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.kutussu.gricha.giftdistribution.model.Project;

public class OpenProject extends AppCompatActivity {
Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_project);

        SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
        Cursor resultSet = mydatabase.rawQuery("Select * from projects order by name asc", null);
        if(resultSet.getCount()==0){
            Toast.makeText(context, "Aucun projet existant", Toast.LENGTH_LONG).show();
            finish();
        }
        resultSet.moveToFirst();
        ArrayList<Project> projectList = new ArrayList<>();

        while (!resultSet.isAfterLast()) {
            Integer id = resultSet.getInt(0);
            String name = resultSet.getString(1);
            Log.i("Open Project Activity", name);
            projectList.add(new Project(id, name));
            resultSet.moveToNext();
        }

        ArrayAdapter adapter = new ArrayAdapter<Project>(this, R.layout.list_project_text_view,
                (List<Project>) projectList);
        ListView listView = (ListView) findViewById(R.id.open_project_list_view);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemClickListener(new ItemList());
        listView.setOnItemLongClickListener(new ItemDeletion());
    }

    class ItemList implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Project project = (Project) parent.getAdapter().getItem(position);
            Intent userCreationIntent = new Intent(view.getContext(), ProjectDetail.class);
            Bundle bundle = new Bundle();
            bundle.putLong("projectId", project.getId());
            bundle.putString("projectName", project.getName());
            userCreationIntent.putExtras(bundle);
            startActivity(userCreationIntent);
        }
    }

    class ItemDeletion implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            final ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
            Project project = (Project) parent.getAdapter().getItem(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    parent.getContext());
            alertDialogBuilder.setTitle("Suppression project : " + project.getName());
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer le project " + project.getName() + "? ");
            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Project project = (Project) adapter.getItem(position);
                    SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution", MODE_PRIVATE, null);
                    DatabaseUtils.deleteProjectAndItsUsers(mydatabase, project);
                    Log.i("project deletion", project.getId().toString());
                    adapter.remove(project);
                    adapter.notifyDataSetChanged();
                    if(adapter.getCount()==0){
                        Toast.makeText(context, "Aucun projet existant", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create();
            alertDialogBuilder.show();

            return true;
        }
    }
}
