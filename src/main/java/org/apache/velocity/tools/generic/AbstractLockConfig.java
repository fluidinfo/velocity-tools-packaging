package org.apache.velocity.tools.generic;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * <p>Apologies to anyone who stepped up to use Tools 2.0-beta1
 * and made their tools extend this class.  Please just extend
 * the SafeConfig class now.  It is better named and no longer
 * requires implementation of {@link #configure(ValueParser)},
 * as that may not be needed by subclasses that may simply want
 * to know safeMode and/or lockConfig settings.
 * </p>
 * <p>
 * Sorry for any inconvenience, but this class will be removed
 * before Tools 2.0 goes final.  Please update your subclasses
 * before that time.  Thanks.
 * </p>
 *
 * @since VelocityTools 2.0
 * @deprecated Use {@link SafeConfig} instead
 */
@Deprecated
public abstract class AbstractLockConfig extends SafeConfig
{
}
