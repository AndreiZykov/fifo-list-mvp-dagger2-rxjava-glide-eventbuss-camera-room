package com.zykov.andrii.fifolist.fragment.reservationlist.view;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zykov.andrii.fifolist.R;
import com.zykov.andrii.fifolist.db.entity.ReservationEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ReservationListItemViewHolder> {

    public final List<ReservationEntity> data = new ArrayList<>();

    private final ReservationListAdapterListener listener;

    public ReservationListAdapter(ReservationListAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_list_item_layout, parent, false);
        return new ReservationListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationListItemViewHolder holder, int position) {
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<ReservationEntity> getData() {
        return data;
    }

    class ReservationListItemViewHolder extends RecyclerView.ViewHolder {

        private long id;

        @BindView(R.id.reservation_list_card_profile_full_name_text_view)
        TextView fullNameTextView;

        @BindView(R.id.reservation_list_card_profile_phone_number_text_view)
        TextView phoneNumberTextView;

        @BindView(R.id.reservation_list_card_profile_picture_image_view)
        ImageView profilePictureImageView;

        ReservationListItemViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }

        @OnClick(R.id.reservation_list_card)
        void onCardClicked() {
            listener.onReservationItemSelected(id);
        }

        void bindView(ReservationEntity entity) {
            Context context = itemView.getContext();
            this.id = entity.getId();
            fullNameTextView.setText(String.format(Locale.US, "%s: %s",
                    context.getString(R.string.reservation_list_adapter_name), entity.getFullName()));
            phoneNumberTextView.setText(String.format(Locale.US, "%s: %s",
                    context.getString(R.string.reservation_list_adapter_tel), entity.getPhoneNumber()));
            if (entity.getImageUri() != null && !entity.getImageUri().isEmpty()) {
                File file = new File(entity.getImageUri());
                Uri uri = Uri.fromFile(file);
                if (uri != null) {
                    Glide.with(context)
                            .setDefaultRequestOptions(new RequestOptions().override(60, 92))
                            .load(uri)
                            .into(profilePictureImageView);
                }
            } else {
                profilePictureImageView.setImageResource(R.drawable.ic_picture_placeholder);
            }
        }

    }

    public interface ReservationListAdapterListener {
        void onReservationItemSelected(long id);
    }

}
