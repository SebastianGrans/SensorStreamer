package no.grans.sensorstreamer.fragments;

import android.content.Intent;

import no.grans.sensorstreamer.R;
import no.grans.sensorstreamer.activities.EditPacketActivity;
import no.grans.sensorstreamer.models.Packet;

public class PacketsFragment extends ItemsOverviewFragment<Packet> {

    public PacketsFragment() {
        super(Packet.class, R.string.no_packets);
    }

    @Override
    public void editItem(int position) {
        Intent intent = new Intent(getActivity(), EditPacketActivity.class);
        intent.putExtra(EditPacketActivity.EXTRA_PACKET, position);
        startActivity(intent);
    }

    @Override
    public void createNewItem() {
        Intent intent = new Intent(getActivity(), EditPacketActivity.class);
        startActivity(intent);
    }
}