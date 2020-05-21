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

package org.albireo.elasticsearch.aggregation;

import org.albireo.util.json.JSONBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.albireo.util.json.JSONBuilder.json;

/**
 * Created by Peter on 2017/5/7.
 */
public class AggregationBuilder {

    public static JSONBuilder termsAggregation(String fieldName, int size, Object missing) {
        return json("terms", json()
                .put("field", fieldName)
                .put("size", size)
                .put("missing", missing)
        );
    }

    public static JSONBuilder dateHistAggregation(String fieldName, String interval, int min_doc_count) {
        return dateHistAggregation(fieldName, interval, 0, null, null);
    }

    public static JSONBuilder dateHistAggregation(String fieldName, String interval, int min_doc_count, Long min, Long max) {
        String format = "yyyy-MM-dd HH:mm";
        JSONBuilder extendedBound = json();
        if (min != null) {
            extendedBound.put("min", timestamp2DateStr(min, format));
        }
        if (max != null) {
            extendedBound.put("max", timestamp2DateStr(max, format));
        }
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return json("date_histogram",
                json().put("field", fieldName)
                        .put("format", format)
                        .put("time_zone", tz.getID())
                        .put("interval", interval)
                        .put("min_doc_count", min_doc_count)
                        .put("extended_bounds", extendedBound)
        );
    }

    private static String timestamp2DateStr(long timestamp, String format) {
        Date date = new Date();
        date.setTime(timestamp);
        return new SimpleDateFormat(format).format(date);
    }
}
