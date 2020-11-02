package io.github.kimmking.gateway.filter.impl;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author think
 * @date 2020/11/2
 */
public class HttpRequestFilterImpl implements HttpRequestFilter {
    @Override
    public void filter( FullHttpRequest fullRequest, ChannelHandlerContext ctx ) {

    }
}
