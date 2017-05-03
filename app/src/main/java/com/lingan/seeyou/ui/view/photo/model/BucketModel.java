package com.lingan.seeyou.ui.view.photo.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA. R
 * Date: 14-7-14
 */
public class BucketModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5203237872983990786L;
	public long Id;
    public String Name;
    public String Cover;
    public int PhotoCount;

    public BucketModel(long id, String name, String cover, int count) {
        Name = name;
        Id = id;
        Cover = cover;
        PhotoCount = count;
    }

    public BucketModel(long id, String name, String cover) {
        Name = name;
        Id = id;
        Cover = cover;
    }

    @Override
    public String toString() {
        return "BucketModel{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Cover='" + Cover + '\'' +
                ", PhotoCount=" + PhotoCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof BucketModel){
            return Id == ((BucketModel) o).Id;
        }else{
            return false;
        }
    }
}
