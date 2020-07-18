package com.common;

public enum NoodleGenre {
    none(0,"----"),
    Shio(1,"塩"),
    Shoyu(2,"醬油"),
    Miso(3,"味噌"),
    Tonkotsu(4,"豚骨"),
    Tsukemen(5,"つけ麺"),
    Tantanmen(6,"担々麵"),
    Mazesoba(7,"まぜそば");

    private Integer labelNum;
    private String japaneseName;

    NoodleGenre(Integer labelNum, String japanese) {
        this.labelNum = labelNum;
        this.japaneseName = japanese;
    }

    public Integer getLabelNum() {
        return labelNum;
    }

    public void setLabelNum(Integer labelNum) {
        this.labelNum = labelNum;
    }

    public String getJapaneseName() {
        return japaneseName;
    }

    public void setJapaneseName(String japaneseName) {
        this.japaneseName = japaneseName;
    }
}
