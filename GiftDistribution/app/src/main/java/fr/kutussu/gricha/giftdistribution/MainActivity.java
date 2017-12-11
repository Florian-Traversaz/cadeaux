package fr.kutussu.gricha.giftdistribution;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS projects(id INTEGER PRIMARY KEY,name VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,gender BOOLEAN,mail VARCHAR,project_id INTEGER, FOREIGN KEY (project_id) REFERENCES projects(id)); ");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS rules(id INTEGER PRIMARY KEY AUTOINCREMENT,project_id INTEGER,player1_id INTEGER,player2_id INTEGER, FOREIGN KEY (project_id) REFERENCES projects(id),FOREIGN KEY (player1_id) REFERENCES users(id),FOREIGN KEY (player2_id) REFERENCES users(id)); ");

        rLayout = (RelativeLayout) findViewById(R.id.activity_main);
        Button createProjectButton = (Button) findViewById(R.id.create_project_button);
        createProjectButton.setOnClickListener(clickListenerCreateProjectButton);

        Button openProjectButton = (Button) findViewById(R.id.open_project_button);
        openProjectButton.setOnClickListener(clickListenerOpenProjectButton);

    }

    private View.OnClickListener clickListenerCreateProjectButton = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), CreateProject.class);
            startActivity(myIntent);

        }

    };

    private View.OnClickListener clickListenerOpenProjectButton = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), OpenProject.class);
            startActivity(myIntent);

        }

    };

    private EditText showNewProjectNameEditText() {
        final EditText editText = (EditText) findViewById(R.id.new_project_name);
        editText.setVisibility(View.VISIBLE);
        return editText;
    }

}
