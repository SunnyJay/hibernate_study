import com.sunnanjun.association.GroupEntity;
import com.sunnanjun.association.UserEntity;
import com.sunnanjun.pk.Login;
import com.sunnanjun.pk.PK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.*/
public class TestJPA
{
    private static EntityManagerFactory entityManagerFactory  = null;
    private static EntityManager entityManager = null;

    @BeforeClass
    public static void setUp() //注意一定要static
    {

        entityManagerFactory  = Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterClass
    public static void tearDown()
    {
        entityManagerFactory.close();
    }

    /*
    测试插入(使用API)
     */
    @Ignore
    @Test
    public void testInsert1()//注意一定要public void
    {
        entityManager.getTransaction().begin();

        UserEntity user = new UserEntity();
        user.setName("sunnanjun");
        user.setPassword("123456");
        //user.setCreateTime(new Date());//自动生成
        user.setExpireTime(new Date());

        GroupEntity group = new GroupEntity();
        group.setName("sinosun");

        user.setGroup(group);

        entityManager.persist(user);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
    测试JPQL的insert语句
     */
    @Ignore
    @Test
    public void testInsert2()
    {
        /*
        JPQL没有insert 语句
        HQL有
         */
        /*
            int insertedEntities = session.createQuery(
                                "insert into Partner (id, name) " +
                                "select p.id, p.name " +
                                "from Person p ")
                                .executeUpdate();
         */
    }


    /*
    测试查询(使用JPA语法)
     */
    @Ignore
    @Test
    public void testQuery1()
    {

        entityManager.getTransaction().begin();

        /*
        使用JPQL
        注意写法，jpa是setParameter
        JPQL中select必须要有
        select 时先写别名u，然后select u
        占位符最好用:id,不要用问号
        最后还有class类
         */
        List<UserEntity> user_list = entityManager.createQuery(
                "select u " + "from UserEntity u " + "where u.id = :id", UserEntity.class)
                .setParameter("id", 1)
                .getResultList();

        //或getSingleResult
        UserEntity user = entityManager.createQuery(
                "select u " + "from UserEntity u " + "where u.id = :id", UserEntity.class)
                .setParameter("id", 1)
                .getSingleResult();//如果超过一个值就会报错

        System.out.println("Using JPQL:" + user_list.get(0).getName());
        System.out.println("Using JPQL:" + user.getName());

        /*
        使用API
         */
        UserEntity user_api = entityManager.find(UserEntity.class, 1);
        System.out.println("Using API:" + user_api.getName());


        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
    测试查询(使用NamedQueries)
     */
    @Ignore
    @Test
    public void testQuery2()
    {

        /*
        这个必须写在实体上面
        @NamedQueries(
                @NamedQuery(
                        name = "get_user_by_name",
                        query = "select u from User u where name = :newname"
                )
        )
        */

        entityManager.getTransaction().begin();
        Query query = entityManager.createNamedQuery("get_user_by_name", UserEntity.class).setParameter("newname","sunnanjun");
        UserEntity user = (UserEntity) query.getSingleResult(); //注意getSingleResult的结果一定要要强制转换
        System.out.println("password is:" + user.getPassword());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
   测试查询(使用HQL)
    */
    @Ignore
    @Test
    public void testQuery3()
    {
        /*
        通过JPA获得Session的方式 unwrap
         */
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        Session session = entityManager.unwrap(Session.class);


        session.getTransaction().begin();

        //可以省略select
        List<UserEntity> user_list = session.createQuery("from UserEntity u where name = :newname")//这里name可以改成user_name，很奇怪
                .setParameter("newname", "snj").list();//注意这里是list

        System.out.println("password is:" + user_list.get(0).getPassword());
        session.getTransaction().commit();
        session.close();
    }

    /*
    测试更新
     */
    @Ignore
    @Test
    public void testUpdate()
    {

        entityManager.getTransaction().begin();

        //update语句不能为typedquery,所以后面不能加类型
        //注意这里的成员一定是类的属性，不是别名，即u.name 而不能是u.user_name
        int num = entityManager.createQuery("update UserEntity u " +
                "set u.name = :newname " +
                "where u.id = :id")
                .setParameter("newname", "snj")
                .setParameter("id", 1)
                .executeUpdate();//返回个数
        System.out.println("更新成功的个数为" + num);

        UserEntity userEntity = (UserEntity) entityManager.createQuery("select u from UserEntity u " +
                "where u.id = :id")
                .setParameter("id", 1)
                .getSingleResult();//getSingleResult一定要强制转换
        System.out.println("更改后的结果为：" + userEntity.getName());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
    测试删除
     */
    @Ignore
    @Test
    public void testDelete()
    {
        entityManager.getTransaction().begin();

        int num = entityManager.createQuery("delete from UserEntity u where u.name = :name")
                .setParameter("name", "sunnanjun")
                .executeUpdate();
        System.out.println("删除成功的个数:" + num);


        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
    测试原生SQL
     */
    //@Ignore
    @Test
    public void testSQL()
    {
        entityManager.getTransaction().begin();

        List<Object[]> list = entityManager.createNativeQuery("select * from UserEntity").getResultList();

        //注意这种的方式只能是Object
        for(Object[] user:list)
        {
            System.out.println("Id is "+  (Integer) user[0]);
            System.out.println("Name is " + (String) user[3]);
        }

        /*
        使用addEntity JPA貌似不能使用addScalar
        使用addEntity返回所有属性
        注意和上面的区别就是类型 这里是List<UserEntity>
         */
        List<UserEntity> list2 = entityManager
                .createNativeQuery("select * from UserEntity", UserEntity.class)
                .getResultList();

        for(UserEntity user:list2)
        {
            System.out.println("Id is " + user.getId());
            System.out.println("Name is " + user.getName());
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }



    /*
    测试pk复合主键
     */
    @Ignore
    @Test
    public void testPK()
    {
        entityManager.getTransaction().begin();

        Login login = new Login();
        login.setDescripion("Just a test");

        PK pk = new PK();
        pk.setSystem("win98");
        pk.setUsername("sunnanjun");

        login.setPk(pk);
        entityManager.persist(login);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*
测试idclass复合主键*/
    @Ignore
    @Test
    public void testIdClass()
    {
        entityManager.getTransaction().begin();

        com.sunnanjun.idclass.Login login = new com.sunnanjun.idclass.Login();
        login.setDescripion("Just a test for idclass");
        login.setSystem("win2000");
        login.setUsername("sunnanjun");

        entityManager.persist(login);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
