package Utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualassistant.R;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private ArrayList<CardViewItem> mCardViewItemArrayList;
    private OnitemClickListener mListener;

    public interface OnitemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnitemClickListener listener) {
        mListener = listener;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mCardViewEditIcon;
        public ImageView mCardViewDeleteIcon;


        public CardViewHolder(View cardViewItem, OnitemClickListener listener) {
            super(cardViewItem);
            mImageView = cardViewItem.findViewById(R.id.cardViewImageView);
            mTextView1 = cardViewItem.findViewById(R.id.cardLine1TextView);
            mTextView2 = cardViewItem.findViewById(R.id.cardLine2TextView);
            mCardViewEditIcon = cardViewItem.findViewById(R.id.cardViewEditIcon);
            mCardViewDeleteIcon = cardViewItem.findViewById(R.id.cardViewDeleteIcon);

            cardViewItem.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            mCardViewEditIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(position);
                    }
                }
            });

            mCardViewDeleteIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

        }
    }

    public CardViewAdapter(ArrayList<CardViewItem> cardViewItemArrayList) {
        mCardViewItemArrayList = cardViewItemArrayList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new CardViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardViewItem currentItem = mCardViewItemArrayList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mCardViewItemArrayList.size();
    }

    public void filterList(ArrayList<CardViewItem> filteredList) {
        mCardViewItemArrayList = filteredList;
        notifyDataSetChanged();
    }
}
