package id.sch.smktelkom_mlg.privateassignment.xirpl110.marvels.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dhenarra on 14/05/2017.
 */

public class Comic implements Serializable {
    public int id;
    public String title;
    public String description;
    public Thumbnail thumbnail;
    public List<Price> prices;
    public List<Url> urls;
}
