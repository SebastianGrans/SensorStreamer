package no.grans.sensorstreamer.fragments;

import android.content.Intent;

import no.grans.sensorstreamer.R;
import no.grans.sensorstreamer.activities.EditConnectionActivity;
import no.grans.sensorstreamer.models.Connection;

public class ConnectionsFragment extends ItemsOverviewFragment<Connection> {

    public ConnectionsFragment() {
        super(Connection.class, R.string.no_connections);
    }

    @Override
    public void editItem(int position) {
        Intent intent = new Intent(getActivity(), EditConnectionActivity.class);
        intent.putExtra(EditConnectionActivity.EXTRA_CONNECTION, position);
        startActivity(intent);
    }

    @Override
    public void createNewItem() {
        Intent intent = new Intent(getActivity(), EditConnectionActivity.class);
        startActivity(intent);
    }
}
