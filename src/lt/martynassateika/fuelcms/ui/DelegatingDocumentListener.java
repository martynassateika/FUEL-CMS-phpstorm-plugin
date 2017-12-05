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

package lt.martynassateika.fuelcms.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Delegates all methods of {@link DocumentListener} to the same method.
 */
@FunctionalInterface
interface DelegatingDocumentListener extends DocumentListener {

    void updated(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        updated(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        updated(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        updated(e);
    }

}
