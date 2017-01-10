package com.example.rxsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.list);
        ArrayList<String> items = new ArrayList<>();
        final ArrayAdapter<String> itemsS = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsS);
        clicks(findViewById(R.id.tvClick))
                .buffer(400, 400, TimeUnit.MILLISECONDS)
                .filter(new Predicate<List<Object>>() {
                    @Override
                    public boolean test(List<Object> objects) throws Exception {
                        return objects.size() > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Object>>() {
                    @Override
                    public void accept(List<Object> objects) throws Exception {
                        itemsS.add(objects.size() + "");
                        itemsS.notifyDataSetChanged();
                        listView.setSelection(itemsS.getCount() - 1);
                    }
                });


    }


    Observable<Object> clicks(View view) {
        return new ViewClickObservable(view);
    }
}
