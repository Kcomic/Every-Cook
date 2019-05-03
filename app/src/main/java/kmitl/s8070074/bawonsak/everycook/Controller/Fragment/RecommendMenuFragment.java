package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.Model.History;
import kmitl.s8070074.bawonsak.everycook.R;


public class RecommendMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mRootRef;
    private ArrayList<History> histories;
    private OnFragmentInteractionListener mListener;
    @BindView(R.id.name)
    TextView name;
    public RecommendMenuFragment() {
        // Required empty public constructor
    }

    public static RecommendMenuFragment newInstance(String param1, String param2) {
        RecommendMenuFragment fragment = new RecommendMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mRootRef = FirebaseDatabase.getInstance().getReference();
        histories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        query();
        return rootView;
    }

    public void query(){
        mRootRef.child("Choose").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> chooseList = (Map<String, Object>) dataSnapshot.getValue();

                for(String username : chooseList.keySet()){
                    Map<String, Object> his = (Map<String, Object>) chooseList.get(username);
                    for(String times : his.keySet()) {
                        ArrayList<String> materailsList = (ArrayList<String>)his.get(times);
                        /*Map<String, Object> mat = (Map<String, Object>) his.get(times);
                        for(String mate : mat.keySet()){
                            materailsList.add(mat.get(mate).toString());
                        }*/
                        History h = new History(username, Integer.valueOf(times.replaceAll("\\D+","")), materailsList);
                        histories.add(h);
                    }
                }
                String test = "";
                for(History h : histories){
                    for(String s : h.getChoose()){
                        test += s;
                    }
                }
                name.setText(test);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
