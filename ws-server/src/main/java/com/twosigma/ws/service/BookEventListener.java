package com.twosigma.ws.service;

import com.twosigma.ws.model.Side;

public interface BookEventListener {
    void start(final String productId);

    void update(final String productId, Side side, double[][] depth);

    void trade(final String productId, final Side side, final double price, final double quantity);

    void tradeHistory(final String productId, final Object[][] tradeHistory);
}
