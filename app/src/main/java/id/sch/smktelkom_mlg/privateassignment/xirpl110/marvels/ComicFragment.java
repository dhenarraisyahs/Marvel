package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.adapter.ComAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Comic;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.ResponseComic;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.GsonGetRequest;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.VolleySingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComicFragment extends Fragment {

    public List<Comic> comics = new ArrayList<>();
    ComAdapter adapter;

    public ComicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ComAdapter(comics, getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comic, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.comicView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fillData();

        return view;
    }

    private void fillData() {
        String url = "https://gateway.marvel.com:443/v1/public/comics?apikey=f4dbb78409bc6ed6f31319830b30a4d5&ts=2&hash=441128b0d6f3fcfcd031f6895bb0723b";
        GsonGetRequest<ResponseComic> request = new GsonGetRequest<>(url, ResponseComic.class, null, new Response.Listener<ResponseComic>() {
            @Override
            public void onResponse(ResponseComic response) {
                if (response.status.equals("Ok")) {
                    comics.addAll(response.data.results);
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ComicFragment", "Error : ", error);
            }
        });
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

}
