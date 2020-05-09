package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.Model.History;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

import static java.lang.Math.sqrt;


public class RecommendMenuFragment extends Fragment implements MaterialAdapter.MaterialAdapterListener {

    private Member member;
    private DatabaseReference mRootRef;
    private ArrayList<History> histories;
    private ArrayList<String> materialList;
    private ArrayList<ArrayList<Double>> cosim;
    private ArrayList<ArrayList<Integer>> data;
    private OnFragmentInteractionListener mListener;
    private AutoCompleteTextView autoTv;
    private MaterialAdapter materialAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progress;
    private FragmentManager fragmentManager;
//    @BindView(R.id.autocomplete_materials)
//    AutoCompleteTextView textView;
    public RecommendMenuFragment() {
        // Required empty public constructor
    }

    public static RecommendMenuFragment newInstance(Member member) {
        RecommendMenuFragment fragment = new RecommendMenuFragment();
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
        histories = new ArrayList<>();
        data = new ArrayList<>();
        cosim = new ArrayList<>();
        progress = new ProgressDialog(getContext());
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recommend_menu, container, false);
        recyclerView = rootView.findViewById(R.id.materialList);
        ButterKnife.bind(this, rootView);
        progress.setMessage("loading materials");
        progress.show();
        query();
        Log.d("eiei", "กำ");
        return rootView;
    }

    public ArrayList<String> getMaterialList(){
        final ArrayList<String>[] materials = new ArrayList[]{new ArrayList<>()};
        mRootRef.child("Material").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                materials[0] = (ArrayList<String>) dataSnapshot.getValue();
                Log.d("eiei", materials[0].size()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return materials[0];
    }


    public void query(){
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> all = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Object> chooseList = (Map<String, Object>) all.get("Choose");

                for(String username : chooseList.keySet()){
                    Map<String, Object> his = (Map<String, Object>) chooseList.get(username);
                    for(String times : his.keySet()) {
                        ArrayList<Integer> materailsList = new ArrayList<>();
                        Map<String, Object> mat = (Map<String, Object>) his.get(times);
                        for(String mate : mat.keySet()){
                            materailsList.add(Integer.valueOf(mate.replaceAll("\\D+","")));
                        }
                        History h = new History(username, Integer.valueOf(times.replaceAll("\\D+","")), materailsList);
                        histories.add(h);
                    }
                }

                materialList = (ArrayList<String>) all.get("Material");
                for(String s : materialList){
                    Log.d("materail", s);
                }
                setAdapter(materialList);
                progress.cancel();
                for(History h : histories){
                    ArrayList<Integer> dimension2 = new ArrayList<>();
                    for(int i=0; i < materialList.size();i++){
                        if(h.getChoose().contains(i)) dimension2.add(1);
                        else dimension2.add(0);
                    }
                    data.add(new ArrayList<>(dimension2));
                }
                for(int row = 0; row < data.get(0).size(); row++){
                    ArrayList<Double> list = new ArrayList<>();
                    for(int col = 0; col < data.get(0).size(); col++){
                        int sum1 = 0, sum2 = 0, sum3 = 0;
                        for(int dimension1 = 0; dimension1 < data.size(); dimension1++){
                            int product = 1, dimension2 = 9;
                            for(int i = 0; i < 2; i++){
                                if(i == 0) dimension2 = row;
                                else dimension2 = col;
                                product *= data.get(dimension1).get(dimension2);
                            }
                            sum1 += product;
                            sum2 += data.get(dimension1).get(row)*data.get(dimension1).get(row);
                            sum3 += data.get(dimension1).get(col)*data.get(dimension1).get(col);
                        }
                        if(sum2 == 0 || sum3 == 0){
                            list.add(0.0);
                        } else {
                            list.add(sum1 / (sqrt(sum2) * sqrt(sum3)));
                        }
                    }
                    cosim.add(new ArrayList<>(list));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void setAdapter(List<String> materials){
        materialAdapter = new MaterialAdapter(getActivity(), RecommendMenuFragment.this);
        recyclerView.setAdapter(materialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        materialAdapter.setMaterials(materials);
    }

    @Override
    public void onItemTouched(String material, int position) {
        HashMap<String, String> choose = new HashMap<>();
        choose.put("not_"+position, material);
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new RecommendConFragment().newInstance(member, cosim, materialList, choose, position, histories))
                .commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
