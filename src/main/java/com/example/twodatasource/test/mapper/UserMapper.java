package com.example.twodatasource.test.mapper;

import com.example.twodatasource.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2021-04-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public User selectTop1();
}
