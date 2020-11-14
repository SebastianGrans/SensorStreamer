package no.grans.sensorstreamer.models;


import android.content.Context;

import no.grans.sensorstreamer.R;

public class TcpServer {
    private int port;

    public String getDescription(Context context) {
        return context.getString(R.string.tcp_server_on_port) + port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
