package kmitl.s8070074.bawonsak.everycook.Adapter.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.R;

public class CommentHolder  extends RecyclerView.ViewHolder {

    public @BindView(R.id.profile_imageIts_comment) ImageView profileImage;
    public @BindView(R.id.nameIts_comment) TextView name;
    public @BindView(R.id.comment) TextView message;

    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
