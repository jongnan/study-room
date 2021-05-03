package jpabook.start;

import javax.persistence.*;

@Entity
@Table(name="MEMBER")
public class Member {
    @Id
    @Column(name="ID")
    private String id;

    @Column(name="NAME")
    private String userName;

    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
