<<<<<<< HEAD
package codefusion.softwareproject1.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
=======


package codefusion.softwareproject1.Models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.*;

import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
>>>>>>> b4b6a66fde8ae60f6564b68efc56075bf548027a
public class CategoryClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private Long id;
    
    private String name;
    
    private String description;
=======
    private int id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizClass> quizzes;
>>>>>>> b4b6a66fde8ae60f6564b68efc56075bf548027a
}

