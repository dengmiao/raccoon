package com.cure.core.config.undertow;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import java.io.IOException;

/**
 * @title: UndertowServerFactoryCustomizer
 * @description: 设置Undertow服务器
 * @author: dengmiao
 * @create: 2019-05-17 17:33
 **/
public class UndertowServerFactoryCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer = deploymentInfo -> {
            WebSocketDeploymentInfo info = (WebSocketDeploymentInfo) deploymentInfo.getServletContextAttributes().get(WebSocketDeploymentInfo.ATTRIBUTE_NAME);
            XnioWorker worker = getXnioWorker();
            ByteBufferPool buffers = new DefaultByteBufferPool(Boolean.getBoolean("io.undertow.websockets.direct-buffers"), 1024, 100, 12);
            info.setWorker(worker);
            info.setBuffers(buffers);
        };
        factory.addDeploymentInfoCustomizers(undertowDeploymentInfoCustomizer);
    }

    /**
     * 获取XnioWorker
     *
     * @return
     */
    private XnioWorker getXnioWorker() {
        XnioWorker worker = null;
        try {
            worker = Xnio.getInstance().createWorker(OptionMap.create(Options.THREAD_DAEMON, true));
        } catch (IOException ignored) {
        }
        return worker;
    }
}
