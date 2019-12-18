/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.rewrite.feature.shadow.context;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.core.rewrite.context.SQLRewriteContext;
import org.apache.shardingsphere.core.rewrite.context.SQLRewriteContextDecorator;
import org.apache.shardingsphere.core.rewrite.feature.shadow.parameter.ShadowParameterRewriterBuilder;
import org.apache.shardingsphere.core.rewrite.feature.shadow.token.ShadowTokenGenerateBuilder;
import org.apache.shardingsphere.core.rewrite.parameter.rewriter.ParameterRewriter;
import org.apache.shardingsphere.core.rule.ShadowRule;

/**
 * SQL rewrite context decorator for shadow.
 *
 * @author zhyee
 */
@RequiredArgsConstructor
public final class ShadowSQLRewriteContextDecorator implements SQLRewriteContextDecorator {

    private final ShadowRule shadowRule;

    @Override
    public void decorate(final SQLRewriteContext sqlRewriteContext) {
        for (ParameterRewriter each : new ShadowParameterRewriterBuilder(shadowRule).getParameterRewriters(sqlRewriteContext.getTableMetas())) {
            if (!sqlRewriteContext.getParameters().isEmpty() && each.isNeedRewrite(sqlRewriteContext.getSqlStatementContext())) {
                each.rewrite(sqlRewriteContext.getParameterBuilder(), sqlRewriteContext.getSqlStatementContext(), sqlRewriteContext.getParameters());
            }
        }
        sqlRewriteContext.addSQLTokenGenerators(new ShadowTokenGenerateBuilder(shadowRule).getSQLTokenGenerators());
    }
}