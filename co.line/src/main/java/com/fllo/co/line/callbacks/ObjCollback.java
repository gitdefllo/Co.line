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

import com.fllo.co.line.results.Error;

public interface ObjCollback<T> {

    /**
     * Return two objects: Error and Custom Class Object
     * previously attached
     *
     * @param obj (T) custom response object
     * @param err (Error) error object
     */
    void onResult(T obj, Error err);
}
