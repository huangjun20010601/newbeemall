package ltd.newbee.mall.dao;

import ltd.newbee.mall.entity.MallUser;
import ltd.newbee.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {

    int insertSelective(MallUser record);

    MallUser selectByLoginName(String loginName);

    MallUser selectByLoginNameAndPassword(@Param("loginName") String loginName, @Param("password") String password);

    List<MallUser> findMallUserList(PageQueryUtil pageUtil);

    int getTotalMallUsers(PageQueryUtil pageUtil);

    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);

    MallUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(MallUser record);
}
