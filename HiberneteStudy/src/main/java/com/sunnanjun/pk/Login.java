package com.sunnanjun.pk;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/2.
 * 联合主键方式二
 */
@Entity
public class Login
{
    @EmbeddedId
    private PK pk;

    @Column(name = "des")
    private String descripion;

    public String getDescripion()
    {
        return descripion;
    }

    public void setDescripion(String descripion)
    {
        this.descripion = descripion;
    }
    public PK getPk()
    {
        return pk;
    }

    public void setPk(PK pk)
    {
        this.pk = pk;
    }

}
