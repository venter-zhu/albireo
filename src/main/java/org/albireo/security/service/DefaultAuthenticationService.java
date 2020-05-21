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

import org.albireo.dto.JwtUser;
import org.albireo.dto.User;
import org.albireo.services.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by yfyuan on 2016/12/14.
 *
 * @modify : venter.zhu on 2020-04-24
 */
@Service
public class DefaultAuthenticationService implements AuthenticationService {

    @Override
    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        JwtUser user = (JwtUser) authentication.getPrincipal();
        User user1 = null;
        if (user == null) {
            return null;
        } else {
            user1 = new User(user, user.getAuthorities());
        }
        return user1;
    }

}
