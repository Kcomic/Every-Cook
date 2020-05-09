package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Adapter.FoodAdapter;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.FoodDetailActivity;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.MainActivity;
import kmitl.s8070074.bawonsak.everycook.Model.Comment;
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements FoodAdapter.FoodAdapterListener {

    private OnFragmentInteractionListener mListener;
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerView;
    private Member member;
    private DatabaseReference mRootRef;
    private List<Food> foods;
    private Food food;
    private ArrayList<Comment> comments;
    private ArrayList<String> materialList;
    private MaterialSearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    public HomeFragment() {

    }


    public static HomeFragment newInstance(Member member) {
        HomeFragment fragment = new HomeFragment();
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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.foodList);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        ButterKnife.bind(this, rootView);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Every Cook");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        query();

        searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                setAdapter(foods);

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<Food> foodFound = new ArrayList<>();
                    for(Food food:foods){
                        if(food.getName().contains(newText))
                            foodFound.add(food);
                    }
                    setAdapter(foodFound);
                }
                else{
                    setAdapter(foods);
                }
                return true;
            }

        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("member", member);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        return rootView;
    }

    public void query(){
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    public void setData(DataSnapshot dataSnapshot){
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
            Log.d("Add Comment", food.getName());
            Map<String, Object> allComment = (Map<String, Object>) all.get("Comment");
            Map<String, Object> commentList = (Map<String, Object>) allComment.get(food.getName());
            if(commentList != null) {
                Log.d("Add Comment", "adding..");
                Log.d("Add Comment", "size = "+commentList.size());
                for (String times : commentList.keySet()) {
                    Log.d("Add Comment", "key = "+times);
                    Map<String, Object> m2 = (Map<String, Object>) commentList.get(times);
                    Log.d("Add Comment", "comment = "+m2.get("message").toString());
                    Comment comment = new Comment(m2.get("message").toString(), m2.get("date").toString(), m2.get("time").toString());
                    user = (Map<String, Object>) userList.get(m2.get("username").toString());
                    Member memberComment = new Member(m.get("username").toString(), user.get("name").toString(), user.get("rating").toString(), user.get("fullname").toString(), user.get("url").toString());
                    comment.setMember(memberComment);
                    comments.add(comment);
                }
                food.setComment(comments);
            }
            foods.add(food);
        }
        setAdapter(foods);
    }
    public void setAdapter(List<Food> foods){
        foodAdapter = new FoodAdapter(getActivity(), HomeFragment.this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter.setFoods(foods);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateMaterial(ArrayList<String> materialList){
        mRootRef.child("Material").setValue(materialList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> getMaterialList(){
        ArrayList<String> materialList = new ArrayList<>();
        for(Food f : foods){
            for(Map.Entry<String, String> materials : f.getMaterials().entrySet()){
                int i = 0;
                for(i = 0; i < materialList.size(); i++) {
                    if(materials.getKey().equals(materialList.get(i))) break;
                }
                if(i == materialList.size()) materialList.add(materials.getKey());
            }
        }
        return materialList;
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
