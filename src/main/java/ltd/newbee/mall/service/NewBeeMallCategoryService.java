package ltd.newbee.mall.service;

import ltd.newbee.mall.controller.vo.NewBeeMallIndexCategoryVO;
import ltd.newbee.mall.controller.vo.SearchPageCategoryVO;
import ltd.newbee.mall.entity.GoodsCategory;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;

import java.util.List;

public interface NewBeeMallCategoryService {

    /**
     * 查询后台管理系统分类分页数据
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    /**
     * 新增一条记录
     * @param goodsCategory
     * @return
     */
    String saveCategory(GoodsCategory goodsCategory);

    /**
     * 修改一条分类记录
     * @param goodsCategory
     * @return
     */
    String updateGoodsCategory(GoodsCategory goodsCategory);

    /**
     * 根据主键查询分类记录
     * @param id
     * @return
     */
    GoodsCategory getGoodsCategoryById(Long id);

    /**
     * 批量删除分类数据
     * @param ids
     * @return
     */
    Boolean deleteBatch(Integer[] ids);

    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds,int categoryLevel);

    /**
     * 返回分类数据（首页调用）
     * @return
     */
    List<NewBeeMallIndexCategoryVO> getCategoriesForIndex();

    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);
}
