package com.XYF.crowd.entity;

/**
 * @username 熊一飞
 */

public class RemoveVO {

    private  Integer adminId;
    private Integer pageNum;

    @Override
    public String toString() {
        return "RemoveVO{" +
                "adminId=" + adminId +
                ", pageNum=" + pageNum +
                '}';
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public RemoveVO() {
    }

    public RemoveVO(Integer adminId, Integer pageNum) {
        this.adminId = adminId;
        this.pageNum = pageNum;
    }
}
