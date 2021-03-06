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
package org.apache.drill.exec.physical.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.Iterators;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.exec.physical.base.AbstractSingle;
import org.apache.drill.exec.physical.base.LateralContract;
import org.apache.drill.exec.physical.base.PhysicalOperator;
import org.apache.drill.exec.physical.base.PhysicalVisitor;
import org.apache.drill.exec.proto.beans.CoreOperatorType;

import java.util.Iterator;
import java.util.List;

import static org.apache.drill.exec.proto.UserBitShared.CoreOperatorType.UNNEST_VALUE;
import static org.apache.drill.exec.proto.beans.CoreOperatorType.UNNEST;

@JsonTypeName("unnest")
public class UnnestPOP extends AbstractSingle {
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UnnestPOP.class);

  private SchemaPath column;

  @JsonCreator
  public UnnestPOP(
      @JsonProperty("child") PhysicalOperator child, // Operator with incoming record batch
      @JsonProperty("column") SchemaPath column) {
    super(child);
    this.column = column;
  }


  @Override
  public Iterator<PhysicalOperator> iterator() {
    return Iterators.singletonIterator(child);
  }

  public SchemaPath getColumn() {
    return column;
  }

  @Override
  public <T, X, E extends Throwable> T accept(PhysicalVisitor<T, X, E> physicalVisitor, X value) throws E {
    return physicalVisitor.visitUnnest(this, value);
  }

  @Override
  public PhysicalOperator getNewWithChild(PhysicalOperator child) {
    UnnestPOP unnest =  new UnnestPOP(child, column);
    return unnest;
  }

  @Override
  public int getOperatorType() {
    return UNNEST_VALUE;
  }
}
