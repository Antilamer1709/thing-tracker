package com.antilamer.thingTracker.model;

import com.antilamer.thingTracker.dto.ExpenseDTO;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "expense_to_expense_type_dict", schema = "thing_tracker",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "expense_type_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<ExpenseTypeDictEntity> expenseTypeDict;


    public static class Builder {

        private ExpenseEntity expense;

        public Builder() {
            this.expense = new ExpenseEntity();
        }

        public Builder fromDTO(ExpenseDTO expenseDTO) {
            expense.setPrice(expenseDTO.getPrice());
            expense.setComment(expenseDTO.getComment());
            if (expenseDTO.getDate() != null)
                expense.setDate(expenseDTO.getDate());
            else
                expense.setDate(LocalDateTime.now());
            return this;
        }

        public Builder withUser(UserEntity user) {
            expense.user = user;
            return this;
        }


        public ExpenseEntity build() {
            return expense;
        }
    }

}
