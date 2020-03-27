package study.mysql.mapper.user.userbase;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import study.mysql.support.db.BaseMapper;

import java.util.List;

public interface UserBaseMapper extends BaseMapper<Long, UserBaseEO> {

    List<UserBaseEO> getUserBaseLt();

    List<UserBaseEO> getShardingUserBaseLt(int index, int total);

    Page<UserBaseEO> getUserBasePageLt(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
