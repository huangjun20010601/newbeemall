package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.StockNumDTO;
import ltd.newbee.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallGoodsMapper {
    /**
     * 保存一条新纪录
     * @param record
     * @return
     */
    int insertSelective(NewBeeMallGoods record);

    /**
     * 根据商品id查询商品
     * @param goodsId
     * @return
     */
    NewBeeMallGoods selectByPrimaryKey(Long goodsId);

    /**
     * 修改一条记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(NewBeeMallGoods record);

    /**
     * 查询分页数据
     * @param pageUtil
     * @return
     */
    List<NewBeeMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    /**
     * 查询总数
     * @param pageUtil
     * @return
     */
    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    /**
     * 批量修改记录
     * @param goodsIds
     * @param sellStatus
     * @return
     */
    int batchUpdateSellStatus(@Param("goodsIds") Long[] goodsIds,@Param("sellStatus") int sellStatus);

    List<NewBeeMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<NewBeeMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int updateStockNum(@Param("stockNumDTOS")List<StockNumDTO> stockNumDTOS);
}
