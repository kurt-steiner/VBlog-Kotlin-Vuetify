package com.steiner.vblog.util.typehandler;

import com.steiner.vblog.model.article.ArticleStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ArticleStatus.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class ArticleStatusTypeHandler extends BaseTypeHandler<ArticleStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ArticleStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.code);
    }

    @Override
    public ArticleStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return ArticleStatus.fromCode(code);
    }

    @Override
    public ArticleStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return ArticleStatus.fromCode(code);
    }

    @Override
    public ArticleStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return ArticleStatus.fromCode(code);
    }
}
