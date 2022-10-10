package ltd.newbee.mall.service;

import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;

public interface NewBeeMallGoodsService {

    /**
     * 添加商品
     * @param goods
     * @return
     */
    String saveNewBeeMallGoods(NewBeeMallGoods goods);

    /**
     * 获取商品详情
     * @param id
     * @return
     */
    NewBeeMallGoods getNewBeeMallGoodsById(Long id);

    /**
     * 修改商品信息
     * @param goods
     * @return
     */
    String updateNewBeeMallGoods(NewBeeMallGoods goods);

    /**
     * 后台分页
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 批量修改销售状态(上下架)
     * @param ids
     * @param sellStatus
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);
}
