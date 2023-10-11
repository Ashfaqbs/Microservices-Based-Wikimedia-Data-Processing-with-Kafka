package com.main.springboot.cosumer.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import javax.persistence.*;


//Note: //since the data from wikimedia is not defined or is not proper to be assigned to a particular class/object
//    we will save it as lob large object and have id which spring boot will generate
@Entity
@Table( name = "Wikimedia_recent_change")
@Getter
@Setter
public class WikimediaData {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;
@Lob
private String wikiEventData;



}
