package kmitl.s8070074.bawonsak.everycook.Controller.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhaellopez.circularimageview.CircularImageView;

import kmitl.s8070074.bawonsak.everycook.Controller.Activity.LoginActivity;
import kmitl.s8070074.bawonsak.everycook.Model.Member;
import kmitl.s8070074.bawonsak.everycook.R;
import kmitl.s8070074.bawonsak.everycook.UserPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Member member;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(Member member) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("member", member);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member = getArguments().getParcelable("member");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        logout();
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void logout(){
        UserPreferences.clearUserName(getActivity());
    }
}
