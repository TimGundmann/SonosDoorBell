package dk.gundmann.general;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

/**
* A filter which logs web requests that lead to an error in the system.
*
*/
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class LogRequestFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(getClass());

    private ErrorAttributes errorAttributes;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
        Map<String, Object> trace = getTrace(wrappedRequest, response.getStatus());

        getBody(wrappedRequest, trace);
        logTrace(wrappedRequest, trace);
    }

    private void getBody(ContentCachingRequestWrapper request, Map<String, Object> trace) {
        // wrap request to make sure we can read the body of the request (otherwise it will be consumed by the actual
        // request handler)
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                }
                catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }

                trace.put("body", payload);
            }
        }
    }

    private void logTrace(HttpServletRequest request, Map<String, Object> trace) {
        Object method = trace.get("method");
        Object path = trace.get("path");
        Object statusCode = trace.get("statusCode");

        logger.info(String.format("%s %s produced an error status code '%s'. Trace: '%s'", method, path, statusCode,
        trace));
    }

    protected Map<String, Object> getTrace(HttpServletRequest request, int status) {
        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");

        Principal principal = request.getUserPrincipal();

        Map<String, Object> trace = new LinkedHashMap<String, Object>();
        trace.put("method", request.getMethod());
        trace.put("path", request.getRequestURI());
        trace.put("principal", principal == null ? "none" : principal.getName());
        trace.put("query", request.getQueryString());
        trace.put("statusCode", status);

        if (exception != null && this.errorAttributes != null) {
            trace.put("error", this.errorAttributes
                .getErrorAttributes(new ServletWebRequest(request), true));
        }

        return trace;
    }

}