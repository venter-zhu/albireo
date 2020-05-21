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

import org.albireo.security.ShareAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by yfyuan on 2017/3/16.
 */
public class ShareAuthenticationProviderDecorator implements AuthenticationProvider {

    private AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof ShareAuthenticationToken) {
            return authentication;
        } else {
            return authenticationProvider.authenticate(authentication);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        if (aClass.equals(ShareAuthenticationToken.class)) {
            return true;
        } else {
            return authenticationProvider.supports(aClass);
        }
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
