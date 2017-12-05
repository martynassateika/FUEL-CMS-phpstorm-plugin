/*
 * Copyright 2017 Martynas Sateika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lt.martynassateika.fuelcms.util;

/**
 * Re-used message formats.
 */
public final class Messages {

    /**
     * @param className a class name
     * @return a message indicating <tt>className</tt> is not a valid PHP class name
     */
    public static String invalidClassName(String className) {
        return String.format("'%s' is not a valid PHP class name", className);
    }

}
