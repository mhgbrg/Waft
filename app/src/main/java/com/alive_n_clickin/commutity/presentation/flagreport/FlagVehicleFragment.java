package com.alive_n_clickin.commutity.presentation.flagreport;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.erik.commutity.R;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FlagVehicleFragment extends Fragment {

    private FlagViewAdapter flagAdapter;
    private ArrayList<FlagButton> flagButtons;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flagButtons = new ArrayList<FlagButton>();
        Resources resources = getResources();

        //TODO hard coded flag data for testing purposes
        flagButtons.add(new FlagButton(R.drawable.full, "Full", FlagType.FULL));
        flagButtons.add(new FlagButton(R.drawable.full, "Stökig", FlagType.ROWDY));
        flagButtons.add(new FlagButton(R.drawable.full, "Försenad", FlagType.LATE));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_flag_vehicle, container, false);
        flagAdapter = new FlagViewAdapter(getActivity(), flagButtons);

        GridView flagGrid = (GridView) rootView.findViewById(R.id.flagGridView);
        flagGrid.setAdapter(flagAdapter);
        flagGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* The user clicked on an entry */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context currentContext = getActivity().getApplicationContext();
                FlagButton button= flagAdapter.getItem(i);
                /*
                Start an intent for the detail view. The intent contains the bus line image, a
                description of the issue and the flag image
                 */
                Intent detailIntent = new Intent(currentContext, getActivity().getClass());
                //detailIntent.putExtra("image_url",button.getImage());
                detailIntent.putExtra(Intent.EXTRA_TEXT, button.getDescription());
                //startActivity(detailIntent);




                Toast.makeText(getActivity(), "" + button.getDescription(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
