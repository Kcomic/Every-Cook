package kmitl.s8070074.bawonsak.everycook.Controller.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Adapter.CommentAdapter;
import kmitl.s8070074.bawonsak.everycook.Model.Comment;
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FoodDetailActivity extends AppCompatActivity implements CommentAdapter.CommentAdapterListener {

    @BindView(R.id.foodImageDec) ImageView foodImage;
    @BindView(R.id.foodNameDec) TextView foodName;
    @BindView(R.id.detailDec) TextView detailDec;
    @BindView(R.id.materialDec) TextView materialDec;
    @BindView(R.id.garnishDec) TextView garnishDec;
    @BindView(R.id.methodDec) TextView methodDec;
    @BindView(R.id.ratingDec) TextView ratingDec;
    @BindView(R.id.member_image_dec) ImageView memberImage;
    @BindView(R.id.owner_image_dec) ImageView ownerImage;
    @BindView(R.id.owner_name_dec) TextView ownerName;
    @BindView(R.id.send_button) ImageView sendButton;
    @BindView(R.id.new_comment) TextView newComment;

    private DatabaseReference mRootRef;


    private Member member;
    private Food food;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        member = intent.getParcelableExtra("member");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        food = intent.getParcelableExtra("food");
        position = intent.getIntExtra("position", 0);
        recyclerView = findViewById(R.id.commentList);
        addnumView();
        foodName.setText(food.getName());
        detailDec.setText(food.getDetail());
        Map<String, String> materails = food.getMaterials();
        String materailText = "";
        int times = 1;
        for(String key : materails.keySet()){
            materailText += times+". "+key+" ";
            if(!materails.get(key).equals("-")){
                materailText += materails.get(key);
            }
            materailText += "\n";
            times += 1;
        }
        materialDec.setText(materailText);
        Map<String, String> garnish = food.getGarnish();
        String garnishText = "";
        times = 1;
        for(String key : garnish.keySet()){
            garnishText += times+". "+key+" ";
            if(!garnish.get(key).equals("-")){
                garnishText += garnish.get(key);
            }
            garnishText += "\n";
            times += 1;
        }
        garnishDec.setText(garnishText);
        String methodText = "";
        times = 1;
        for(String method: food.getMethod()){
            methodText += times+". "+method+"\n";
            times += 1;
        }
        methodDec.setText(methodText);
        double rating = 0;
        for(String rate : food.getRating()){
            rating += Double.valueOf(rate);
        }
        if(food.getRating().size() != 0) rating /= food.getRating().size();
        DecimalFormat df2 = new DecimalFormat("#.##");
        ratingDec.setText(df2.format(rating));
        ownerName.setText(food.getMember().getFullname());

        Glide.with(getApplicationContext())
                .load(food.getUrl())
                .into(foodImage);
        Glide.with(getApplicationContext())
                .load(member.getUrl())
                .into(memberImage);
        Glide.with(getApplicationContext())
                .load(food.getMember().getUrl())
                .into(ownerImage);
        if(food.getComments() != null) {
            for(Comment comment : food.getComments()){
                Log.d("FoodDetailActivity", comment.getMessage());
            }
            setAdapter(food.getComments());
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRootRef.child("Food").child(food.getName()).child("rating").child(member.getUsername()).setValue(Float.toString(rating));
                Toast.makeText(FoodDetailActivity.this, "Vote complete", Toast.LENGTH_SHORT).show();
            }
        });
        sendButton.setOnClickListener(view -> {
            String commentText = newComment.getText().toString();
            if(!commentText.equals("") && commentText != null) {
                String key = mRootRef.child("Comment").child(food.getName()).push().getKey();
                DatabaseReference parent = mRootRef.child("Comment").child(food.getName()).child(key);
                SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                parent.child("date").setValue(dateformatter.format(date));
                parent.child("time").setValue(timeformatter.format(date));
                parent.child("username").setValue(member.getUsername());
                parent.child("message").setValue(commentText);
                ArrayList<Comment> commentList = food.getComments();
                if(commentList == null) commentList = new ArrayList<>();
                Comment comment = new Comment(commentText, dateformatter.format(date), timeformatter.format(date));
                comment.setMember(member);
                commentList.add(comment);
                food.setComment(commentList);
                setAdapter(food.getComments());
                Toast.makeText(this, "comment complete", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "please comment more than 1 alphabet", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void setAdapter(ArrayList<Comment> comments){
        commentAdapter = new CommentAdapter(FoodDetailActivity.this, FoodDetailActivity.this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodDetailActivity.this));
        commentAdapter.setComments(comments);
    }

    public void addnumView(){
        int view = Integer.valueOf(food.getView())+1;
        food.setView(Integer.toString(view));
        mRootRef.child("Food").child(food.getName()).child("view").setValue(Integer.toString(view));
        mRootRef.child("Watch").child(member.getUsername()).child(food.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mapView = (Map<String, Object>) dataSnapshot.getValue();
                if(mapView != null){
                    for(String key : mapView.keySet()){
                        mRootRef.child("Watch").child(member.getUsername()).child(food.getName()).child("not_"+position).setValue(Integer.toString(Integer.valueOf(mapView.get(key).toString())+1));
                    }
                } else{
                    mRootRef.child("Watch").child(member.getUsername()).child(food.getName()).child("not_"+position).setValue("1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    @Override
    public void onItemTouched(Comment comment) {

    }
}
