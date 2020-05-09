package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Adapter.MaterialAdapter;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.FoodDetailActivity;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.MainActivity;
import kmitl.s8070074.bawonsak.everycook.DataHelper;
import kmitl.s8070074.bawonsak.everycook.Model.History;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendConFragment extends Fragment implements MaterialAdapter.MaterialAdapterListener {

    private Member member;
    private DatabaseReference mRootRef;
    private ArrayList<History> histories;
    private ArrayList<String> materialList;
    private ArrayList<ArrayList<Double>> cosim;
    private ArrayList<ArrayList<Integer>> data;
    private int position;
    private RecommendMenuFragment.OnFragmentInteractionListener mListener;
    private AutoCompleteTextView autoTv;
    private HashMap<String, String> choose;
    private MaterialAdapter materialAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progress;
    private ArrayList<TextView> recommends;
    private FragmentManager fragmentManager;
    @BindView(R.id.recommend1) TextView recommend1;
    @BindView(R.id.recommend2) TextView recommend2;
    @BindView(R.id.recommend3) TextView recommend3;

    @BindView(R.id.okButton) Button okButton;
    @BindView(R.id.cancelButton) Button cancelButton;

    public RecommendConFragment() {
        // Required empty public constructor
    }

    public static RecommendConFragment newInstance(Member member, ArrayList<ArrayList<Double>> cosim, ArrayList<String> materialList, HashMap<String, String> choose, int position, ArrayList<History> histories) {
        RecommendConFragment fragment = new RecommendConFragment();
        Bundle args = new Bundle();
        args.putParcelable("member", member);
        args.putParcelable("cosim", new DataHelper(cosim));
        args.putStringArrayList("materialList", materialList);
        args.putSerializable("choose", choose);
        args.putInt("position", position);
        args.putParcelableArrayList("histories", histories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        position = getArguments().getInt("position");
        member = getArguments().getParcelable("member");
        DataHelper dataHelper = getArguments().getParcelable("cosim");
        cosim = dataHelper.getCosim();
        histories = getArguments().getParcelableArrayList("histories");
        materialList = getArguments().getStringArrayList("materialList");
        choose = (HashMap<String, String>) getArguments().getSerializable("choose");
        recommends = new ArrayList<>();
        progress = new ProgressDialog(getContext());
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommend_con, container, false);
        recyclerView = rootView.findViewById(R.id.materialListCon);
        ButterKnife.bind(this, rootView);
        progress.setMessage("loading materials");
        progress.show();
        setAdapter(materialList);
        ArrayList<Double> row = cosim.get(position);
        recommends.add(recommend1);
        recommends.add(recommend2);
        recommends.add(recommend3);
        for(int i = 0; i<row.size(); i++){
            Log.d("d", i+" = "+row.get(i));
        }
        for(int q = 0; q < 3; q++) {
            Double max = 0.0;
            int maxIndex = -1;
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) > max & row.get(i) != 0.0 & i != position) {
                    max = row.get(i);
                    maxIndex = i;
                }
            }
                if (maxIndex == -1 || max == 0.0) {
                    recommends.get(q).setText("");
                } else {
                    row.set(maxIndex, 0.0);
                    recommends.get(q).setText(materialList.get(maxIndex));
                }
        }
        int check = 0;
        for(TextView recommend : recommends){
            if(recommend.getText().equals("")) check += 1;
        }
        if(check == 3) recommend2.setText("ไม่มีวัตถุดิบอาหารที่แนะนำ");
        setAdapter(materialList);
        progress.cancel();
        okButton.setOnClickListener(view -> {
            ArrayList<History> hisOwn = new ArrayList<>();
            for(History history : histories){
                if(history.getUsername().equals(member.getUsername())){
                    hisOwn.add(history);
                }
            }
            mRootRef.child("Choose").child(member.getUsername()).child("not_"+(hisOwn.size()+1)).setValue(choose);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new FoodRecommendFragment().newInstance(member, choose))
                    .commit();
        });
        cancelButton.setOnClickListener(view -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new RecommendMenuFragment().newInstance(member))
                    .commit();
        });
        return rootView;
    }

    public void setAdapter(List<String> materials){
        materialAdapter = new MaterialAdapter(getActivity(), RecommendConFragment.this);
        recyclerView.setAdapter(materialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        materialAdapter.setMaterials(materials);
    }

    @Override
    public void onItemTouched(String material, int position) {
        choose.put("not_"+position, material);
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new RecommendConFragment().newInstance(member, cosim, materialList, choose, position, histories))
                .commit();
    }
}
