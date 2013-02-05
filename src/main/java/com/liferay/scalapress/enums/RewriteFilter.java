package com.liferay.scalapress.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RewriteFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Pattern htmlPattern;
    private Pattern htmlPatternWrong;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, final ServletResponse arg1, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) arg0;
        String path = request.getServletPath();

        Matcher matcher = htmlPattern.matcher(path);
        Matcher matcherWrong = htmlPatternWrong.matcher(path);


        if (matcher.matches() && !matcherWrong.matches()) {

            String type = matcher.group(1);
            String id = matcher.group(2);

            Map<String, String> params = new HashMap();

            StringBuilder url = new StringBuilder();

            switch (type) {
                case "i":
                    url.append("/object/");
                    url.append(id);
                    //		url.append("&path=");
                    //		url.append(path.substring(1));
                    break;
                case "c":
                    url.append("/folder/");
                    url.append(id);
                    //		url.append("&path=");
                    //		url.append(path.substring(1));
                    break;
            }

            if (params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    url.append("&");
                    url.append(entry.getKey());
                    url.append("=");
                    url.append(entry.getValue());
                }
            }

            logger.debug("Forwarding URL [{}]", url);
            arg0.getRequestDispatcher(url.toString()).forward(arg0, arg1);
            return;

        }

        chain.doFilter(arg0, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        htmlPattern = Pattern.compile(".*-([a-z])(\\d+)\\.html\\??(.*)$");

        // to avoid links like "/smth/items-c377.html"
        htmlPatternWrong = Pattern.compile("/.*([a-z])/.*-([a-z])(\\d+)\\.html\\??(.*)$");
    }

}

