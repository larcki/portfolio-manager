package com.nordcomet.pflio.asset.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private ParserOptions parserOptions;

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


    public List<Tag> getTags() {
        return tags;
    }

    public BigDecimal getProportionOfTag(Tags tag) {
        return getTags().stream()
                .filter(assetTag -> assetTag.getName() == tag)
                .findFirst().map(Tag::getProportion)
                .orElse(BigDecimal.ZERO);
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ParserOptions getParserOptions() {
        return parserOptions;
    }

    public void setParserOptions(ParserOptions parserOptions) {
        this.parserOptions = parserOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(id, asset.id) &&
                Objects.equals(name, asset.name) &&
                Objects.equals(parserOptions, asset.parserOptions) &&
                Objects.equals(tags, asset.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parserOptions, tags);
    }
}
