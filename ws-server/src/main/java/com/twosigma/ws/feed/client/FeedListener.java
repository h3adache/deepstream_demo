package com.twosigma.ws.feed.client;

import com.twosigma.ws.model.Side;

public interface FeedListener {
    void start(final String productId);

    void quote(final String productId, final Side side, final double price, final double quantity);

    void trade(final String productId, final Side side, final double price, final double quantity);
}
