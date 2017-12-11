package fr.kutussu.gricha.giftdistribution;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.kutussu.gricha.giftdistribution.model.Gender;
import fr.kutussu.gricha.giftdistribution.model.Player;

/**
 * Created by a555917 on 16/02/2017.
 */
public class ResultActivity extends AppCompatActivity {
    ArrayAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_detail_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> resultList = extras.getStringArrayList("resultList");
            adapter = new ArrayAdapter<String>(this, R.layout.list_project_text_view,
                    (List<String>) resultList);
            ListView listView = (ListView) findViewById(R.id.draw_result_list_view);
            listView.setAdapter(adapter);
        }
    }
}
