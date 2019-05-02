/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package streamserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Http static server via zero copy which reduces overhead of copying kernel
 * buffer to memory.
 */
public class HttpStaticFileServerHandler2 extends SimpleChannelInboundHandler<HttpRequest> {

    static String dir = "/opt/applications/streamserver/streams/";
    //static String dir = "/home/julius/streams/";

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpRequest request) {
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        final String uri = request.getUri();
        System.out.println("uri: " + uri);
        try {
            //String fromFile = "http://hiddennetworks.xyz:80/live/IanGathu/Wanjiru2308!" + uri.replaceAll("mp4", "ts");
            String fromFile = "http://vapi.vaders.tv/play/" + uri.replaceAll("mp4", "ts"+"?token=eyJ1c2VybmFtZSI6ImlnYXRodSIsInBhc3N3b3JkIjoid2FuamlydTIzIn0=");
            Thread download = new Thread() {
                public void run() {
                    try {
                        System.setProperty("http.agent", "Chrome");
                        URL website = new URL(fromFile);
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream(dir + uri);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    } catch (IOException ex) {
                        System.err.println("Download Error" + ex);
                        File dfile = new File(dir + uri.replace("/", ""));
                        Path fpath = Paths.get(dfile.toString());
                        System.err.println("Deleting: " + dfile.toString());
                        try {
                            Files.deleteIfExists(fpath);
                        } catch (IOException exe) {
                            Logger.getLogger(HttpStaticFileServerHandler2.class.getName()).log(Level.SEVERE, null, exe);
                        }
                    }
                }
            };
            File dfile = new File(dir + uri.replace("/", ""));
            if (dfile.exists()) {
                System.out.println("Already existing: " + dfile);
                int cusers = getChannelUsers(uri.replace("/", ""));
                setChannelUsers(uri.replace("/", ""), String.valueOf(cusers + 1));
                final String path = sanitizeUri(uri);
                System.out.println("Paath: " + path);
                if (path == null) {
                    sendError(ctx, FORBIDDEN);
                    return;
                }

                File file = new File(path);
                if (file.isHidden() || !file.exists()) {
                    sendError(ctx, NOT_FOUND);
                    return;
                }

                if (!file.isFile()) {
                    sendError(ctx, FORBIDDEN);
                    return;
                }

                RandomAccessFile raf;
                try {
                    raf = new RandomAccessFile(file, "r");
                } catch (FileNotFoundException ignore) {
                    sendError(ctx, NOT_FOUND);
                    return;
                }
                //long fileLength = raf.length();
                long ofileLength = raf.length();
                long fileLength = Long.MAX_VALUE;

                System.out.println("Original file length: " + ofileLength);

                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
                setContentTypeHeader(response, file);
                if (HttpHeaders.isKeepAlive(request)) {
                    response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                }

                // Write the content.
                ChannelFuture sendFileFuture;
                ChannelFuture lastContentFuture;

                // Tell clients that Partial Requests are available.
                response.headers().add(HttpHeaders.Names.ACCEPT_RANGES, HttpHeaders.Values.BYTES);

                String rangeHeader = request.headers().get(HttpHeaders.Names.RANGE);
                System.out.println("RangeHeader: " + rangeHeader);
                System.out.println(HttpHeaders.Names.RANGE + " = " + rangeHeader);
                if (rangeHeader != null && rangeHeader.length() > 0) { // Partial Request
                    PartialRequestInfo partialRequestInfo = getPartialRequestInfo(rangeHeader, fileLength);
                    PartialRequestInfo partialRequestInfo2 = getPartialRequestInfo(rangeHeader, ofileLength);
                    // Set Response Header
                    response.headers().add(HttpHeaders.Names.CONTENT_RANGE, HttpHeaders.Values.BYTES + " "
                            + partialRequestInfo2.endOffset + "-" + partialRequestInfo.endOffset + "/" + fileLength);
                    System.out.println(
                            HttpHeaders.Names.CONTENT_RANGE + " : " + response.headers().get(HttpHeaders.Names.CONTENT_RANGE));

                    HttpHeaders.setContentLength(response, partialRequestInfo.getChunkSize());
                    System.out.println(HttpHeaders.Names.CONTENT_LENGTH + " : " + partialRequestInfo.getChunkSize());

                    response.setStatus(HttpResponseStatus.PARTIAL_CONTENT);

                    // Write Response
                    ctx.write(response);
                    sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), partialRequestInfo.getStartOffset(),
                            partialRequestInfo.getChunkSize()), ctx.newProgressivePromise());
                } else {
                    // Set Response Header
                    HttpHeaders.setContentLength(response, fileLength);

                    // Write Response
                    ctx.write(response);
                    //here we go
                    sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), ofileLength, fileLength),
                            ctx.newProgressivePromise());
                }

                lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

                sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                    @Override
                    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                        if (total < 0) { // total unknown
                            System.err.println(future.channel() + " Transfer progress: " + progress);
                        } else {
                            //System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
                        }
                    }

                    @Override
                    public void operationComplete(ChannelProgressiveFuture future) {
                        System.err.println(future.channel() + " Transfer complete.");
                        int cusers = getChannelUsers(uri.replace("/", ""));
                        setChannelUsers(uri.replace("/", ""), String.valueOf(cusers - 1));
                        if (cusers == 1) {
                            download.interrupt();
                            File dfile = new File(dir + uri.replace("/", ""));
                            Path fpath = Paths.get(dfile.toString());
                            System.err.println("Deleting: " + dfile.toString());
                            try {
                                Files.deleteIfExists(fpath);
                            } catch (IOException exe) {
                                Logger.getLogger(HttpStaticFileServerHandler2.class.getName()).log(Level.SEVERE, null, exe);
                            }
                        }
                    }
                });

                // Decide whether to close the connection or not.
                if (!HttpHeaders.isKeepAlive(request)) {
                    // Close the connection when the whole content is written out.
                    lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                }

            } else {
                download.start();//start the thread*/   
                setChannelUsers(uri.replace("/", ""), "1");
                final String path = sanitizeUri(uri);
                System.out.println("Paath: " + path);
                if (path == null) {
                    sendError(ctx, FORBIDDEN);
                    return;
                }

                File file = new File(path);
                if (file.isHidden() || !file.exists()) {
                    sendError(ctx, NOT_FOUND);
                    return;
                }

                if (!file.isFile()) {
                    sendError(ctx, FORBIDDEN);
                    return;
                }

                RandomAccessFile raf;
                try {
                    raf = new RandomAccessFile(file, "r");
                } catch (FileNotFoundException ignore) {
                    sendError(ctx, NOT_FOUND);
                    return;
                }
                //long fileLength = raf.length();
                long fileLength = Long.MAX_VALUE;

                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
                setContentTypeHeader(response, file);
                if (HttpHeaders.isKeepAlive(request)) {
                    response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                }

                // Write the content.
                ChannelFuture sendFileFuture;
                ChannelFuture lastContentFuture;

                // Tell clients that Partial Requests are available.
                response.headers().add(HttpHeaders.Names.ACCEPT_RANGES, HttpHeaders.Values.BYTES);

                String rangeHeader = request.headers().get(HttpHeaders.Names.RANGE);
                System.out.println(HttpHeaders.Names.RANGE + " = " + rangeHeader);
                if (rangeHeader != null && rangeHeader.length() > 0) { // Partial Request
                    PartialRequestInfo partialRequestInfo = getPartialRequestInfo(rangeHeader, fileLength);

                    // Set Response Header
                    response.headers().add(HttpHeaders.Names.CONTENT_RANGE, HttpHeaders.Values.BYTES + " "
                            + partialRequestInfo.startOffset + "-" + partialRequestInfo.endOffset + "/" + fileLength);
                    System.out.println(
                            HttpHeaders.Names.CONTENT_RANGE + " : " + response.headers().get(HttpHeaders.Names.CONTENT_RANGE));

                    HttpHeaders.setContentLength(response, partialRequestInfo.getChunkSize());
                    System.out.println(HttpHeaders.Names.CONTENT_LENGTH + " : " + partialRequestInfo.getChunkSize());

                    response.setStatus(HttpResponseStatus.PARTIAL_CONTENT);

                    // Write Response
                    ctx.write(response);
                    sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), partialRequestInfo.getStartOffset(),
                            partialRequestInfo.getChunkSize()), ctx.newProgressivePromise());
                } else {
                    // Set Response Header
                    HttpHeaders.setContentLength(response, fileLength);

                    // Write Response
                    ctx.write(response);
                    sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength),
                            ctx.newProgressivePromise());
                }

                lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

                sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                    @Override
                    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                        if (total < 0) { // total unknown
                            //System.err.println(future.channel() + " Transfer progress: " + progress);
                        } else {
                            //System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
                        }
                    }

                    @Override
                    public void operationComplete(ChannelProgressiveFuture future) {
                        System.err.println(future.channel() + " Transfer complete.");
                        int cusers = getChannelUsers(uri.replace("/", ""));
                        setChannelUsers(uri.replace("/", ""), String.valueOf(cusers - 1));
                        if (cusers == 1) {
                            download.interrupt();
                            File dfile = new File(dir + uri.replace("/", ""));
                            Path fpath = Paths.get(dfile.toString());
                            System.err.println("Deleting: " + dfile.toString());
                            try {
                                Files.deleteIfExists(fpath);
                            } catch (IOException exe) {
                                Logger.getLogger(HttpStaticFileServerHandler2.class.getName()).log(Level.SEVERE, null, exe);
                            }
                        }
                    }
                });

                // Decide whether to close the connection or not.
                if (!HttpHeaders.isKeepAlive(request)) {
                    // Close the connection when the whole content is written out.
                    lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                }

            }
        } catch (Exception exe) {
            Logger.getLogger(HttpStaticFileServerHandler2.class.getName()).log(Level.SEVERE, null, exe);
            File dfile = new File(dir + uri.replace("/", ""));
            Path fpath = Paths.get(dfile.toString());
            System.err.println("Deleting: " + dfile.toString());
            try {
                Files.deleteIfExists(fpath);
            } catch (IOException ex) {
                Logger.getLogger(HttpStaticFileServerHandler2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static String sanitizeUri(String uri) throws FileNotFoundException, InterruptedException {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }

        if (uri.isEmpty() || uri.charAt(0) != '/') {
            return null;
        }

        // Convert file separators.
        uri = uri.replace('/', File.separatorChar);

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.charAt(0) == '.'
                || uri.charAt(uri.length() - 1) == '.' || INSECURE_URI.matcher(uri).matches()) {
            System.out.println("insecure");
            return null;
        }

        // Convert to absolute path.
        //return SystemPropertyUtil.get("user.dir") + File.separator + uri;
        Thread.sleep(7000);
        return dir + File.separator + uri;
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Sets the content type header for the HTTP Response
     *
     * @param response HTTP response
     * @param file file to extract content type
     */
    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }

    private PartialRequestInfo getPartialRequestInfo(String rangeHeader, long fileLength) {
        PartialRequestInfo partialRequestInfo = new PartialRequestInfo();
        long startOffset = 0;
        long endOffset;
        try {
            startOffset = Integer
                    .parseInt(rangeHeader.trim().replace(HttpHeaders.Values.BYTES + "=", "").replace("-", ""));
        } catch (NumberFormatException e) {
        }
        endOffset = startOffset + fileLength;

        if (endOffset >= fileLength) {
            endOffset = fileLength - 1;
        }
        long chunkSize = endOffset - startOffset + 1;

        partialRequestInfo.setStartOffset(startOffset);
        partialRequestInfo.setEndOffset(endOffset);
        partialRequestInfo.setChunkSize(chunkSize);
        return partialRequestInfo;
    }

    class PartialRequestInfo {

        private long startOffset;
        private long endOffset;
        private long chunkSize;

        public long getStartOffset() {
            return startOffset;
        }

        public void setStartOffset(long startOffset) {
            this.startOffset = startOffset;
        }

        public long getEndOffset() {
            return endOffset;
        }

        public void setEndOffset(long endOffset) {
            this.endOffset = endOffset;
        }

        public long getChunkSize() {
            return chunkSize;
        }

        public void setChunkSize(long chunkSize) {
            this.chunkSize = chunkSize;
        }
    }

    public int getChannelUsers(String chan) {

        String stt = "";
        try {
            FileReader reader = new FileReader(dir + chan + ".txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            stt = bufferedReader.readLine();
            reader.close();

        } catch (IOException e) {
            //setStatus("off");
            e.printStackTrace();
        }
        return parseInt(stt);
    }

    public void setChannelUsers(String chan, String users) {
        try {
            FileWriter writer = new FileWriter(dir + chan + ".txt");
            writer.write(users);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void terminalCMD(String command) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        try {
            Process shell = pb.start();
        } catch (Exception exp) {
            System.out.println("Exception--->" + exp.getMessage());
        }
        // close the stream

        System.out.println("Done---->");
    }
}
