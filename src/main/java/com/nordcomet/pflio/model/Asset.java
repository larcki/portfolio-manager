package com.nordcomet.pflio.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private String source;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "asset_tags",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(id, asset.id) &&
                Objects.equals(name, asset.name) &&
                Objects.equals(code, asset.code) &&
                Objects.equals(source, asset.source) &&
                Objects.equals(tags, asset.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, source, tags);
    }
}
