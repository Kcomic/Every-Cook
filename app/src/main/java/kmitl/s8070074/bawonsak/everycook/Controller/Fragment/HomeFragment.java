package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Adapter.FoodAdapter;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.FoodDetailActivity;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
                    /*Map<String, Object> materails = (Map<String, Object>) m.get("materails");
                    ArrayList<String> materailsList = new ArrayList<>();
                    for(String mate : materails.keySet()){
                        materailsList.add(mate);
                    }*/
                    food = new Food(key, (ArrayList<String>) m.get("method"), m.get("detail").toString(), (Map<String, String>) m.get("materials"), (Map<String, String>) m.get("garnish"), m.get("rating").toString(), m.get("view").toString(), m.get("url").toString());

                    Map<String, Object> userList = (Map<String, Object>) all.get("Member");
                    Map<String, Object> user = (Map<String, Object>) userList.get(m.get("username"));
                    Member member = new Member(m.get("username").toString(), user.get("name").toString(), user.get("rating").toString(), user.get("fullname").toString(), user.get("url").toString());
                    food.setMember(member);

                    Map<String, Object> allComment = (Map<String, Object>) all.get("Comment");
                    Map<String, Object> commentList = (Map<String, Object>) allComment.get(food.getName());
                    if(commentList != null) {
                        for (String username : commentList.keySet()) {
                            Map<String, Object> m2 = (Map<String, Object>) commentList.get(username);
                            Comment comment = new Comment(m2.get("message").toString(), username, m2.get("date").toString(), m2.get("time").toString());
                            comments.add(comment);
                        }
                        food.setComment(comments);
                    }
                    foods.add(food);
                }
                setAdapter(foods);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
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
    public void onItemTouched(Food food) {
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra("food", food);
        intent.putExtra("member", member);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
