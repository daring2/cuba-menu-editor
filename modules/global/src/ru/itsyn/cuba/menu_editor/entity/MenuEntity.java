package ru.itsyn.cuba.menu_editor.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NamePattern("%s|name")
@Table(name = "MENU_MENU")
@Entity(name = "menu_MenuEntity")
public class MenuEntity extends StandardEntity {
    private static final long serialVersionUID = 8060389614924374355L;

    public static final String DEFAULT_CODE = "default";

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    protected String name;

    @Column(name = "CODE", unique = true)
    protected String code;

    @Column(name = "DESCRIPTION", length = 2000)
    protected String description;

    @Lob
    @Column(name = "CONFIG")
    protected String config;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}