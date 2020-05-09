package kmitl.s8070074.bawonsak.everycook.Adapter.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.s8070074.bawonsak.everycook.R;

public class MaterialHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.materialIts)
    TextView view;

    public MaterialHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
