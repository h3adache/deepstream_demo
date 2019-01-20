package com.twosigma.ws.service;

import com.google.common.collect.Sets;
import com.twosigma.ws.feed.FeedService;
import com.twosigma.ws.model.Side;
import io.deepstream.DeepstreamClient;
import io.deepstream.ListenListener;
import io.deepstream.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Set;

@Service
public class MarketEventsService implements BookEventListener {
    private static final Logger logger = LoggerFactory.getLogger(MarketEventsService.class);
    private final DeepstreamClient client;
    private final Set<String> subscriptions;
    private final double largeTrade;

    public MarketEventsService(final FeedService feedService,
                               final BookService bookService,
                               @Value("${feeds.broadcast}") final String broadcast,
                               @Value("${news.large_trade}") final double largeTrade) throws URISyntaxException {
        this.subscriptions = Sets.newHashSet();
        this.client = new DeepstreamClient(broadcast);
        this.largeTrade = largeTrade;
        client.login();
        logger.info("started websocket service");

        bookService.listener(this);
        client.record.listen("/*", new ListenListener() {
            @Override
            public boolean onSubscriptionForPatternAdded(final String productId) {
                logger.info("Incoming subscription for {}", productId);
                subscriptions.add(productId);
                feedService.subscribe(productId);
                return true;
            }

            @Override
            public void onSubscriptionForPatternRemoved(final String productId) {
                logger.info("Removed subscription for {}", productId);
                subscriptions.remove(productId);
            }
        });
    }

    @Override
    public void start(final String productId) {
        logger.info("Book started {}", productId);
        final Record record = client.record.getRecord(productId);
        record.set("product_id", productId);
    }

    @Override
    public void update(final String productId, final Side side, final double[][] depth) {
        if(subscriptions.contains(productId)) {
            final Record record = client.record.getRecord(productId);
            record.set(side.name(), depth);
        }
    }

    @Override
    public void trade(final String productId, final Side side, final double price, final double quantity) {
        if(subscriptions.contains(productId)) {
            client.event.emit("trade", new Object[]{productId, side, price, quantity});
        }

        final double tradedAmount = price * quantity;
        if(tradedAmount > largeTrade) {
            notableEvent(String.format("%s large trade of $%f", productId, tradedAmount));
        }
    }

    @Override
    public void tradeHistory(final String productId, final Object[][] tradeHistory) {
        if(subscriptions.contains(productId)) {
            final Record record = client.record.getRecord(productId);
            record.set("trade_history", tradeHistory);
        }
    }

    private void notableEvent(final String event) {
        client.event.emit("news", event);
    }
}
