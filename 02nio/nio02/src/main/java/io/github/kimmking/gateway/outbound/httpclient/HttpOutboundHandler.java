package io.github.kimmking.gateway.outbound.httpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author isaac
 * @date 2020/11/2
 */
public class HttpOutboundHandler {

    private String backendUrl;

    public HttpOutboundHandler( String serverUrl) {
        this.backendUrl = serverUrl.endsWith("/") ? serverUrl.substring(0,serverUrl.length()-1) : serverUrl;
    }

    public void handle( final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws IOException {
        final String url = this.backendUrl + fullRequest.uri();
        fetchGet(fullRequest, ctx, url);

    }
    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url){
        final HttpGet httpGet = new HttpGet(url);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleResponse(inbound, ctx, response);
    }
    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) {
        FullHttpResponse response = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
            System.out.println(new String(body));

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                ctx.write(response);
            }
            ctx.flush();
            ctx.close();
        }

    }
}

