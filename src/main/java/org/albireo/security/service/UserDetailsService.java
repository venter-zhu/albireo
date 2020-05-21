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

import org.albireo.dao.UserDao;
import org.albireo.dto.User;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public final class UserDetailsService extends AbstractCasAssertionUserDetailsService {

    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";

    @SuppressWarnings("unused")
    private final String[] attributes;

    @SuppressWarnings("unused")
    private boolean convertToUpperCase = true;

    @Autowired
    private UserDao userDao;

    public UserDetailsService(final String[] attributes) {
        Assert.notNull(attributes, "attributes cannot be null.");
        Assert.isTrue(attributes.length > 0, "At least one attribute is required to retrieve roles from.");
        this.attributes = attributes;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected UserDetails loadUserDetails(final Assertion assertion) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User(
                (String) assertion.getPrincipal().getAttributes().get("displayName"),
                NON_EXISTENT_PASSWORD_VALUE,
                true, true, true, true,
                grantedAuthorities);
        user.setCompany((String) assertion.getPrincipal().getAttributes().get("company"));
        user.setDepartment((String) assertion.getPrincipal().getAttributes().get("department"));
        user.setUserId((String) assertion.getPrincipal().getAttributes().get("employee"));
        user.setName(assertion.getPrincipal().getName());
        userDao.saveNewUser(user.getUserId(), user.getUsername(), user.getName());
        return user;
    }

    /**
     * Converts the returned attribute values to uppercase values.
     *
     * @param convertToUpperCase true if it should convert, false otherwise.
     */
    public void setConvertToUpperCase(final boolean convertToUpperCase) {
        this.convertToUpperCase = convertToUpperCase;
    }
}
