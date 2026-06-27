package com.JIMS.integration.entity;

public class TopBrokeCauseDTO {
    private String category;   // "elec" | "mech" | "hydr"
    private String item;       // e.g. "Panel / Wiring"
    private int    count;

    public TopBrokeCauseDTO() {}

    public TopBrokeCauseDTO(String category, String item, int count) {
        this.category = category;
        this.item     = item;
        this.count    = count;
    }

    public String getCategory() { return category; }
    public void   setCategory(String v) { category = v; }

    public String getItem() { return item; }
    public void   setItem(String v) { item = v; }

    public int    getCount() { return count; }
    public void   setCount(int v) { count = v; }
}