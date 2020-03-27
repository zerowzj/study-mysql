package study.mysql.support.db;

/**
 * 基础Dao
 *
 * @author wangzhj
 */
public interface BaseMapper<PK, E extends BaseEO> {

    /**
     * 新增实体
     *
     * @param entity 实体信息
     * @return int 新增数量
     */
    int insert(final E entity);

    /**
     * 删除实体
     *
     * @param id 主键
     * @return int 删除数量
     */
    int delete(final PK id);

    /**
     * 更新实体
     *
     * @param entity 实体信息
     * @return int 更新数量
     */
    int update(final E entity);

    /**
     * 获取实体
     *
     * @param id 主键
     * @return E 实体信息
     */
    E get(final PK id);
}
