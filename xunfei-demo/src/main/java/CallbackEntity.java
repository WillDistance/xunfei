

import java.lang.reflect.Method;

/**
 * 
 * @����: �ص���������ʵ��
 * @��Ȩ: Copyright (c) 2019 
 * @��˾: ˼�ϿƼ� 
 * @����: ����
 * @�汾: 1.0 
 * @��������: 2019��6��24�� 
 * @����ʱ��: ����2:09:18
 */
public class CallbackEntity
{
    private Class executeClass = null; //����������
    private String methodName = null; //ִ�з���
    private Object[] methodParams = null; //����ִ�в���
    private Class<?>[] parameterTypes = null; //����ִ���β�����
    
    public CallbackEntity(Class executeClass, String methodName, Object[] methodParams, Class<?>[] parameterTypes)
    {
        this.executeClass = executeClass;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.parameterTypes = parameterTypes;
    }
    /**
     * 
     * @��������ȡMethod����
     * @���ߣ�����
     * @ʱ�䣺2019��6��24�� ����2:42:20
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
