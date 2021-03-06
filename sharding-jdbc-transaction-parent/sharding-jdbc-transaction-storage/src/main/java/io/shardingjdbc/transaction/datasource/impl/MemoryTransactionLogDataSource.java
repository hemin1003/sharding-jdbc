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

package io.shardingjdbc.transaction.datasource.impl;

import io.shardingjdbc.transaction.constants.TransactionLogDataSourceType;
import io.shardingjdbc.transaction.datasource.TransactionLogDataSource;

import javax.sql.DataSource;

/**
 * Transaction log data source in memory.
 * 
 * @author caohao
 */
public class MemoryTransactionLogDataSource implements TransactionLogDataSource {
    
    @Override
    public TransactionLogDataSourceType getType() {
        return TransactionLogDataSourceType.MEMORY;
    }
    
    //TODO return memory's map
    @Override
    public DataSource getDataSource() {
        throw new UnsupportedOperationException();
    }
}
