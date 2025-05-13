package com.mycompany.baikiemtra;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class RequestListener implements ServletRequestListener {
    private static final Logger logger = Logger.getLogger(RequestListener.class.getName());
    private static int requestCount = 0;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        requestCount++;
        logger.info("Số yêu cầu đã xử lý: " + requestCount);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        // Không cần xử lý
    }
}