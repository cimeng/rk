package id.co.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aldi on 5/18/2017.
 */


public class menuClass {
    int id;
    String nama;
    String subs;
    double lat;
    double lng;
    Boolean status;

    menuClass(int id, String a, String b, Boolean c, double lat, double lng){
        this.id = id;
        this.nama = a;
        this.subs = b;
        this.lat = lat;
        this.lng = lng;
        this.status = c;
    }

    static ArrayList<menuClass> addAll(List<Integer> id, List<String> name, String[] sub, List<Double> lat, List<Double> lng){
        ArrayList<menuClass> hasil = new ArrayList<>();
        for (int i=0; i<name.size(); i++){
            hasil.add(new menuClass(
                    id.get(i),
                    name.get(i),
                    sub[i],
                    false, lat.get(i), lng.get(i)));
        }
        return hasil;
    }
}
