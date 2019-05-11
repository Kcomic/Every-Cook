package kmitl.s8070074.bawonsak.everycook.Adapter.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.R;

public class FoodHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.profile_imageIts) ImageView profileImage;
    public @BindView(R.id.member_name) TextView memberName;
    public @BindView(R.id.foodImage) ImageView foodImage;
    public @BindView(R.id.foodName) TextView foodName;
    public @BindView(R.id.view) TextView view;
    public @BindView(R.id.rating) TextView rating;
    public @BindView(R.id.relative_user) RelativeLayout relativeUser;

    public FoodHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
