package com.gtu.example.springdata.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@Cacheable
//@Entity(name = "ImageGroup")
//@Table(name = "image_group", indexes = { @Index(name = "img_grp_tenant_name_idx", columnList = "tenant,name"),
//        @Index(name = "img_grp_tenant_idx", columnList = "tenant") }, uniqueConstraints = {
//                @UniqueConstraint(name = "img_grp_tenant_name_uq", columnNames = { "tenant", "name" }), })
//@SequenceGenerator(name = "image_group_id_sequence", sequenceName = "image_group_id_seq", initialValue = 1)
public class ImageGroupDO {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_group_id_sequence")
    @Column(name = "id")
    private long id;

    /** The name. */
    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /** The description. */
    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    /** The active. */
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

//    /** The tenant do. */
//    @NotNull
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "tenant", foreignKey = @ForeignKey(name = "img_grp_tenant_fk"))
//    private TenantDO tenantDo;
//
//    /** The images. */
//    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//    @JoinTable(name = "image_group_image", joinColumns = @JoinColumn(name = "image_group", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "image", referencedColumnName = "id"), foreignKey = @ForeignKey(name = "img_grp_img_image_group_fk"), inverseForeignKey = @ForeignKey(name = "img_grp_img_image_fk"), indexes = {
//            @Index(name = "img_grp_img_image_group_fk_idx", columnList = "image_group"), @Index(name = "img_grp_img_image_fk_idx", columnList = "image") }, uniqueConstraints = {
//                    @UniqueConstraint(name = "img_grp_img_img_grp_img_uq", columnNames = { "image_group", "image" }) })
//    private Set<ImageDO> images = new HashSet<>();

    /** The created at. */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    /** The modified at. */
    @NotNull
    @Column(name = "modified_at", nullable = false)
    private Date modifiedAt;

    /**
     * On create.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.modifiedAt = new Date();
    }

    /**
     * On update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = new Date();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active
     *            the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the created at.
     *
     * @return the created at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt
     *            the new created at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the modified at.
     *
     * @return the modified at
     */
    public Date getModifiedAt() {
        return modifiedAt;
    }

    /**
     * Sets the modified at.
     *
     * @param modifiedAt
     *            the new modified at
     */
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}