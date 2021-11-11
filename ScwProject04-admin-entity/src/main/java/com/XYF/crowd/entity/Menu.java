package com.XYF.crowd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.ArrayList;
import java.util.List;

@TableName(value = "t_menu")
public class Menu extends Model<Menu> {

    @TableId(value = "id",
            type = IdType.AUTO)
    private Integer id;

//    父节点的id
    private Integer pid;

//    节点名称
    private String name;

//    节点附带的url地址，是将来点击菜单时要跳转的地址
    private String url;

//    节点图标的样式
    private String icon;

    //存放菜单的子节点，初始化是为了避免空指针异常
    @TableField(exist = false) //解决在输出数据时出现mybaits-plus无法读取非映射字段的错误
    private List<Menu> children = new ArrayList<>();

    //判断菜单是否打开,默认为true,可以不设等于true
    @TableField(exist = false)
    private boolean open = true;

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Menu() {
    }



    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", open=" + open +
                '}';
    }
}