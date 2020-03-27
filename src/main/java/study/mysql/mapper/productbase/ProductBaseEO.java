package study.mysql.mapper.productbase;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import study.mysql.support.db.BaseEO;

import java.util.Date;

@Getter
@Setter
@ToString
@Alias("ProductBaseEO")
public class ProductBaseEO extends BaseEO {

    private Long pbId;

    private String pbName;

    private Date pbBeginTime;

    private Date pbEndTime;
}
