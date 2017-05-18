package id.co.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ListView Listmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Listmenu = (ListView) findViewById(R.id.list_menu);
        String[] name= getResources().getStringArray(R.array.nama_toko);
        String[] sub = getResources().getStringArray(R.array.subs);
        menuAdapter adapter = new menuAdapter(MainActivity.this, menuClass.addAll(name, sub));
        Listmenu.setAdapter(adapter);
    }

    private class menuAdapter extends ArrayAdapter<menuClass> {
        ArrayList<menuClass> menuArray;
        menuAdapter(Context context, ArrayList<menuClass> list) {
            super(context, 0, list);
            menuArray = list;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            menuClass menu = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_menu, parent, false);
            }
            // Lookup view for data population
            TextView nama = (TextView) convertView.findViewById(R.id.item_menu_nama);
            TextView subs = (TextView) convertView.findViewById(R.id.item_menu_subs);
            // Populate the data into the template view using the data object
            if (menu != null) {
                nama.setText(menu.nama);
                subs.setText(menu.subs);
                // Return the completed view to render on screen
            }
            return convertView;
        }
    }
}
