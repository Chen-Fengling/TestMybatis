import com.mybatis.bean.Employee;
import com.mybatis.mapper.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;


import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenrun on 2017/6/21.
 */
public class MybatisTest {
    /**
     * 方法一：
     * 1、创建Mybatis全局配置文件,有数据源及运行环境等信息
     * 2、创建SqlSessionFactory，配置了每一个sql，及sql的封装信息
     *
     * @throws IOException
     */
    @Test
    public void testMain() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //3、获取SqlSession实例，能直接执行已经映射了的sql语句
        //一个SqlSession就代表与数据库的一次会话，用完关闭
        //SqlSession和Connection一样都是非线程安全的，每次使用应该创建新的SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // selectOne(String s,Object o)
        //参数1：sql的唯一标识
        try {
            //4、需要将映射文件注册在全局配置文件里
            //使用名称空间+唯一标识指定执行哪一个sql
            //以下方法为Mybatis以前的版本
            Employee employee = sqlSession.selectOne("com.mybatis.mapper.EmployeeMapper.selectEmployeeById", 1);
            System.out.println(employee);
        }finally {
            //5、关闭session
            sqlSession.close();
        }
    }

    /**
     * 方法二：利用接口动态绑定实现sql查询，现阶段用的最多的方法
     * 通过代理对象实现
     * @throws Exception
     */
    @Test
    public void interfaceTest()throws Exception{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //Mapper没有实现类，但是Mybatis会为接口生成一个代理对象，将接口和xml绑定起来
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

            System.out.println(employeeMapper.getClass());//返回一个代理对象
            Employee employee = employeeMapper.selectEmployeeById(1);
            System.out.println(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

