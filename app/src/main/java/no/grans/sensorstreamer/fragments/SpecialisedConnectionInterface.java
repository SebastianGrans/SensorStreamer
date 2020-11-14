package no.grans.sensorstreamer.fragments;

import no.grans.sensorstreamer.models.Connection;

public interface SpecialisedConnectionInterface {
    boolean isValid();
    void commit(Connection connection);
    interface OnFragmentInteractionListener {
        void onValidation(boolean valid);
        Connection onDataLoad();
    }
}
