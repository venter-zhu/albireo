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

package org.albireo.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.albireo.dto.JwtUser;

import java.util.Date;
import java.util.HashMap;

/**
 * @author zjp
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET = "joybosecret";
    private static final String ISS = "joybo";
    private static final String WECHAT_SECRET = "joybosecret";
    private static final String WECHAT_ISS = "joybo";
    private static final String APP_TYPE = "appType";
    private static final String OPEN_ID = "openId";
    private static final String UNION_ID = "unionId";
    private static final String PHONE = "phone";
    private static final String APP_ID = "appId";
    private static final String USER_ID = "userId";
    private static final String RESOURCE_TYPE = "resourceType";

    /**微信用户信息的key*/
    public static final String WECHAT_USERINFO = "wechatUserinfo";

    /**
     * 角色的key
     */
    private static final String ROLE_CLAIMS = "rol";

    /**
     * 过期时间是3600秒，既是1个小时
     */
    private static final long EXPIRATION = 3600L;

    /**
     * 选择了记住我之后的过期时间为7天
     */
    private static final long EXPIRATION_REMEMBER = 604800L;
    /**
     * 手机登陆，那么token有效期延长到30天
     */
    private static final long EXPIRATION_MOBILE = 2592000L;

    /**
     * 微信用户中心token过期时间
     */
    private static final long EXPIRATION_WECHAT = 2592000L;

    /**
     * 创建token
     */
    public static String createToken(String username,String role, boolean isRememberMe,boolean isMobile) {
//        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        // 记住我跟手机登陆互斥
//        if(isMobile){
//            expiration = EXPIRATION_MOBILE;
//        }
        // 默认过期时间为30天
        long expiration = EXPIRATION_MOBILE * 12;
//        long expiration = 10 * 60;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    public static String createToken(JwtUser user, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        if (isRememberMe) {
            expiration = expiration * 24 * 7;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        map.put("userId", user.getUserId());
        map.put("loginName", user.getLoginName());
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }


    /**后台管理员用户*/
    public static String createToken(String appType , String appId,String phone,String userId,String role,String resourceType) {
        // 默认过期时间为360天
        long expiration = EXPIRATION_WECHAT * 6;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        map.put(APP_TYPE,appType);
        map.put(PHONE,phone);
        map.put(APP_ID,appId);
        map.put(USER_ID,userId);
        map.put(RESOURCE_TYPE,resourceType);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(SECRET)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     *
     从token中获取用户名
     */
    public static String getUsername(String token){
        return getTokenBody(token.replace(JwtTokenUtils.TOKEN_PREFIX, "")).getSubject();
    }

    /**
     * 获取用户角色
     */
    public static String getUserRole(String token) {
        return (String) getTokenBody(token.replace(JwtTokenUtils.TOKEN_PREFIX, "")).get(ROLE_CLAIMS);
    }

    /**
     * 获取用户角色
     */
    public static String getUserId(String token) {
        return (String) getTokenBody(token.replace(JwtTokenUtils.TOKEN_PREFIX, "")).get("userId");
    }

    public static String getLoginUserName(String token) {
        return (String) getTokenBody(token.replace(JwtTokenUtils.TOKEN_PREFIX, "")).get("loginName");
    }

    /**
     * 获取appId
     */
    public static String getSource(String token) {
        return (String) getTokenBody(token.replace(JwtTokenUtils.TOKEN_PREFIX, "")).get(APP_ID);
    }

    /**
     *
     获取应用类型
     */
    public static String getAppType(String token){
        return (String) getTokenBody(token.replace(JwtTokenUtils.TOKEN_PREFIX,"")).get(APP_TYPE);
    }

    /**
     *
     token 是否已过期
     */
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 获取Claims
     * @param token
     * @return
     */
    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

}
