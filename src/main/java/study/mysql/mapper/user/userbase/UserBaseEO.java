package study.mysql.mapper.user.userbase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import study.mysql.support.db.BaseEO;

import java.util.Date;

@Getter
@Setter
@ToString
@Alias("UserBaseEO")
public class UserBaseEO extends BaseEO {

    private Long ubId;

    private Long ubUserId;

    private String ubLoginName;

    private String ubLoginPwd;

    private String ubType;

    private Date ubBeginTime;

    private Date ubEndTime;
}
