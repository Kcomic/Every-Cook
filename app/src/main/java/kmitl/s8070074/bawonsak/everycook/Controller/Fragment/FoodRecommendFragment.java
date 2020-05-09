package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Adapter.FoodAdapter;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.FoodDetailActivity;
import kmitl.s8070074.bawonsak.everycook.Model.Comment;
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodRecommendFragment extends Fragment implements FoodAdapter.FoodAdapterListener {

    private HomeFragment.OnFragmentInteractionListener mListener;
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerView;
    private Member member;
    private DatabaseReference mRootRef;
    private List<Food> foods;
    private HashMap<String, String> choose;
    private Food food;
    private ArrayList<Comment> comments;
    private ArrayList<String> materialList;

    public FoodRecommendFragment() {
        // Required empty public constructor
    }

    public static FoodRecommendFragment newInstance(Member member, HashMap<String, String> choose) {
        FoodRecommendFragment fragment = new FoodRecommendFragment();
        Bundle args = new Bundle();
        args.putParcelable("member", member);
        args.putSerializable("choose", choose);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member = getArguments().getParcelable("member");
        choose = (HashMap<String, String>) getArguments().getSerializable("choose");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        foods = new ArrayList<>();
        comments = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_food_recommend, container, false);
        recyclerView = rootView.findViewById(R.id.foodList);
        ButterKnife.bind(this, rootView);
        query();
        return rootView;
    }

    public void query(){
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> all = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Object> foodList = (Map<String, Object>) all.get("Food");
                for(String key : foodList.keySet()){
                    Map<String, Object> m = (Map<String, Object>) foodList.get(key);
                    Map<String, Object> ratingMap = (Map<String, Object>) m.get("rating");
                    ArrayList<String> ratingList = new ArrayList<>();
                    if(ratingMap != null) {
                        for (String username: ratingMap.keySet()){
                            ratingList.add(ratingMap.get(username).toString());
                        }
                    }
                    food = new Food(key, (ArrayList<String>) m.get("method"), m.get("detail").toString(), (Map<String, String>) m.get("materials"), (Map<String, String>) m.get("garnish"), ratingList, m.get("view").toString(), m.get("url").toString());
                    comments = new ArrayList<>();
                    Map<String, Object> userList = (Map<String, Object>) all.get("Member");
                    Map<String, Object> user = (Map<String, Object>) userList.get(m.get("username"));
                    Member member = new Member(m.get("username").toString(), user.get("name").toString(), user.get("rating").toString(), user.get("fullname").toString(), user.get("url").toString());
                    food.setMember(member);
                    Map<String, Object> allComment = (Map<String, Object>) all.get("Comment");
                    Map<String, Object> commentList = (Map<String, Object>) allComment.get(food.getName());
                    if(commentList != null) {
                        for (String times : commentList.keySet()) {
                            Map<String, Object> m2 = (Map<String, Object>) commentList.get(times);
                            Comment comment = new Comment(m2.get("message").toString(), m2.get("date").toString(), m2.get("time").toString());
                            user = (Map<String, Object>) userList.get(m2.get("username").toString());
                            Member memberComment = new Member(m.get("username").toString(), user.get("name").toString(), user.get("rating").toString(), user.get("fullname").toString(), user.get("url").toString());
                            comment.setMember(memberComment);
                            comments.add(comment);
                        }
                        food.setComment(comments);
                    }
                    ArrayList<String> matChoose = new ArrayList<>();
                    for(String chooseKey : choose.keySet()){
                        matChoose.add(choose.get(chooseKey));
                    }
                    ArrayList<String> matList = new ArrayList<>();
                    for(String mat : food.getMaterials().keySet()){
                        matList.add(mat);
                    }
                    int check = 0;
                    for(int i =0;i<matChoose.size();i++){
                        for(int j=0;j<matList.size();j++){
                            if(matChoose.get(i).equals(matList.get(j))){
                                check++;
                                break;
                            }
                        }
                    }if(check == matChoose.size()) foods.add(food);
                }
                setAdapter(foods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void setAdapter(List<Food> foods){
        foodAdapter = new FoodAdapter(getActivity(), FoodRecommendFragment.this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter.setFoods(foods);
    }

    @Override
    public void onItemTouched(Food food, int position) {
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra("food", food);
        intent.putExtra("member", member);
        intent.putExtra("position", position);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
