package com.mkolongo.product_shop.domain.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends NamedEntity {
}
