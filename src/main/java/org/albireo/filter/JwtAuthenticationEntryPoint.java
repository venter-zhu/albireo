/*
 * Copyright Albireo
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

package org.albireo.filter;

import com.alibaba.fastjson.JSONObject;
import org.albireo.dto.Result;
import org.albireo.dto.ResultCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : venter.zhu
 * @version : 1.0
 * @date :  2020/4/23 0023 21:08
 **/
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(ResultCodeEnum.UNAUTHORIZED.getCode());
        // 统一处理token失效或者无效的问题
        String reason = JSONObject.toJSONString(Result.failure(ResultCodeEnum.UNAUTHORIZED.getCode(), authException.getMessage()));
        response.getWriter().write(reason);
    }
}
