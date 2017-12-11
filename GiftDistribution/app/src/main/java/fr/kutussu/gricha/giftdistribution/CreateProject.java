package fr.kutussu.gricha.giftdistribution;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateProject extends AppCompatActivity {
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        Button createProjectButton = (Button) findViewById(R.id.create_project_button);
        createProjectButton.setOnClickListener(clickListenerCreateProjectButton);
    }

    private View.OnClickListener clickListenerCreateProjectButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText text = (EditText) findViewById(R.id.new_project_name);
            String value = text.getText().toString();
            SQLiteDatabase mydatabase = openOrCreateDatabase("gift_distribution",MODE_PRIVATE,null);
            ContentValues values = new ContentValues();

            //  values.put(KEY_ID, information.getId());
            values.put("name", value);
            long projectId = mydatabase.insert("projects",null,values);

            Intent userCreationIntent = new Intent(view.getContext(), ProjectDetail.class);
            Bundle bundle = new Bundle();
            bundle.putLong("projectId",projectId);
            bundle.putString("projectName",value);
            finish();
            userCreationIntent.putExtras(bundle);
            startActivity(userCreationIntent);


        }

    };
}
