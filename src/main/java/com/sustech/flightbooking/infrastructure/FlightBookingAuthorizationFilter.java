package com.sustech.flightbooking.infrastructure;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Henry on 4/25/2017.
 */
public class FlightBookingAuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        FlightBookingAuthenticationToken authentication =
                (FlightBookingAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        if (!authorize(authentication, path, "passenger", "passenger")
                || !authorize(authentication, path, "admin", "administrator")) {
            response.sendRedirect("/login?returnUri=" + path);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean authorize(FlightBookingAuthenticationToken authentication,
                              String path, String controllerPath, String role) {
        if (path.toLowerCase().startsWith("/" + role)) {
            if (authentication == null ||
                    !authentication.getRole().equalsIgnoreCase(role)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void destroy() {

    }
}