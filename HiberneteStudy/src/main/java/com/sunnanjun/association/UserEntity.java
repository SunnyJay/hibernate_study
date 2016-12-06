package com.sunnanjun.association;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Administrator on 2016/12/1.
 */
@NamedQueries(
        @NamedQuery(
                name = "get_user_by_name",
                query = "select u from UserEntity u where name = :newname"
        )
)
@Entity
public class UserEntity
{
    @Id
    @GeneratedValue( strategy = IDENTITY)
    private int id;

    //关联
    //注意写法,参考文档 @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id",
            foreignKey = @ForeignKey(name = "GROUP_ID")
    )
    private GroupEntity group;


    @Column(name = "user_name")
    private String name;

    @Column(name = "user_pass")
    private String password;

    @CreationTimestamp //注意这里使用这个 自动生成时间
    private Date createTime;

    @Temporal(TemporalType.DATE) //需要你手动插入，所以要写@Temporal(TemporalType.DATE)指定时间类型
    private Date expireTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public GroupEntity getGroup()
    {
        return group;
    }

    public void setGroup(GroupEntity group)
    {
        this.group = group;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }
}
