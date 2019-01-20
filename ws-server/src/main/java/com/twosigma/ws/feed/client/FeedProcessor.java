package com.twosigma.ws.feed.client;

import com.twosigma.ws.model.Subscribe;

public interface FeedProcessor {
    void subscribe(final Subscribe subscribe);
}
