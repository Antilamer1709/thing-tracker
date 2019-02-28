package com.antilamer.thingTracker.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "expense", schema = "thing_tracker")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "price")
    private Integer price;

    @Column(name = "comment")
    private String comment;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "expense_to_expense_type_dict",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "expense_type_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<ExpenseTypeDictEntity> expenseTypeDict;

}
