package com.limaopu.myboot.core.config.filter;

import com.limaopu.myboot.core.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mac
 */
@Slf4j
public class HttpBodyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // log.info("=======> getContentType   =>" + request.getRequestURI() + " ==  " + request.getContentType());
        if (request.getContentType() != null && !request.getContentType().startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            // log.info("=======> startsWith");
            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);

            chain.doFilter(requestWrapper, response);
            return;
        }

//        if ("POST".equalsIgnoreCase(request.getMethod())) {
//
//            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
//
//            String body = HttpUtil.getBodyString(requestWrapper);
//            // log.info("HttpBody = " + body);
//            chain.doFilter(requestWrapper, response);
//            return ;
//        }
//
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}