/*
 * Copyright 2016 Florent Blot
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
package com.fllo.co.line.builders;

public enum HttpMethod {

    // Enums
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    HEAD("HEAD");

    private final String value;

    /**
     * Set the value
     * @param value methods value
     */
    private HttpMethod(final String value) {
        this.value = value;
    }

    /**
     * Get the value of enum
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return value;
    }
}
