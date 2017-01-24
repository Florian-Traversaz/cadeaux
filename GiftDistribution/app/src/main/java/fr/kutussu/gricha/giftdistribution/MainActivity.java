package fr.kutussu.gricha.giftdistribution;

import android.content.Intent;
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
        public void onClick(View v) {
            //recuperation des noms des projets existants

        }

    };

    private EditText showNewProjectNameEditText() {
        final EditText editText = (EditText) findViewById(R.id.new_project_name);
        editText.setVisibility(View.VISIBLE);
        return editText;
    }

}
