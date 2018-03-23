/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drill.exec.util;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This helper class holds any custom Jackson serializers used when outputing
 * the data in JSON format.
 */
public class SerializationModule {

    // copied from DateUtility.  Added here for inclusion into drill-jdbc-all
    public static final DateTimeFormatter formatDate        = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter formatTimeStamp    = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter formatTimeStampTZ = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
    public static final DateTimeFormatter formatTime        = DateTimeFormat.forPattern("HH:mm:ss.SSS");

    public static final SimpleModule drillModule = new SimpleModule("DrillModule");

    static {
        drillModule.addSerializer(Time.class, new JsonSerializer<Time>() {
            @Override
            public void serialize(Time value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(formatTime.print(value.getTime()));
            }
        });

        drillModule.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(formatDate.print(value.getTime()));
            }
        });

        drillModule.addSerializer(Timestamp.class, new JsonSerializer<Timestamp>() {
            @Override
            public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(formatTimeStamp.print(value.getTime()));
            }
        });
    }

    public static final SimpleModule getModule() {
        return drillModule;
    }
}