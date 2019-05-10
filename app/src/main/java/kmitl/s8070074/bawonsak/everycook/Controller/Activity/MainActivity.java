package kmitl.s8070074.bawonsak.everycook.Controller.Activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Controller.Fragment.CreateMenuFragment;
import kmitl.s8070074.bawonsak.everycook.Controller.Fragment.FavoriteMenuFragment;
import kmitl.s8070074.bawonsak.everycook.Controller.Fragment.HomeFragment;
import kmitl.s8070074.bawonsak.everycook.Controller.Fragment.ProfileFragment;
import kmitl.s8070074.bawonsak.everycook.Controller.Fragment.RecommendMenuFragment;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.homeBtn) LinearLayout homeBtn;
    @BindView(R.id.favoriteBtn) LinearLayout favoriteBtn;
    @BindView(R.id.recommendBtn) LinearLayout recommendBtn;
    @BindView(R.id.createBtn) LinearLayout createBtn;
    @BindView(R.id.profileBtn) LinearLayout profileBtn;
    @BindView(R.id.homeIv) ImageView homeIv;
    @BindView(R.id.favoriteIv) ImageView favoriteIv;
    @BindView(R.id.recommendIv) ImageView recommendIv;
    @BindView(R.id.createIv) ImageView createIv;
    @BindView(R.id.profileIv) ImageView profileIv;
    @BindView(R.id.homeTv) TextView homeTv;
    @BindView(R.id.favoriteTv) TextView favoriteTv;
    @BindView(R.id.recommendTv) TextView recommendTv;
    @BindView(R.id.createTv) TextView createTv;
    @BindView(R.id.profileTv) TextView profileTv;

    private FragmentManager fragmentManager;
    private Member member;
    private ArrayList<String> materialList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Reset();
        fragmentManager = getSupportFragmentManager();
        homeTv.setTextColor(Color.parseColor("#ff4656"));
        //homeIv.setImageResource(R.drawable.ic_press_home);
        Intent intent = getIntent();
        member = intent.getParcelableExtra("member");
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment().newInstance(member))
                .commit();
    }

    @Override
    public void onClick(View view) {
        if(R.id.homeBtn == view.getId()){
            Reset();
            homeBtn.setOnClickListener(null);
            homeTv.setTextColor(Color.parseColor("#ff4656"));
            //homeIv.setImageResource(R.drawable.ic_press_home);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        } else if (R.id.favoriteBtn == view.getId()){
            Reset();
            favoriteBtn.setOnClickListener(null);
            favoriteTv.setTextColor(Color.parseColor("#ff4656"));
            //favoriteIv.setImageResource(R.drawable.ic_press_time);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new FavoriteMenuFragment())
                    .commit();
        } else if (R.id.recommendBtn == view.getId()){
            Reset();
            recommendBtn.setOnClickListener(null);
            recommendTv.setTextColor(Color.parseColor("#ff4656"));
            //recommendIv.setImageResource(R.drawable.ic_press_rating);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new RecommendMenuFragment())
                    .commit();
        } else if (R.id.createBtn == view.getId()){
            Reset();
            createBtn.setOnClickListener(null);
            createTv.setTextColor(Color.parseColor("#ff4656"));
            //createIv.setImageResource(R.drawable.ic_press_rating);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new CreateMenuFragment())
                    .commit();
        } else if (R.id.profileBtn == view.getId()){
            Reset();
            profileBtn.setOnClickListener(null);
            profileTv.setTextColor(Color.parseColor("#ff4656"));
            //profileIv.setImageResource(R.drawable.ic_press_user);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new ProfileFragment())
                    .commit();
        }
    }

    private void Reset(){
        homeTv.setTextColor(Color.parseColor("#727272"));
        favoriteTv.setTextColor(Color.parseColor("#727272"));
        recommendTv.setTextColor(Color.parseColor("#727272"));
        createTv.setTextColor(Color.parseColor("#727272"));
        profileTv.setTextColor(Color.parseColor("#727272"));
//        homeIv.setImageResource(R.drawable.ic_menu_home);
//        favoriteIv.setImageResource(R.drawable.ic_menu_time);
//        recommendIv.setImageResource(R.drawable.ic_menu_rating);
//        createIv.setImageResource(R.drawable.ic_rating);
//        profileIv.setImageResource(R.drawable.ic_menu_user);
        homeBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
        recommendBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);

    }

}
