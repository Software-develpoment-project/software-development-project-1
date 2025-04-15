package codefusion.softwareproject1.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import codefusion.softwareproject1.Models.CategoryClass;
import codefusion.softwareproject1.Models.ChoiceClass;
import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.QuestionsClass.DifficultyLevel;
import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TeacherRepo teacherRepo;
    
    @Autowired
    private QuizRepo quizRepo;
    
    @Autowired
    private QuestionsRepo questionsRepo;
    
    @Autowired
    private ChoiceRepo choiceRepo;
    
    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public void run(String... args) throws Exception {
        loadTeachers();
        loadCategories();
        loadQuizzes();
        loadQuestions();
        loadChoices();
    }
    
    private void loadTeachers() {
        if (teacherRepo.count() == 0) {
            List<TeacherClass> teachers = new ArrayList<>();
            
            TeacherClass teacher1 = new TeacherClass();
            teacher1.setName("John Smith");
            teacher1.setEmail("john.smith@university.edu");
            
            TeacherClass teacher2 = new TeacherClass();
            teacher2.setName("Sarah Johnson");
            teacher2.setEmail("sarah.johnson@university.edu");
            
            TeacherClass teacher3 = new TeacherClass();
            teacher3.setName("Michael Brown");
            teacher3.setEmail("michael.brown@university.edu");
            
            teachers.add(teacher1);
            teachers.add(teacher2);
            teachers.add(teacher3);
            
            teacherRepo.saveAll(teachers);
            
            System.out.println("Sample teachers loaded!");
        }
    }
    
    private void loadCategories() {
        if (categoryRepo.count() == 0) {
            List<CategoryClass> categories = new ArrayList<>();
            
            CategoryClass category1 = new CategoryClass();
            category1.setName("Mathematics");
            category1.setDescription("Topics related to math concepts and problem solving");
            
            CategoryClass category2 = new CategoryClass();
            category2.setName("Computer Science");
            category2.setDescription("Topics related to programming, algorithms and data structures");
            
            CategoryClass category3 = new CategoryClass();
            category3.setName("Physics");
            category3.setDescription("Topics related to physical phenomena and theories");
            
            categories.add(category1);
            categories.add(category2);
            categories.add(category3);
            
            categoryRepo.saveAll(categories);
            
            System.out.println("Sample categories loaded!");
        }
    }
    
    private void loadQuizzes() {
        if (quizRepo.count() == 0) {
            List<TeacherClass> teachers = teacherRepo.findAll();
            List<CategoryClass> categories = categoryRepo.findAll();
            
            if (!teachers.isEmpty() && !categories.isEmpty()) {
                List<QuizClass> quizzes = new ArrayList<>();
                
                QuizClass quiz1 = new QuizClass();
                quiz1.setName("Java Programming Basics");
                quiz1.setDescription("A quiz covering basic Java programming concepts");
                quiz1.setPublished(true);
                quiz1.setCategories(Arrays.asList(categories.get(1))); // Computer Science
                // quiz1.setTeachers(Arrays.asList(teachers.get(0))); // Issue with TeacherClass list
                
                QuizClass quiz2 = new QuizClass();
                quiz2.setName("Calculus I");
                quiz2.setDescription("A quiz covering differential calculus");
                quiz2.setPublished(true);
                quiz2.setCategories(Arrays.asList(categories.get(0))); // Mathematics
                // quiz2.setTeachers(Arrays.asList(teachers.get(1)));
                
                QuizClass quiz3 = new QuizClass();
                quiz3.setName("Mechanics");
                quiz3.setDescription("A quiz covering basic mechanics concepts");
                quiz3.setPublished(false);
                quiz3.setCategories(Arrays.asList(categories.get(2))); // Physics
                // quiz3.setTeachers(Arrays.asList(teachers.get(2)));
                
                quizzes.add(quiz1);
                quizzes.add(quiz2);
                quizzes.add(quiz3);
                
                quizRepo.saveAll(quizzes);
                
                System.out.println("Sample quizzes loaded!");
            }
        }
    }
    
    private void loadQuestions() {
        if (questionsRepo.count() == 0) {
            List<QuizClass> quizzes = quizRepo.findAll();
            
            if (!quizzes.isEmpty()) {
                List<QuestionsClass> questions = new ArrayList<>();
                
                // Java Quiz Questions
                QuestionsClass q1 = new QuestionsClass();
                q1.setQuestionText("What is the main method signature in Java?");
                q1.setAnswer("public static void main(String[] args)");
                q1.setDifficultyLevel(DifficultyLevel.easy);
                q1.setQuiz(quizzes.get(0));
                
                QuestionsClass q2 = new QuestionsClass();
                q2.setQuestionText("Which keyword is used to inherit a class in Java?");
                q2.setAnswer("extends");
                q2.setDifficultyLevel(DifficultyLevel.easy);
                q2.setQuiz(quizzes.get(0));
                
                // Calculus Questions
                QuestionsClass q3 = new QuestionsClass();
                q3.setQuestionText("What is the derivative of x^2?");
                q3.setAnswer("2x");
                q3.setDifficultyLevel(DifficultyLevel.medium);
                q3.setQuiz(quizzes.get(1));
                
                QuestionsClass q4 = new QuestionsClass();
                q4.setQuestionText("What is the integral of 2x?");
                q4.setAnswer("x^2 + C");
                q4.setDifficultyLevel(DifficultyLevel.medium);
                q4.setQuiz(quizzes.get(1));
                
                // Physics Questions
                QuestionsClass q5 = new QuestionsClass();
                q5.setQuestionText("What is Newton's Second Law?");
                q5.setAnswer("F = ma");
                q5.setDifficultyLevel(DifficultyLevel.hard);
                q5.setQuiz(quizzes.get(2));
                
                QuestionsClass q6 = new QuestionsClass();
                q6.setQuestionText("What is the unit of Force in SI units?");
                q6.setAnswer("Newton");
                q6.setDifficultyLevel(DifficultyLevel.easy);
                q6.setQuiz(quizzes.get(2));
                
                questions.add(q1);
                questions.add(q2);
                questions.add(q3);
                questions.add(q4);
                questions.add(q5);
                questions.add(q6);
                
                questionsRepo.saveAll(questions);
                
                System.out.println("Sample questions loaded!");
            }
        }
    }
    
    private void loadChoices() {
        if (choiceRepo.count() == 0) {
            List<QuestionsClass> questions = questionsRepo.findAll();
            
            if (!questions.isEmpty()) {
                List<ChoiceClass> choices = new ArrayList<>();
                
                // Choices for Java main method question
                ChoiceClass c1 = new ChoiceClass();
                c1.setOptionText("public static void main(String[] args)");
                c1.setCorrect(true);
                c1.setQuestion(questions.get(0));
                
                ChoiceClass c2 = new ChoiceClass();
                c2.setOptionText("public void main(String[] args)");
                c2.setCorrect(false);
                c2.setQuestion(questions.get(0));
                
                ChoiceClass c3 = new ChoiceClass();
                c3.setOptionText("static void main(String args[])");
                c3.setCorrect(false);
                c3.setQuestion(questions.get(0));
                
                ChoiceClass c4 = new ChoiceClass();
                c4.setOptionText("void main(String args)");
                c4.setCorrect(false);
                c4.setQuestion(questions.get(0));
                
                // Choices for Java inheritance question
                ChoiceClass c5 = new ChoiceClass();
                c5.setOptionText("extends");
                c5.setCorrect(true);
                c5.setQuestion(questions.get(1));
                
                ChoiceClass c6 = new ChoiceClass();
                c6.setOptionText("implements");
                c6.setCorrect(false);
                c6.setQuestion(questions.get(1));
                
                ChoiceClass c7 = new ChoiceClass();
                c7.setOptionText("inherits");
                c7.setCorrect(false);
                c7.setQuestion(questions.get(1));
                
                ChoiceClass c8 = new ChoiceClass();
                c8.setOptionText("super");
                c8.setCorrect(false);
                c8.setQuestion(questions.get(1));
                
                // Add choices for Calculus questions
                ChoiceClass c9 = new ChoiceClass();
                c9.setOptionText("2x");
                c9.setCorrect(true);
                c9.setQuestion(questions.get(2));
                
                ChoiceClass c10 = new ChoiceClass();
                c10.setOptionText("x");
                c10.setCorrect(false);
                c10.setQuestion(questions.get(2));
                
                ChoiceClass c11 = new ChoiceClass();
                c11.setOptionText("x^2");
                c11.setCorrect(false);
                c11.setQuestion(questions.get(2));
                
                ChoiceClass c12 = new ChoiceClass();
                c12.setOptionText("2");
                c12.setCorrect(false);
                c12.setQuestion(questions.get(2));
                
                choices.add(c1);
                choices.add(c2);
                choices.add(c3);
                choices.add(c4);
                choices.add(c5);
                choices.add(c6);
                choices.add(c7);
                choices.add(c8);
                choices.add(c9);
                choices.add(c10);
                choices.add(c11);
                choices.add(c12);
                
                choiceRepo.saveAll(choices);
                
                System.out.println("Sample choices loaded!");
            }
        }
    }
} 