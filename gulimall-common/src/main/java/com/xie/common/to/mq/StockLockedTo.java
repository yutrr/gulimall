package com.xie.common.to.mq;

/**
 * @title: StockLockedTo
 * @Author Xie
 * @Date: 2023/2/22 22:46
 * @Version 1.0
 */

/**
 *  锁定库存成功，往延时队列存入 工作单to 对象
 *  wms_ware_order_task
 * @author zr
 * @date 2021/12/29 15:07
 */
public class StockLockedTo {

    /** 库存工作单的id **/
    private Long id;

    /** 库存单详情 wms_ware_order_task_detail**/
    private StockDetailTo detailTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockDetailTo getDetailTo() {
        return detailTo;
    }

    public void setDetailTo(StockDetailTo detailTo) {
        this.detailTo = detailTo;
    }
}
