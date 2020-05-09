package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Adapter.FoodAdapter;
import kmitl.s8070074.bawonsak.everycook.Adapter.MaterialAdapter;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.FoodDetailActivity;
import kmitl.s8070074.bawonsak.everycook.Model.Comment;
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.Model.History;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

import static java.lang.Math.sqrt;

public class FavoriteMenuFragment extends Fragment implements FoodAdapter.FoodAdapterListener {

    private Member member;
    private DatabaseReference mRootRef;
    private ArrayList<History> histories;
    private ArrayList<String> materialList;
    private ArrayList<ArrayList<Double>> cosim;
    private ArrayList<ArrayList<Integer>> data;
    private AutoCompleteTextView autoTv;
    private MaterialAdapter materialAdapter;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private ProgressDialog progress;
    private FragmentManager fragmentManager;
    private List<Food> foods;
    private Food food;
    private ArrayList<Comment> comments;
    @BindView(R.id.textShow) TextView textShow;

    public FavoriteMenuFragment() {
        // Required empty public constructor
    }

    public static FavoriteMenuFragment newInstance(Member member) {
        FavoriteMenuFragment fragment = new FavoriteMenuFragment();
        Bundle args = new Bundle();
        args.putParcelable("member", member);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member = getArguments().getParcelable("member");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        foods = new ArrayList<>();
        comments = new ArrayList<>();
        data = new ArrayList<>();
        cosim = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = rootView.findViewById(R.id.foodList);
        ButterKnife.bind(this, rootView);
        query();
        return rootView;
    }

    public void query(){
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> all = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Object> watchList = (Map<String, Object>) all.get("Watch");
                ArrayList<Map<String, String>> watchMapList = new ArrayList<>();
                Map<String, String> memberWatch = new HashMap<>();
                for (String username : watchList.keySet()) {
                    Map<String, String> watch = new HashMap<>();
                    Map<String, Object> foodNameMap = (Map<String, Object>) watchList.get(username);
                    for (String foodName : foodNameMap.keySet()) {
                        watch.putAll((HashMap) foodNameMap.get(foodName));
                        if (username.equals(member.getUsername()))
                            memberWatch.putAll((HashMap) foodNameMap.get(foodName));
                    }
                    watchMapList.add(watch);

                }
                Map<String, Object> foodList = (Map<String, Object>) all.get("Food");

//                materialList = (ArrayList<String>) all.get("Material");
//                for(String s : materialList){
//                    Log.d("materail", s);
//                }

                for (Map<String, String> map : watchMapList) {
                    ArrayList<Integer> dimension2 = new ArrayList<>();
                    ArrayList<Integer> watchListString = new ArrayList<>();
                    for (String s : map.keySet()) {
                        watchListString.add(Integer.valueOf(s.replaceAll("\\D+", "")));
                    }
                    for (int i = 0; i < foodList.size(); i++) {
                        if (watchListString.contains(i)) {
                            Log.d("favarite", map.get("not_" + i));
                            dimension2.add(Integer.valueOf(map.get("not_" + i)));
                        } else dimension2.add(0);
                    }
                    data.add(new ArrayList<>(dimension2));
                }
                for (ArrayList<Integer> r : data) {
                    Log.d("d", "row ...");
                    for (int i = 0; i < r.size(); i++) {
                        Log.d("d", i + " = " + r.get(i));
                    }
                }
                for (int row = 0; row < data.get(0).size(); row++) {
                    ArrayList<Double> list = new ArrayList<>();
                    for (int col = 0; col < data.get(0).size(); col++) {
                        int sum1 = 0, sum2 = 0, sum3 = 0;
                        for (int dimension1 = 0; dimension1 < data.size(); dimension1++) {
                            int product = 1, dimension2 = 9;
                            for (int i = 0; i < 2; i++) {
                                if (i == 0) dimension2 = row;
                                else dimension2 = col;
                                product *= data.get(dimension1).get(dimension2);
                            }
                            sum1 += product;
                            sum2 += data.get(dimension1).get(row) * data.get(dimension1).get(row);
                            sum3 += data.get(dimension1).get(col) * data.get(dimension1).get(col);
                        }
                        if (sum2 == 0 || sum3 == 0) {
                            list.add(0.0);
                        } else {
                            list.add(sum1 / (sqrt(sum2) * sqrt(sum3)));
                        }
                    }
                    cosim.add(new ArrayList<>(list));
                }
                for (ArrayList<Double> row : cosim) {
                    for (int i = 0; i < row.size(); i++) {
                        Log.d("d", i + " = " + row.get(i));
                    }
                }
                if (memberWatch.size() != 0){
                    for (int q = 0; q < 3; q++) {
                        Map<String, Integer> newMap = new HashMap<>();
                        for (String key2 : memberWatch.keySet()) {
                            newMap.put(key2, Integer.valueOf(memberWatch.get(key2)));
                        }
                        String positionWithNot = Collections.max(newMap.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
                        int position = Integer.valueOf(positionWithNot.replaceAll("\\D+", ""));
                        ArrayList<Double> row = cosim.get(position);
                        Double max = 0.0;
                        int maxIndex = -1;
                        for (int i = 0; i < row.size(); i++) {
                            if (row.get(i) > max & row.get(i) != 0.0 & i != position) {
                                max = row.get(i);
                                maxIndex = i;
                            }
                        }
                        if (maxIndex != -1) row.set(maxIndex, 0.0);

                        int y = 0;
                        for (String key : foodList.keySet()) {
                            Log.d("food", "y = " + y + ", " + key);
                            Map<String, Object> m = (Map<String, Object>) foodList.get(key);
                            Map<String, Object> ratingMap = (Map<String, Object>) m.get("rating");
                            ArrayList<String> ratingList = new ArrayList<>();
                            if (ratingMap != null) {
                                for (String username : ratingMap.keySet()) {
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
                            if (commentList != null) {
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
                            if (y == maxIndex) foods.add(food);
                            y++;
                        }
                    }
                } else {
                    textShow.setVisibility(View.VISIBLE);
                }
                setAdapter(foods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void setAdapter(List<Food> foods){
        foodAdapter = new FoodAdapter(getActivity(), FavoriteMenuFragment.this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter.setFoods(foods);
    }

    public <K, V extends Comparable<V>> V maxUsingCollectionsMaxAndLambda(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = Collections.max(map.entrySet(), (Map.Entry<K, V> e1, Map.Entry<K, V> e2) -> e1.getValue()
                .compareTo(e2.getValue()));
        return maxEntry.getValue();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
