package qwestgroup.model;




public class Purchase {
    // 03126000-2 (03 - section), (1 - group), (2 - clas), (6 - category)
    private String code;
    private String name;
    private int id;
    private int section;
    private int group;
    private int clas;
    private int category;

    public int getClas() {
        return clas;
    }

    public void setClas(int clas) {
        this.clas = clas;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getGroup() {

        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return code+"  "+ name;
    }
}
