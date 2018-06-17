package service.bean;

/**
 * yamlgen
 * Created by wangzhilei3 on 2018/1/9.
 */
public class ServiceTerm {
    private String value;
    private String description;
    private TestBean testBean;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceTerm{");
        sb.append("value='").append(value).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

