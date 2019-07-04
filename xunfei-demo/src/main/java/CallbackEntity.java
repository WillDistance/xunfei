

import java.lang.reflect.Method;

/**
 * 
 * @描述: 回调函数对象实体
 * @版权: Copyright (c) 2019 
 * @公司: 思迪科技 
 * @作者: 严磊
 * @版本: 1.0 
 * @创建日期: 2019年6月24日 
 * @创建时间: 下午2:09:18
 */
public class CallbackEntity
{
    private Class executeClass = null; //方法所属类
    private String methodName = null; //执行方法
    private Object[] methodParams = null; //方法执行参数
    private Class<?>[] parameterTypes = null; //方法执行形参类型
    
    public CallbackEntity(Class executeClass, String methodName, Object[] methodParams, Class<?>[] parameterTypes)
    {
        this.executeClass = executeClass;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.parameterTypes = parameterTypes;
    }
    /**
     * 
     * @描述：获取Method对象
     * @作者：严磊
     * @时间：2019年6月24日 下午2:42:20
     * @return
     * @throws Exception
     */
    public Method getMethod() throws Exception
    {
        return executeClass.getMethod(methodName, parameterTypes);
    }
    
    public Class getExecuteClass()
    {
        return executeClass;
    }
    public void setExecuteClass(Class executeClass)
    {
        this.executeClass = executeClass;
    }
    public String getMethodName()
    {
        return methodName;
    }
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
    public Object[] getMethodParams()
    {
        return methodParams;
    }
    public void setMethodParams(Object[] methodParams)
    {
        this.methodParams = methodParams;
    }
    public Class<?>[] getParameterTypes()
    {
        return parameterTypes;
    }
    public void setParameterTypes(Class<?>[] parameterTypes)
    {
        this.parameterTypes = parameterTypes;
    }
    
}
