package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Char;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txtNama;
    TextView txtDeskripsi;
    ImageView gambarChar;
    Context context;
    // TODO: Rename and change types of parameters
    private Char chars;

    public DetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Char chars) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, chars);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.chars = (Char) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        txtNama = (TextView) view.findViewById(R.id.Nama);
        txtDeskripsi = (TextView) view.findViewById(R.id.Deskripsi);
        gambarChar = (ImageView) view.findViewById(R.id.imageCharDetail);
        txtNama.setText(chars.name);
        txtDeskripsi.setText(chars.description);
        Glide.with(getActivity())
                .load(chars.thumbnail.path + "/portrait_xlarge.jpg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.no_image)
                .into(gambarChar);
        return view;
    }

}