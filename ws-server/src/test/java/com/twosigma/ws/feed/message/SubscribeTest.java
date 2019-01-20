package com.twosigma.ws.feed.message;

import com.google.gson.Gson;
import com.twosigma.ws.model.Channel;
import com.twosigma.ws.model.Subscribe;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribeTest {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeTest.class);

    @Test
    public void toJson() {
        final Subscribe subscribe = new Subscribe();
        subscribe.addProduct("ETH-USD");
        subscribe.addChannel(Channel.heartbeat);

        final Gson gson = new Gson();
        final String json = gson.toJson(subscribe);

        logger.info("Got {}", json);
    }
}