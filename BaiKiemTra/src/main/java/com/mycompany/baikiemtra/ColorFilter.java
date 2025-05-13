package com.mycompany.baikiemtra;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;


@WebFilter("/ColorServlet")
public class ColorFilter implements Filter {
    private static final String[] VALID_COLORS = {"red", "blue", "green", "yellow", "white"};

    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        String color = request.getParameter("color");
        boolean isValid = false;
        
        for (String validColor : VALID_COLORS) {
            if (validColor.equalsIgnoreCase(color)) {
                isValid = true;
                break;
            }
        }
        
        if (isValid) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).sendRedirect("color.jsp");
        }
    }

    public void destroy() {
        // Hủy filter
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}