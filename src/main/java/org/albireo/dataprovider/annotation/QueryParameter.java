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

package org.albireo.dataprovider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yfyuan on 2016/8/18.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParameter {
    String label();

    Type type();

    int order() default 0;

    // Init value
    String value() default "";

    String placeholder() default "";

    String[] options() default "N/A";

    // 获取options数组的方法名称
    String optionsMethod() default "";

    boolean checked() default false;

    boolean required() default false;

    // 页面类型，all、test、dataset、widget，多个可用逗号隔开，默认：all
    String pageType() default "all";

    enum Type {
        Input("input"), TextArea("textarea"), TextArea2("textarea2"), Number("number"), Checkbox("checkbox"), Select("select");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

    }
}
