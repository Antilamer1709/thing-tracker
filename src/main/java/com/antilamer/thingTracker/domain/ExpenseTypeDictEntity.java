package com.antilamer.thingTracker.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "expense_type_dict", schema = "thing_tracker")
public class ExpenseTypeDictEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "used_count")
    private Integer usedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "name")
    private String name;


    public void incrementUsedCounter() {
        usedCount++;
    }



    public static class Builder {

        private ExpenseTypeDictEntity expenseTypeDict;

        public Builder() {
            expenseTypeDict = new ExpenseTypeDictEntity();
            expenseTypeDict.usedCount = 0;
        }


        public Builder withUser(UserEntity user) {
            expenseTypeDict.user = user;
            return this;
        }

        public Builder withName(String name) {
            expenseTypeDict.name = name;
            return this;
        }


        public ExpenseTypeDictEntity build() {
            return expenseTypeDict;
        }
    }

}
