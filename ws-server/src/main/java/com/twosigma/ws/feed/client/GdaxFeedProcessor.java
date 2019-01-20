package com.twosigma.ws.feed.client;

import com.google.common.base.Enums;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twosigma.ws.model.MessageType;
import com.twosigma.ws.model.Side;
import com.twosigma.ws.model.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import static com.twosigma.ws.model.MessageType.ignore;

@Component
@ClientEndpoint
public class GdaxFeedProcessor implements FeedProcessor {
    private static final Logger logger = LoggerFactory.getLogger(GdaxFeedProcessor.class);

    private final FeedListener listener;
    private final Gson gson;
    private final JsonParser jsonParser;
    private Session session;

    public GdaxFeedProcessor(final FeedListener listener) {
        this.listener = listener;
        this.gson = new Gson();
        this.jsonParser = new JsonParser();
    }

    @OnOpen
    public void onOpen(final Session session, final EndpointConfig config) {
        this.session = session;
        logger.info("Session opened {}", session, config);
    }

    @Override
    public void subscribe(final Subscribe subscribe) {
        sendMessage(gson.toJson(subscribe));
    }

    @OnClose
    public void onClose(final Session session, final CloseReason closeReason) {
        logger.info("Endpoint was closed {} {}", session, closeReason);
    }

    @OnMessage
    public void onMessage(final String message) {
        final JsonElement jsonElement = jsonParser.parse(message);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final String type = jsonObject.get("type").getAsString();
        final MessageType messageType = Enums.getIfPresent(MessageType.class, type).or(ignore);

        if (messageType != ignore) {
            final String productId = jsonObject.get("product_id").getAsString();
            switch (messageType) {
                case snapshot:
                    processSnapshot(productId, jsonObject);
                    break;
                case l2update:
                    processUpdate(productId, jsonObject);
                    break;
                case match:
                case last_match:
                    processTrade(productId, jsonObject);
                    break;
            }
        }
    }

    private void processTrade(final String productId, final JsonObject jsonObject) {
        final Side side = Side.valueOf(jsonObject.get("side").getAsString());
        final double price = jsonObject.get("price").getAsDouble();
        final double size = jsonObject.get("size").getAsDouble();

        listener.trade(productId, side, price, size);
    }

    private void processSnapshot(final String productId, final JsonObject jsonObject) {
        listener.start(productId);
        processSide(productId, jsonObject, Side.buy);
        processSide(productId, jsonObject, Side.sell);
    }

    private void processUpdate(final String productId, final JsonObject jsonObject) {
        final JsonArray changes = jsonObject.getAsJsonArray("changes");
        changes.forEach(changeElement -> {
            final JsonArray change = changeElement.getAsJsonArray();
            final Side side = Side.valueOf(change.get(0).getAsString());
            final double price = change.get(1).getAsDouble();
            final double quantity = change.get(2).getAsDouble();

            listener.quote(productId, side, price, quantity);
        });
    }

    private void processSide(final String productId, final JsonObject jsonObject, final Side side) {
        final JsonArray quotes = jsonObject.getAsJsonArray(side == Side.buy ? "bids" : "asks");
        quotes.forEach(quoteElement -> {
            final JsonArray quote = quoteElement.getAsJsonArray();

            final double price = quote.get(0).getAsDouble();
            final double quantity = quote.get(1).getAsDouble();

            listener.quote(productId, side, price, quantity);
        });
    }

    private void sendMessage(final String message) {
        logger.info("Sending message {}", message);
        session.getAsyncRemote().sendText(message);
    }
}
