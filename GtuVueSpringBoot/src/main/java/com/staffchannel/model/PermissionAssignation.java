///**
// * 
// */
//package com.staffchannel.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//
///**
// * @author Walalala
// *
// */
//@Entity
//@IdClass(PermissionAssignation.class)
//public class PermissionAssignation implements Serializable {
//
//     @Id
//     @ManyToOne
//     @JoinColumn(name="id")
//     private User user;
//
//     @Id
//     @ManyToOne
//     @JoinColumn(name="role_id")
//     private UserProfile userProfile;
//
//     @Id
//     @ManyToOne
//     @JoinColumn(name="menu_id")
//     private MenuList menuList;
//     
//}
