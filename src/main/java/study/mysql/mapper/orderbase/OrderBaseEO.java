package study.mysql.mapper.orderbase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import study.mysql.support.db.BaseEO;

import java.util.Date;

@Getter
@Setter
@ToString
@Alias("OrderBaseEO")
public class OrderBaseEO extends BaseEO {

    private Long rbId;

    private Long rbOrderId;

    private Long rbPbId;

    private Long rbUbId;

    private Date rbBeginTime;

    private Date rbBeginEnd;
}
