package ltd.newbee.mall.service;

import ltd.newbee.mall.controller.vo.NewBeeMallOrderDetailVO;
import ltd.newbee.mall.controller.vo.NewBeeMallOrderItemVO;
import ltd.newbee.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.entity.NewBeeMallOrder;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;

import java.util.List;

public interface NewBeeMallOrderService {

    PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil);

    String updateOrderInfo(NewBeeMallOrder newBeeMallOrder);

    String checkDone(Long[] ids);

    String checkOut(Long[] ids);

    String closeOrder(Long[] ids);

    String saveOrder(NewBeeMallUserVO user, List<NewBeeMallShoppingCartItemVO> myShoppingCartItems);

    NewBeeMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    NewBeeMallOrder getNewBeeMallOrderByOrderNo(String orderNo);

    PageResult getMyOrders(PageQueryUtil pageUtil);

    String cancelOrder(String orderNo, Long userId);

    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<NewBeeMallOrderItemVO> getOrderItems(Long id);
}
