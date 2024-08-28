package com.ericsson.cifwk.ve.web;

import com.ericsson.cifwk.ve.application.DashboardConnection;
import com.google.common.base.Throwables;
import org.atmosphere.socketio.SocketIOException;
import org.atmosphere.socketio.SocketIOPacket;
import org.atmosphere.socketio.SocketIOSessionOutbound;
import org.atmosphere.socketio.transport.SocketIOPacketImpl;

/**
 *
 */
public class AtmosphereConnection implements DashboardConnection {

    private SocketIOSessionOutbound session;

    public AtmosphereConnection(SocketIOSessionOutbound session) {
        this.session = session;
    }

    @Override
    public void send(String message) {
        try {
            SocketIOPacket packet = new SocketIOPacketImpl(
                    SocketIOPacketImpl.PacketType.EVENT,
                    "{\"name\":\"message\", \"args\":[" + message + "]}"
            );
            session.sendMessage(packet);
        } catch (SocketIOException e) {
            throw Throwables.propagate(e);
        }
    }

}
