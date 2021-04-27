package top.zbsong.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zbsong.community.mapper.UserMapper;
import top.zbsong.community.model.User;
import top.zbsong.community.model.UserExample;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> dbUserList = userMapper.selectByExample(userExample);
        if (dbUserList.isEmpty()) {
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.insert(user);
        } else {
            // 更新
//            User dbUser = dbUserList.get(0);
//            dbUser.setGmtModified(System.currentTimeMillis());
//            dbUser.setAvatarUrl(user.getAvatarUrl());
//            dbUser.setToken(user.getToken());
//            userMapper.updateByPrimaryKeySelective(user);

            user.setGmtModified(System.currentTimeMillis());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andAccountIdEqualTo(user.getAccountId());
            userMapper.updateByExampleSelective(user, example);
        }
    }
}
