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
import kmitl.s8070074.bawonsak.everycook.Adapter.Holder.CommentHolder;
import kmitl.s8070074.bawonsak.everycook.Controller.Activity.ProfileSomeone;
import kmitl.s8070074.bawonsak.everycook.Model.Comment;
import kmitl.s8070074.bawonsak.everycook.R;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Comment> comments;
    private CommentAdapterListener listener;
    public CommentAdapter(Context mContext, CommentAdapterListener listener){
        this.mContext = mContext;
        this.listener = listener;
        comments = new ArrayList<>();
    }
    public interface CommentAdapterListener {

        public void onItemTouched(Comment comment);
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentHolder commentHolder = new CommentHolder(view);

        return commentHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((CommentHolder)holder).itemView.setOnClickListener(v -> listener.onItemTouched(comments.get(position)));
        ((CommentHolder)holder).name.setText(comments.get(position).getMember().getFullname());
        ((CommentHolder)holder).message.setText(comments.get(position).getMessage());
        ((CommentHolder)holder).name.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProfileSomeone.class);
            intent.putExtra("member_from_comment", comments.get(position).getMember());
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        ((CommentHolder)holder).profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ProfileSomeone.class);
            intent.putExtra("member_from_comment", comments.get(position).getMember());
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        Glide.with(mContext)
                .load(comments.get(position).getMember().getUrl())
                .into(((CommentHolder)holder).profileImage);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
