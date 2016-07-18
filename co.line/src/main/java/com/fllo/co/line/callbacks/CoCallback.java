/*
 * Copyright 2015 Florent Blot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fllo.co.line.callbacks;

import com.fllo.co.line.models.CoError;
import com.fllo.co.line.models.CoResponse;

public interface CoCallback {

    /**
     * This method returns a couple of response objects
     *
     * @param err (CoError) error object
     * @param res (CoResponse) response object
     */
    void onResult(CoError err, CoResponse res);
}
