/*
 * Copyright (C) 2012 Dominik Schürmann <dominik@dominikschuermann.de>
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

package com.lack006.hosts_l.rootcommands.util;

import java.io.IOException;

public class RootAccessDeniedException extends IOException {
    private static final long serialVersionUID = 9088998884166225540L;

    public RootAccessDeniedException() {
        super();
    }

    public RootAccessDeniedException(String detailMessage) {
        super(detailMessage);
    }

}
