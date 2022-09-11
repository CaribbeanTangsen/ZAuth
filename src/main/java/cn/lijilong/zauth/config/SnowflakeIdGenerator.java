package cn.lijilong.zauth.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SnowflakeIdGenerator implements IdentifierGenerator {


    private final Snowflake snowflake = IdUtil.getSnowflake();

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return snowflake.nextId();
    }
}
