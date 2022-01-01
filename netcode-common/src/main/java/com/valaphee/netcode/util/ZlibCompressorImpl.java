/*
 * Copyright (c) 2021-2022, Valaphee.
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

package com.valaphee.netcode.util;

/**
 * @author Kevin Ludwig
 */
public final class ZlibCompressorImpl {
    static {
        startup();
    }

    public int consumed;
    public boolean finished;

    public static native void startup();

    public native long init(boolean compress, int level, boolean raw);

    public native int process(long zStream, long in, int inLength, long out, int outLength, boolean compress);

    public native void reset(long zStream, boolean compress);

    public native void free(long zStream, boolean compress);
}
