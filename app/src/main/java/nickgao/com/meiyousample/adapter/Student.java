package nickgao.com.meiyousample.adapter;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/5/12.
 */

public class Student {
    public int age;
    public String name;
    List<Course> course;


    public void setCourse(List<Course> course) {
        this.course = course;
    }

    public List<Course> getCourse() {
        return course;
    }
    public Student(int age,String name,List<Course> courses) {
        this.age = age;
        this.name = name;
        this.course = courses;
    }

    public void printStr(String name) {
        System.out.print("====name="+name+"this.age="+this.age);
    }
}
