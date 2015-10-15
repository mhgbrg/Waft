package com.alive_n_clickin.commutity.presentation.flagreport;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alive_n_clickin.commutity.R;

import java.util.List;

/**
 * This class handles creation of each Flag Type button and puts them in the RecyclerView element.
 */
public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<FlagButton> flagButtonList;

    public RecyclerAdapter(List<FlagButton> flagButtonList) {
        this.flagButtonList = flagButtonList;
    }

    /**
     * This inner-class handles binding the elements together within the RecyclerAdapter.
     * It sets the information for each FlagType card.
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView flagInfo;
        public ImageView flagImg;
        public ViewHolder(View v) {
            super(v);
            flagInfo = (TextView) v.findViewById(R.id.textView);
            flagImg = (ImageView) v.findViewById(R.id.imageView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flag_type_card,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItemPosition = holder.getLayoutPosition();
                FlagButton currentButton = flagButtonList.get(currentItemPosition);

                //Prepare arguments
                FlagVehicleDetailFragment detailFragment = new FlagVehicleDetailFragment();
                Bundle args = new Bundle();
                args.putInt(FlagVehicleDetailFragment.getARG_POSITION(), currentItemPosition);

                //Add flag data
                args.putInt("flag_image_ID", currentButton.getImageID());
                args.putString("flag_description", currentButton.getDescription());
                args.putInt("flag_type_ID", currentButton.getType().getId());
                detailFragment.setArguments(args);

                //Make the switch
                FragmentActivity fragmentActivity = (FragmentActivity) parent.getContext();
                FragmentTransaction fragmentTransaction = fragmentActivity
                        .getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, detailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FlagButton button = this.flagButtonList.get(position);
        Context context = holder.flagImg.getContext();

        holder.flagImg.setImageDrawable(context.getResources().getDrawable(button.getImageID()));
        holder.flagInfo.setText(button.getDescription());
    }

    @Override
    public int getItemCount() {
        return flagButtonList.size();
    }

}
