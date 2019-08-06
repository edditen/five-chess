package com.tenchael.chess.handlers;


import com.tenchael.chess.config.Configs;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.RandomAccessFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);


    static {
        LOGGER.debug("pages location: {}", Configs.WEB_APP_BASE);
    }

    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
            throws Exception {
        LOGGER.debug("request uri: {}", request.uri());
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            String path;
            if ("/".equals(request.uri())) {
                path = Configs.WEB_APP_BASE + Configs.INDEX;
            } else {
                int index = request.uri().indexOf("?");
                if (index == -1) {
                    path = Configs.WEB_APP_BASE + request.uri();
                } else {
                    path = Configs.WEB_APP_BASE + request.uri().substring(0, index);
                }
            }


            RandomAccessFile file = new RandomAccessFile(path, "r");
            HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            contentTypeSetting(request, response);

            boolean keepAlive = HttpUtil.isKeepAlive(request);

            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);

            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void contentTypeSetting(HttpRequest request, HttpResponse response) {
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        if (request.uri().endsWith(".js")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-javascript");
        }
        if (request.uri().endsWith(".css")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css");
        }
        if (request.uri().endsWith(".ico")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/x-icon");
        }
    }
}
