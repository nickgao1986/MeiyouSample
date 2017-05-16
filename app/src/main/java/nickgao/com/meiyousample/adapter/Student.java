package nickgao.com.meiyousample.adapter;

/**
 * Created by gaoyoujian on 2017/5/12.
 */

public class Student {
    public int age;
    public String name;

    public Student(int age,String name) {
        this.age = age;
        this.name = name;
    }


    public void printStr(String name) {
        System.out.print("====name="+name+"this.age="+this.age);
    }
}
