/*
 * Copyright 2016 Middlesex University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.mdx.cs.ie.acontextlib;

/**
 * Basic exception for handling context related errors.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class ContextException extends Exception {

    private static final long serialVersionUID = -773464875086441307L;

    public ContextException() {
        super();
    }

    public ContextException(String message, Throwable toThrow) {
        super(message, toThrow);
    }

    public ContextException(String message) {
        super(message);
    }

    public ContextException(Throwable toThrow) {
        super(toThrow);
    }

}
