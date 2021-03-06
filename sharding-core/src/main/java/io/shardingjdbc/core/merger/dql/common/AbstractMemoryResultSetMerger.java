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

package io.shardingjdbc.core.merger.dql.common;

import io.shardingjdbc.core.merger.ResultSetMerger;
import io.shardingjdbc.core.util.SQLUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.util.Calendar;
import java.util.Map;

/**
 * 内存归并结果集.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public abstract class AbstractMemoryResultSetMerger implements ResultSetMerger {
    
    private final Map<String, Integer> labelAndIndexMap;
    
    @Setter
    private MemoryResultSetRow currentResultSetRow;
    
    private boolean wasNull;
    
    @Override
    public Object getValue(final int columnIndex, final Class<?> type) throws SQLException {
        if (Blob.class == type || Clob.class == type || Reader.class == type || InputStream.class == type || SQLXML.class == type) {
            throw new SQLFeatureNotSupportedException();
        }
        Object result = currentResultSetRow.getCell(columnIndex);
        wasNull = null == result;
        return result;
    }
    
    @Override
    public Object getValue(final String columnLabel, final Class<?> type) throws SQLException {
        if (Blob.class == type || Clob.class == type || Reader.class == type || InputStream.class == type || SQLXML.class == type) {
            throw new SQLFeatureNotSupportedException();
        }
        Object result = currentResultSetRow.getCell(labelAndIndexMap.containsKey(columnLabel) ? labelAndIndexMap.get(columnLabel) : labelAndIndexMap.get(SQLUtil.getExactlyValue(columnLabel)));
        wasNull = null == result;
        return result;
    }
    
    @Override
    public Object getCalendarValue(final int columnIndex, final Class<?> type, final Calendar calendar) {
        // TODO 时间相关取值未实现calendar模式
        Object result = currentResultSetRow.getCell(columnIndex);
        wasNull = null == result;
        return result;
    }
    
    @Override
    public Object getCalendarValue(final String columnLabel, final Class<?> type, final Calendar calendar) {
        // TODO 时间相关取值未实现calendar模式
        Object result = currentResultSetRow.getCell(labelAndIndexMap.get(columnLabel));
        wasNull = null == result;
        return result;
    }
    
    @Override
    public InputStream getInputStream(final int columnIndex, final String type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public InputStream getInputStream(final String columnLabel, final String type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public boolean wasNull() {
        return wasNull;
    }
}
