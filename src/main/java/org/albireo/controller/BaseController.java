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

package org.albireo.controller;

import org.apache.commons.lang3.StringUtils;
import org.albireo.dto.CBoardActionLog;
import org.albireo.dto.User;
import org.albireo.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

//import org.cboard.services.AuthenticationService;

/**
 * Created by zyong on 2017/9/28.
 */
@Controller
public class BaseController {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected AuthenticationService authenticationService;

    protected ThreadLocal<User> tlUser = new ThreadLocal<>();

    @Value("${log.negativeFilter}")
    protected String negativeFilter;

    @Value("${log.positveFilter}")
    protected String positveFilter;

    @ModelAttribute
    public void initialAuthUser(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        User user = authenticationService.getCurrentUser();
        tlUser.set(user);
        String log = new CBoardActionLog(user, url).toString();

        boolean isNegtiveMatch = false, isPositveMatch = true;

        if (StringUtils.isNotBlank(positveFilter)) {
            isPositveMatch = Pattern.compile(positveFilter).matcher(log).find();
        }

        if (StringUtils.isNotBlank(negativeFilter)) {
            isNegtiveMatch = Pattern.compile(negativeFilter).matcher(log).find();
        }

        if (user != null && !isNegtiveMatch && isPositveMatch) {
            LOG.info(log);
        }
    }
}
