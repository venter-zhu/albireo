/*
 * Copyright Cboard
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.albireo.security.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang.StringUtils;
import org.albireo.dto.User;
import org.albireo.security.ShareAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by yfyuan on 2017/2/22.
 */
public class LocalSecurityFilter implements Filter {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static String context = "";
    private static String schema = "";
    private static LoadingCache<String, String> sidCache = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            return null;
        }
    });

    public static void put(String sid, String uid) {
        sidCache.put(sid, uid);
    }

    public static String getContext() {
        return context;
    }

    public static String getSchema() {
        return schema;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) servletRequest;
        if (StringUtils.isBlank(context) || StringUtils.isBlank(schema)) {
            context = hsr.getLocalPort() + hsr.getContextPath();
            schema = hsr.getScheme();
        }
        if ("/render.html".equals(hsr.getServletPath())) {
            String sid = hsr.getParameter("sid");
            try {
                String uid = sidCache.get(sid);
                if (StringUtils.isNotEmpty(uid)) {
                    User user = new User("shareUser", "", new ArrayList<>());
                    user.setUserId(sidCache.get(sid));
                    SecurityContext context = SecurityContextHolder.getContext();
                    context.setAuthentication(new ShareAuthenticationToken(user));
                    hsr.getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);
                }
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
