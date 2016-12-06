package com.sunnanjun.idclass;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/2.
 * 联合主键方式二
 */
@Entity
@IdClass(PK.class)
public class Login
{
    @Id
    private String system;

    @Id
    private String username;

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
    public String getSystem()
    {
        return system;
    }

    public void setSystem(String system)
    {
        this.system = system;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
