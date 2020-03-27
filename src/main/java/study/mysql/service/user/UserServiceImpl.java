package study.mysql.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.mysql.mapper.user.userbase.UserBaseMapper;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseMapper userBaseMapper;
}
