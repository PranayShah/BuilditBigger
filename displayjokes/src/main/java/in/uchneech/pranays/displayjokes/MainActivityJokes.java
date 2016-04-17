package in.uchneech.pranays.displayjokes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
public class MainActivityJokes extends AppCompatActivity {

    private static final String JOKE_CONSTANT = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("dis", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String result = intent.getStringExtra(JOKE_CONSTANT);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
