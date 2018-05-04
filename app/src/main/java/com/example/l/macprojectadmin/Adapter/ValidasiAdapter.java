package com.example.l.macprojectadmin.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.l.macprojectadmin.Entity.ValidasiEntity;
import com.example.l.macprojectadmin.Interface.AdapterInterface;
import com.example.l.macprojectadmin.R;

import java.util.List;

/**
 * Created by L on 14/12/17.
 */

public class ValidasiAdapter extends BaseAdapter {

    private List<?> objects;
    private Context context;
    private LayoutInflater inflater;
    private AdapterInterface adapterInterface;
    private int layout;

    public ValidasiAdapter(Context context, List<?> entityList){
        this.objects           = entityList;
        this.context          = context;
        inflater = LayoutInflater.from(context);
    }
    public void setAdapterInterface(AdapterInterface adapterInterface,int layout){
        this.adapterInterface = adapterInterface;
        this.layout = layout;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("panggil","dukali");
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(layout,viewGroup,false);
        adapterInterface.setView(view,i);
        return view;
    }
}
