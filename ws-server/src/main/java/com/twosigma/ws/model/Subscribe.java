package com.twosigma.ws.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subscribe {
    private final MessageType type = MessageType.subscribe;
    @SerializedName("product_ids")
    private final List<String> products;
    private final List<Channel> channels;

    public Subscribe() {
        this.products = Lists.newArrayList();
        this.channels = Lists.newArrayList();
    }

    public void addProduct(final String productId) {
        products.add(productId);
    }

    public void addChannel(final Channel channel) {
        channels.add(channel);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("products", products)
                .add("channels", channels)
                .toString();
    }
}