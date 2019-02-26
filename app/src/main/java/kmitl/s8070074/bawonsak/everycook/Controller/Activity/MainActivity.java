package kmitl.s8070074.bawonsak.everycook.Controller.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kmitl.s8070074.bawonsak.everycook.Controller.Fragment.HomeFragment;
import kmitl.s8070074.bawonsak.everycook.R;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();
    }
}
