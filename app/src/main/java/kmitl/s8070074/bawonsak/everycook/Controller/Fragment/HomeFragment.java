package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
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
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private Member member;
    private DatabaseReference mRootRef;
    private List<Food> foods;
    private ArrayList<String> materialList;
    @BindView(R.id.name)
    TextView name;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        query();
        return rootView;
    }

    public void query(){
        mRootRef.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> foodList = (Map<String, Object>) dataSnapshot.getValue();

                for(String key : foodList.keySet()){
                    Map<String, Object> m = (Map<String, Object>) foodList.get(key);
                    /*Map<String, Object> materails = (Map<String, Object>) m.get("materails");
                    ArrayList<String> materailsList = new ArrayList<>();
                    for(String mate : materails.keySet()){
                        materailsList.add(mate);
                    }*/


                    Food f = new Food(key, m.get("method").toString(), "-", 0, null, (Map<String, String>) m.get("materials"));
                    foods.add(f);
                }
                //updateMaterial(getMaterialList());
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
        name.setText(String.join(" ", materialList));
        return materialList;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
