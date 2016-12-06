package com.sunnanjun.pk;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/2.
 */
@Embeddable
public class PK implements Serializable
{
    private String system;
    private String username;

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
