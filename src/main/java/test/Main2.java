package test;

import java.lang.reflect.Method;

/**
 * @author fatih
 */
public class Main2 {
    public static void main(String[] args)
    {
        try
        {
            // Loading the class and creating an object
            ClassLoader l = ClassLoader.getSystemClassLoader();
            Class<?> c = l.loadClass("java.util.Random");
            Object o = c.getDeclaredConstructor().newInstance();
            Method m = c.getMethod("nextInt", int.class);

            // Invoking the method: o.nextInt(100)
            Object r = m.invoke(o, 100);
            System.out.println("Random number between 0 and 99: "+r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

