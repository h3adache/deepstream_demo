package com.twosigma.ws.feed;

import com.google.common.collect.Sets;
import com.twosigma.ws.feed.client.FeedProcessor;
import com.twosigma.ws.feed.client.GdaxFeedProcessor;
import com.twosigma.ws.model.Channel;
import com.twosigma.ws.model.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

@Service
@ClientEndpoint
public class GdaxFeedService implements FeedService {
    private static final Logger logger = LoggerFactory.getLogger(GdaxFeedService.class);
    private final String feedUrl;
    private final FeedProcessor feedProcessor;
    private final Set<String> subscriptions;
    private boolean started;

    @Autowired
    public GdaxFeedService(@Value("${feeds.gdax}") final String feedUrl, final GdaxFeedProcessor feedProcessor) {
        this.feedUrl = feedUrl;
        this.feedProcessor = feedProcessor;
        this.subscriptions = Sets.newHashSet();
    }

    @Override
    public void subscribe(final String productId) {
        try {
            if (!started) startEngine();

            if (subscriptions.add(productId)) {
                final Subscribe subscribe = new Subscribe();
                subscribe.addProduct(productId);
                subscribe.addChannel(Channel.matches);
                subscribe.addChannel(Channel.level2);

                feedProcessor.subscribe(subscribe);
            }
        } catch (IOException | DeploymentException e) {
            logger.error("Feed Engine failed to start", e);
        }
    }

    @PostConstruct
    private void startEngine() throws IOException, DeploymentException {
        logger.info("Starting engine to {}", feedUrl);
        final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        webSocketContainer.setDefaultMaxTextMessageBufferSize(1024 * 1024);
        webSocketContainer.connectToServer(feedProcessor, URI.create(feedUrl));
        logger.info("Connected to feed");
        started = true;
    }
}
