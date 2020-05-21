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

"use strict";

function waitFor(testFx, onReady, timeOutMillis) {
    var maxtimeOutMillis = timeOutMillis ? timeOutMillis : 3000, //< Default Max Timout is 3s
        start = new Date().getTime(),
        condition = false,
        interval = setInterval(function () {
            var elapsedTime = new Date().getTime() - start;
            console.log("elapsedTime:" + elapsedTime + ", [" + maxtimeOutMillis + "]");
            if ((elapsedTime < maxtimeOutMillis) && !condition) {
                // If not time-out yet and condition not yet fulfilled
                condition = (typeof(testFx) === "string" ? eval(testFx) : testFx()); //< defensive code
            } else {
                if (!condition) {
                    // If condition still not fulfilled (timeout but condition is 'false')
                    console.log("'waitFor()' timeout");
                    phantom.exit(1);
                } else {
                    // Condition fulfilled (timeout and/or condition is 'true')
                    console.log("'waitFor()' finished in " + (new Date().getTime() - start) + "ms.");
                    typeof(onReady) === "string" ? eval(onReady) : onReady(); //< Do what it's supposed to do once the condition is fulfilled
                    clearInterval(interval); //< Stop this interval
                }
            }
        }, 2000); //< repeat check every 2000ms
}

var page = require('webpage').create();
var system = require('system');
page.viewportSize = {width: 1500, height: 1080};
var _url = system.args[1];
console.log("[PhantomJS] Opening Url:", _url);
page.open(_url, function (status) {
    if (status !== 'success') {
        console.log('open page fail!');
        phantom.exit();
    } else {
        console.log("success");
        waitFor(function () {
            return page.evaluate(function () {
                return $(".persistFinish").length > 0;
            });
        }, function () {
            phantom.exit();
        }, 120000);
    }
});
