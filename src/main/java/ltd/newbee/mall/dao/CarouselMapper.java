package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.Carousel;
import ltd.newbee.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarouselMapper {
    /**
     * 删除一条记录
     * @param carouselId
     * @return
     */
    int deleteByPrimaryKey(Integer carouselId);

    /**
     * 保存一条新记录
     * @param record
     * @return
     */
    int insert(Carousel record);

    /**
     * 保存一条新记录
     * @param record
     * @return
     */
    int insertSelective(Carousel record);

    /**
     * 根据主键查询记录
     * @param carouselId
     * @return
     */
    Carousel selectByPrimaryKey(Integer carouselId);

    /**
     * 修改记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Carousel record);

    /**
     * 修改记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(Carousel record);

    /**
     * 查询分页数据
     * @param pageUtil
     * @return
     */
    List<Carousel> findCarouselList(PageQueryUtil pageUtil);

    /**
     * 查询总数
     * @param pageUtil
     * @return
     */
    int getTotalCarousels(PageQueryUtil pageUtil);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteBatch(Integer[] ids);

    /**
     * 查询固定数量的记录
     * @param number
     * @return
     */
    List<Carousel> findCarouselsByNum(@Param("number") int number);
}
