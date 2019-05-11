package kmitl.s8070074.bawonsak.everycook.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import kmitl.s8070074.bawonsak.everycook.Adapter.Holder.FoodHolder;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.ProfileSomeone;
import kmitl.s8070074.bawonsak.everycook.Model.Food;
import kmitl.s8070074.bawonsak.everycook.R;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Food> foods;
    private FoodAdapterListener listener;
    public FoodAdapter(Context mContext, FoodAdapterListener listener){
        this.mContext = mContext;
        this.listener = listener;
        foods = new ArrayList<>();
    }
    public interface FoodAdapterListener {

        public void onItemTouched(Food food);
    }
    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_food, parent, false);
        FoodHolder foodHolder = new FoodHolder(view);

        return foodHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((FoodHolder)holder).itemView.setOnClickListener(v -> listener.onItemTouched(foods.get(position)));
        ((FoodHolder)holder).memberName.setText(foods.get(position).getMember().getFullname());
        ((FoodHolder)holder).foodName.setText(foods.get(position).getName());
        ((FoodHolder)holder).view.setText(foods.get(position).getView());
        ((FoodHolder)holder).rating.setText(foods.get(position).getRating());
        //Picasso.with(mContext).load(events.get(position).getUrl()).fit().into(((EventHolder)holder).eventImage);
        ((FoodHolder)holder).relativeUser.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProfileSomeone.class);
            intent.putExtra("member_from_food", foods.get(position).getMember());
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        Glide.with(mContext)
                .load(foods.get(position).getUrl())
                .into(((FoodHolder)holder).foodImage);
        Glide.with(mContext)
                .load(foods.get(position).getMember().getUrl())
                .into(((FoodHolder)holder).profileImage);

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
