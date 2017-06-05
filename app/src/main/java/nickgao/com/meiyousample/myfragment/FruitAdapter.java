package nickgao.com.meiyousample.myfragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/5/31.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private Context mContext;
    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView fruitView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view.findViewById(R.id.card_view);
            fruitView = (ImageView)view.findViewById(R.id.fruit_image);
            textView = (TextView)view.findViewById(R.id.fruit_name);
        }
    }


    public FruitAdapter(Context context, List<Fruit> fruitList) {
        this.mContext = context;
        mFruitList = fruitList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Fruit fruit = mFruitList.get(position);
//                Intent intent = new Intent(mContext, FruitActivity.class);
//                intent.putExtra(FruitActivity.FRUIT_NAME, fruit.getName());
//                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.getImageId());
//                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.textView.setText(fruit.name);
        Glide.with(mContext).load(fruit.imageId).into(holder.fruitView);
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
