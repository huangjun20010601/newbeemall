package ltd.newbee.mall.controller.admin;

import javafx.beans.binding.ObjectBinding;
import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.common.NewBeeMallCategoryLevelEnum;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.entity.GoodsCategory;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.service.NewBeeMallCategoryService;
import ltd.newbee.mall.service.NewBeeMallGoodsService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class NewBeeMallGoodsController {

    @Resource
    private NewBeeMallCategoryService newBeeMallCategoryService;
    @Resource
    private NewBeeMallGoodsService newBeeMallGoodsService;

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request){
        request.setAttribute("path","edit");
        //查询所有一级分类
        List<GoodsCategory> firstLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)){
            List<GoodsCategory> secondLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()),NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)){
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()),NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories",firstLevelCategories);
                request.setAttribute("secondLevelCategories",secondLevelCategories);
                request.setAttribute("thirdLevelCategories",thirdLevelCategories);
                request.setAttribute("path","goods-edit");
                return "admin/newbee_mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @RequestMapping(value = "/goods/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody NewBeeMallGoods newBeeMallGoods){
        if (StringUtils.isEmpty(newBeeMallGoods.getGoodsName())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsIntro())
        || StringUtils.isEmpty(newBeeMallGoods.getTag())
        || Objects.isNull(newBeeMallGoods.getOriginalPrice())
        || Objects.isNull(newBeeMallGoods.getGoodsCategoryId())
        || Objects.isNull(newBeeMallGoods.getSellingPrice())
        || Objects.isNull(newBeeMallGoods.getStockNum())
        || Objects.isNull(newBeeMallGoods.getGoodsSellStatus())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsCoverImg())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsDetailContent())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallGoodsService.saveNewBeeMallGoods(newBeeMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request,@PathVariable("goodsId") Long goodsId){
        request.setAttribute("path","edit");
        NewBeeMallGoods newBeeMallGoods = newBeeMallGoodsService.getNewBeeMallGoodsById(goodsId);
        if (newBeeMallGoods == null){
            return "error/error_400";
        }
        if (newBeeMallGoods.getGoodsCategoryId() > 0){
            if (newBeeMallGoods.getGoodsCategoryId() != null || newBeeMallGoods.getGoodsCategoryId() > 0){
                //有分类字段则查询相关分类数据并返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = newBeeMallCategoryService.getGoodsCategoryById(newBeeMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类id,不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel()){
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L),NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询在当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()),NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = newBeeMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null){
                        //根据parentId查询在当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()),NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firstCategory = newBeeMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firstCategory != null){
                            request.setAttribute("firstLevelCategories",firstLevelCategories);
                            request.setAttribute("secondLevelCategories",secondLevelCategories);
                            request.setAttribute("thirdLevelCategories",thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId",firstCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId",secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId",currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (newBeeMallGoods.getGoodsCategoryId() == 0){
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L),NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)){
                //查询在一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()),NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)){
                    //查询在二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()),NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories",firstLevelCategories);
                    request.setAttribute("secondLevelCategories",secondLevelCategories);
                    request.setAttribute("thirdLevelCategories",thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods",newBeeMallGoods);
        request.setAttribute("path","goods-edit");
        return "admin/newbee_mall_goods_edit";
    }

    /**
     * 修改
     * @param newBeeMallGoods
     * @return
     */
    @RequestMapping(value = "/goods/update",method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody NewBeeMallGoods newBeeMallGoods){
        if (Objects.isNull(newBeeMallGoods.getGoodsId())
            || StringUtils.isEmpty(newBeeMallGoods.getGoodsName())
            || StringUtils.isEmpty(newBeeMallGoods.getGoodsIntro())
            || StringUtils.isEmpty(newBeeMallGoods.getTag())
            || Objects.isNull(newBeeMallGoods.getOriginalPrice())
            || Objects.isNull(newBeeMallGoods.getSellingPrice())
            || Objects.isNull(newBeeMallGoods.getGoodsCategoryId())
            || Objects.isNull(newBeeMallGoods.getStockNum())
            || Objects.isNull(newBeeMallGoods.getGoodsSellStatus())
            || StringUtils.isEmpty(newBeeMallGoods.getGoodsCoverImg())
            || StringUtils.isEmpty(newBeeMallGoods.getGoodsDetailContent())){
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = newBeeMallGoodsService.updateNewBeeMallGoods(newBeeMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(result);
        }
    }

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request){
        request.setAttribute("path","newbee_mall_goods");
        return "admin/newbee_mall_goods";
    }

    /**
     * 列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/goods/list",method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallGoodsService.getNewBeeMallGoodsPage(pageUtil));
    }

    @RequestMapping(value = "/goods/status/{sellStatus}",method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids,@PathVariable("sellStatus") int sellStatus){
        if (ids.length < 1){
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN){
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (newBeeMallGoodsService.batchUpdateSellStatus(ids, sellStatus)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("修改失败！");
        }
    }
}
