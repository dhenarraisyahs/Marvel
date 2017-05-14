package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.adapter.CharAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Char;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Response;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.GsonGetRequest;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.VolleySingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharFragment extends Fragment implements CharAdapter.charListener {

    public CharAdapter charAdapter;
    public List<Char> charList = new ArrayList<>();
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;

    public CharFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        charAdapter = new CharAdapter(charList, getContext(), this);
    }

    private void fillData() {

        String url = "https://gateway.marvel.com:443/v1/public/characters?ts=1&apikey=f4dbb78409bc6ed6f31319830b30a4d5&hash=1b4a1c0351f6be2a613dd55c4246f3d9";
        GsonGetRequest<Response> req = new GsonGetRequest<Response>(url, Response.class, null, new com.android.volley.Response.Listener<Response>() {
            @Override
            public void onResponse(Response response) {
                if (response.status.equals("Ok")) {
                    charList.addAll(response.data.results);
                }
                charAdapter.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CHAR FRAGMENT", "Error : ", error);
            }
        });
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_char, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(charAdapter);
        fillData();

        return view;
    }

    @Override
    public void detail(int pos) {
        Char chari = charList.get(pos);
        Intent intent = new Intent(getActivity(), CharDetailActivity.class);
        intent.putExtra("detail", chari);
        startActivity(intent);
    }
}