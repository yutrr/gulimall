package com.xie.common.constant;

import lombok.Data;

/**
 * @title: ProductConstant
 * @Author Xie
 * @Date: 2022/8/11 18:58
 * @Version 1.0
 */
public class ProductConstant {
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String msg;

        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
