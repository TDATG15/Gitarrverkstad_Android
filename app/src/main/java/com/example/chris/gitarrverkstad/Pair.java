package com.example.chris.gitarrverkstad;

/**
 * Created by stefa_000 on 2017-03-05.
 */

public class Pair<CustomType> {
    private CustomType type;
    private int id;
    public Pair(CustomType type, int id){
        this.type = type;
        this.id = id;
    }
    public CustomType getType(){ return type; }
    public int getId(){ return id; }
    public void setType(CustomType type){ this.type = type; }
    public void setId(int id){ this.id = id; }
}