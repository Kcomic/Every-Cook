package kmitl.s8070074.bawonsak.everycook.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import kmitl.s8070074.bawonsak.everycook.Adapter.Holder.MaterialHolder;
import kmitl.s8070074.bawonsak.everycook.R;

public class MaterialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> materials;
    private MaterialAdapterListener listener;
    public MaterialAdapter(Context mContext, MaterialAdapterListener listener){
        this.mContext = mContext;
        this.listener = listener;
        materials = new ArrayList<>();
    }
    public interface MaterialAdapterListener {

        public void onItemTouched(String material, int position);
    }
    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_material, parent, false);
        MaterialHolder materialHolder = new MaterialHolder(view);

        return materialHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MaterialHolder)holder).itemView.setOnClickListener(v -> listener.onItemTouched(materials.get(position), position));
        ((MaterialHolder)holder).view.setText(materials.get(position));
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }
}
