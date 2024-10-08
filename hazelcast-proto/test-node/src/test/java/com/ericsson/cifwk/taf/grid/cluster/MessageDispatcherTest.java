package com.ericsson.cifwk.taf.grid.cluster;

import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

/**
 *
 */

public class MessageDispatcherTest implements RequestHandler {

    Channel channel;

    MessageDispatcher disp;
    RspList rsp_list;
    String props; // to be set by application programmer


    public void start() throws Exception {
        channel = new JChannel(props);
        disp = new MessageDispatcher(channel, null, null, this);
        channel.connect("MessageDispatcherTestGroup");

        for (int i = 0; i < 10; i++) {
            Util.sleep(100);
            System.out.println("Casting message #" + i);
            rsp_list = disp.castMessage(null,
                    new Message(null, null, "Number #" + i),
                    RequestOptions.SYNC());
            System.out.println("Responses:\n" + rsp_list);
        }
        channel.close();
        disp.stop();
    }

    public Object handle(Message msg) throws Exception {
        System.out.println("handle(): " + msg);
        return "Success !";
    }

    public static void main(String[] args) {
        try {
            new MessageDispatcherTest().start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
