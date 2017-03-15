package www.weride.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapzen.android.graphics.MapzenMapPeliasLocationProvider;
import com.mapzen.android.search.MapzenSearch;
import com.mapzen.pelias.Pelias;
import com.mapzen.pelias.widget.AutoCompleteAdapter;
import com.mapzen.pelias.widget.AutoCompleteListView;
import com.mapzen.pelias.widget.PeliasSearchView;
import com.mapzen.tangram.LngLat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.weride.com.MainActivity;
import www.weride.com.R;
import com.mapzen.pelias.gson.Result;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements retrofit2.Callback<Result> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PeliasSearchView searchView;
    MainActivity mainActivity;
    Toolbar mainToolbar;
    ActionBar.LayoutParams layoutParams;
    MapzenSearch mapzenSearch;
    Pelias pel;
    com.mapzen.pelias.gson.Result res;
    MapzenMapPeliasLocationProvider lp;

    FragmentManager fragmentManager;
    LngLat point;
    double lngLat;
    double latLng;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) this.getActivity();
        searchView = new PeliasSearchView(this.getContext());
        mainToolbar = mainActivity.getToolbar();
        lp = new MapzenMapPeliasLocationProvider(mainActivity);
        mapzenSearch = new MapzenSearch(mainActivity);
        mapzenSearch.setLocationProvider(lp);
        pel = mapzenSearch.getPelias();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        searchView.setId(R.id.search_toolbar);
        mainActivity.findViewById(R.id.search).setVisibility(View.GONE);
        mainToolbar.addView(searchView, layoutParams);
        AutoCompleteListView  listView = (AutoCompleteListView)
                view.findViewById(R.id.list_view);
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(mainActivity,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        searchView.setPelias(pel);
        searchView.setAutoCompleteListView(listView);
        searchView.setCallback(this);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        fragmentManager = getFragmentManager();
        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {
        if(!(response.body() == null)){

           List<Double> coordinates = response.body().getFeatures().get(0).geometry.coordinates;
            latLng = coordinates.get(0);
            lngLat = coordinates.get(1);
            point = new LngLat(latLng, lngLat);
            mListener.passPoint(point);
        }

    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        //response failed. no location.
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
        void passPoint(LngLat dest);
    }





}