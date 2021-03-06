/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package io.shardingjdbc.core.merger.dql.orderby;

import io.shardingjdbc.core.merger.QueryResult;
import io.shardingjdbc.core.merger.dql.common.AbstractStreamResultSetMerger;
import io.shardingjdbc.core.parsing.parser.context.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;

import java.sql.SQLException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Stream merger for order by.
 *
 * @author zhangliang
 */
public class OrderByStreamResultSetMerger extends AbstractStreamResultSetMerger {
    
    private final List<OrderItem> orderByItems;
    
    @Getter(AccessLevel.PROTECTED)
    private final Queue<OrderByValue> orderByValuesQueue;
    
    @Getter(AccessLevel.PROTECTED)
    private boolean isFirstNext;
    
    public OrderByStreamResultSetMerger(final List<QueryResult> queryResults, final List<OrderItem> orderByItems) throws SQLException {
        this.orderByItems = orderByItems;
        this.orderByValuesQueue = new PriorityQueue<>(queryResults.size());
        orderResultSetsToQueue(queryResults);
        isFirstNext = true;
    }
    
    private void orderResultSetsToQueue(final List<QueryResult> queryResults) throws SQLException {
        for (QueryResult each : queryResults) {
            OrderByValue orderByValue = new OrderByValue(each, orderByItems);
            if (orderByValue.next()) {
                orderByValuesQueue.offer(orderByValue);
            }
        }
        setCurrentQueryResult(orderByValuesQueue.isEmpty() ? queryResults.get(0) : orderByValuesQueue.peek().getQueryResult());
    }
    
    @Override
    public boolean next() throws SQLException {
        if (orderByValuesQueue.isEmpty()) {
            return false;
        }
        if (isFirstNext) {
            isFirstNext = false;
            return true;
        }
        OrderByValue firstOrderByValue = orderByValuesQueue.poll();
        if (firstOrderByValue.next()) {
            orderByValuesQueue.offer(firstOrderByValue);
        }
        if (orderByValuesQueue.isEmpty()) {
            return false;
        }
        setCurrentQueryResult(orderByValuesQueue.peek().getQueryResult());
        return true;
    }
}
