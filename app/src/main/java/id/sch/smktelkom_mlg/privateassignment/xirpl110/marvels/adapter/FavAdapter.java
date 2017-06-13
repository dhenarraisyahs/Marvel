package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.R;
import id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model.Fav;

/**
 * Created by dhenarra on 13/06/2017.
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    public List<Fav> favs;
    Context context;
    IFavAdapter iFavAdapter;

    public FavAdapter(List<Fav> favs, Context context, Fragment fragment) {
        this.favs = favs;
        this.context = context;
        iFavAdapter = (IFavAdapter) fragment;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favlay, parent, false);
        ViewHolder vh = new FavAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fav fav = favs.get(position);
        holder.txtNama.setText(fav.title);
        holder.txtDeskripsi.setText(fav.description);
        holder.txtPrice.setText("$" + fav.price);
        Bitmap bitmap = getImage(fav.picture);
        holder.ivFav.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        if (favs != null) {
            return favs.size();
        }
        return 0;
    }

    public interface IFavAdapter {
        void detFav(int pos);

        void delete(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNama, txtDeskripsi, txtPrice;
        ImageView ivFav;
        Button butdel;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.textViewJudulFav);
            txtDeskripsi = (TextView) itemView.findViewById(R.id.textViewDeskripsiFav);
            ivFav = (ImageView) itemView.findViewById(R.id.imageViewComicFav);
            txtPrice = (TextView) itemView.findViewById(R.id.textViewPriceFav);
            butdel = (Button) itemView.findViewById(R.id.buttonDelete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iFavAdapter.detFav(getAdapterPosition());
                }
            });
            butdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iFavAdapter.delete(getAdapterPosition());
                }
            });
        }
    }
}
