package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.adapter.ComAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Comic;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Fav;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Price;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.ResponseComic;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.GsonGetRequest;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.VolleySingleton;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComicFragment extends Fragment implements ComAdapter.IcomAdapter {

    private static final String ARG_PARAM = "param";
    public List<Comic> comics = new ArrayList<>();
    public Realm realm;
    ComAdapter adapter;
    Comic comic = null;
    TextView status;
    Fav favsave = new Fav();
    Bitmap bitmap = null;
    byte[] gambar = new byte[102400];
    private int sort = 0;

    public ComicFragment() {
        // Required empty public constructor
    }

    public static ComicFragment newInstance(int sort) {
        ComicFragment fragment = new ComicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, sort);
        fragment.setArguments(args);
        return fragment;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.sort = getArguments().getInt(ARG_PARAM);
        }
        realm = Realm.getDefaultInstance();
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
        fillData(this.sort);

        if (comics.isEmpty()) {
            status.setVisibility(View.VISIBLE);
        } else {
            status.setVisibility(View.GONE);
        }

        return view;
    }

    private void fillData(int sort) {
        String url = "https://gateway.marvel.com:443/v1/public/comics?apikey=f4dbb78409bc6ed6f31319830b30a4d5&ts=2&hash=441128b0d6f3fcfcd031f6895bb0723b";
        if (sort != 0) {
            url = "https://gateway.marvel.com:443/v1/public/characters/" + sort + "/comics?apikey=f4dbb78409bc6ed6f31319830b30a4d5&ts=2&hash=441128b0d6f3fcfcd031f6895bb0723b";
        }
        GsonGetRequest<ResponseComic> request = new GsonGetRequest<>(url, ResponseComic.class, null, new Response.Listener<ResponseComic>() {
            @Override
            public void onResponse(ResponseComic response) {
                if (response.status.equals("Ok")) {
                    comics.addAll(response.data.results);
                }
                if (comics.isEmpty()) {
                    status.setVisibility(View.VISIBLE);
                } else {
                    status.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ComicFragment", "Error : ", error);
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("error", "Timeout");
                        startActivity(intent);
                    }
                }
            }
        });
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void detailCom(int pos) {
        Comic comic = comics.get(pos);
        Intent intent = new Intent(getActivity(), ComsDetActivity.class);
        intent.putExtra("comdetail", comic);
        startActivity(intent);
    }

    @Override
    public void doSave(int pos, ImageButton fab) {
        comic = comics.get(pos);
        Fav fav = realm.where(Fav.class).equalTo("id", comic.id).findFirst();
        if (fav == null) {
            try {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            bitmap = Glide
                                    .with(getActivity().getApplicationContext())
                                    .load(comic.thumbnail.path + "/landscape_xlarge.jpg")
                                    .asBitmap()
                                    .into(270, 200)
                                    .get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        gambar = getBytes(bitmap);
                        Price price;
                        if (comic.prices.size() > 1) {
                            price = comic.prices.get(1);
                        } else {
                            price = comic.prices.get(0);
                        }
                        favsave = new Fav(comic.id, comic.title, comic.description, gambar, price.price);
                        realm.beginTransaction();
                        realm.insert(favsave);
                        realm.commitTransaction();
                    }
                }.execute();
                Toast.makeText(getActivity(), comic.title + " berhasil masuk Favorite", Toast.LENGTH_LONG).show();
                fab.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } catch (Exception e) {
                Log.e("ComicFragment", "Error : ", e);
            }
        } else {
            Toast.makeText(getActivity(), "Sudah ada di Favorite", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }
}
