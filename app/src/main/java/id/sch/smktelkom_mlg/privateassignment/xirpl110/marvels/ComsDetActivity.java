package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.adapter.CharAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Char;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Comic;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Price;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Response;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.GsonGetRequest;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.service.VolleySingleton;

public class ComsDetActivity extends AppCompatActivity implements CharAdapter.charListener {

    public CharAdapter adapter;
    public List<Char> aChar = new ArrayList<>();
    public int ID = 0;
    TextView txtNama, txtPrice;
    ImageView ivCom;
    Comic comic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms_det);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        comic = (Comic) getIntent().getSerializableExtra("comdetail");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < comic.urls.size(); i++) {
                    if (comic.urls.get(i).type.equals("detail")) {
                        Uri webpage = Uri.parse(comic.urls.get(i).url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivity(intent);
                    }
                }
            }
        });
        Price price = null;
        if (comic.prices.size() > 1) {
            price = comic.prices.get(1);
        } else {
            price = comic.prices.get(0);
        }
        setTitle(comic.title);
        String deskripsi = comic.description;
        txtNama = (TextView) findViewById(R.id.deskripsiCoy);
        txtPrice = (TextView) findViewById(R.id.detPrice);
        ivCom = (ImageView) findViewById(R.id.imageFoto);
        txtNama.setText(deskripsi);
        txtPrice.setText("Price : $" + price.price);
        Glide.with(this)
                .load(comic.thumbnail.path + "/landscape_xlarge.jpg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_image_landscape)
                .into(ivCom);
        ID = comic.id;
        adapter = new CharAdapter(aChar, this, null);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.comschar);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fillData(ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fillData(int id) {
        String url = "https://gateway.marvel.com:443/v1/public/comics/" + id + "/characters?apikey=f4dbb78409bc6ed6f31319830b30a4d5&ts=2&hash=441128b0d6f3fcfcd031f6895bb0723b";
        GsonGetRequest<Response> req = new GsonGetRequest<>(url, Response.class, null, new com.android.volley.Response.Listener<Response>() {
            @Override
            public void onResponse(Response response) {
                if (response.status.equals("Ok")) {
                    aChar.addAll(response.data.results);
                }
                adapter.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

    @Override
    public void detail(int pos) {
        Char chari = aChar.get(pos);
        Intent intent = new Intent(this, CharDetailActivity.class);
        intent.putExtra("detail", chari);
        startActivity(intent);
    }
}

