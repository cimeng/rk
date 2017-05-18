package id.co.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aldi on 5/18/2017.
 */


public class menuClass {
    String nama;
    String subs;
    Boolean status;

    menuClass(String a, String b, Boolean c){
        nama = a;
        subs = b;
        status = c;
    }

    static ArrayList<menuClass> addAll(String[] name, String[] sub){
        ArrayList<menuClass> hasil = new ArrayList<>();
        for (int i=0; i<name.length; i++){
            hasil.add(new menuClass(
                    name[i],
                    sub[i],
                    false));
        }
        return hasil;
    }
}
